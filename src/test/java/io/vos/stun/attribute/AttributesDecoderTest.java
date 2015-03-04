package io.vos.stun.attribute;

import static io.vos.stun.attribute.Attributes.*;
import static io.vos.stun.testing.Messages.*;
import static org.junit.Assert.*;

import io.vos.stun.attribute.Attribute;
import io.vos.stun.attribute.AttributeFactory;
import io.vos.stun.attribute.AttributesDecoder;
import io.vos.stun.message.Message;
import io.vos.stun.testing.FakeAttribute;
import io.vos.stun.testing.Messages;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AttributesDecoderTest {

  AttributeFactory attrFactory;

  @Before
  public void setUp() {
    attrFactory = new AttributeFactory() {
        @Override
        public Attribute createAttribute(int type, int length, byte[] valueData, Message m) {
          return new FakeAttribute(type, length, valueData);
        }
      };
  }

  @Test
  public void decodeMessageAttributes_withNoAttributes() {
    Message msgNoAttrs = Messages.createMessage(NO_ATTRIBUTE_MESSAGE);

    AttributesDecoder decoder = new AttributesDecoder(attrFactory);
    assertTrue(decoder.decodeMessageAttributes(msgNoAttrs).isEmpty());
  }

  @Test
  public void decodeMessageAttributes_withAttributes() throws Exception {
    Message msgNoAttrs = Messages.createMessage(SAMPLE_REQUEST_1);

    AttributesDecoder decoder = new AttributesDecoder(attrFactory);
    List<Attribute> attrList = decoder.decodeMessageAttributes(msgNoAttrs);
    assertEquals(6, attrList.size());

    assertEquals(ATTRIBUTE_SOFTWARE, attrList.get(0).getType());
    assertEquals(16, attrList.get(0).getLength());
    assertEquals("STUN test client", new String(attrList.get(0).getValue(), "UTF-8"));

    assertEquals(ATTRIBUTE_USERNAME, attrList.get(3).getType());
    assertEquals(9, attrList.get(3).getLength());
    assertEquals("evtj:h6vY", new String(attrList.get(3).getValue(), "UTF-8"));

    assertEquals(ATTRIBUTE_FINGERPRINT, attrList.get(5).getType());
  }
}
