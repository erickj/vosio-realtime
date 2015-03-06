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

import org.junit.Before;
import org.junit.Test;

public class AttributesDecoderTest {

  AttributeFactory attrFactory;

  @Before
  public void setUp() {
    attrFactory = new AttributeFactory() {
        @Override
        public Attribute createAttribute(int type, int length, byte[] valueData) {
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
    AttributesCollection attrCollection = decoder.decodeMessageAttributes(msgNoAttrs);
    assertEquals(6, attrCollection.size());

    Attribute softwareAttr = attrCollection.getFirstAttributeOfType(ATTRIBUTE_SOFTWARE);
    assertEquals(16, softwareAttr.getLength());
    assertEquals("STUN test client", new String(softwareAttr.getValue(), "UTF-8"));

    Attribute usernameAttr = attrCollection.getFirstAttributeOfType(ATTRIBUTE_USERNAME);
    assertEquals(9, usernameAttr.getLength());
    assertEquals("evtj:h6vY", new String(usernameAttr.getValue(), "UTF-8"));

    assertNotNull(attrCollection.getFirstAttributeOfType(ATTRIBUTE_FINGERPRINT));
  }
}
