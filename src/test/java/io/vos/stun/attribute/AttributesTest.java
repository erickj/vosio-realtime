package io.vos.stun.attribute;

import static io.vos.stun.testing.Messages.*;
import static org.junit.Assert.*;

import io.vos.stun.message.Message;
import io.vos.stun.testing.FakeAttribute;

import java.util.List;

import org.junit.Test;

public class AttributesTest {

  @Test
  public void findAttributeByType() {
    AttributeFactory attrFactory = new AttributeFactory() {
        @Override
        public Attribute createAttribute(int type, int length, byte[] valueData) {
          return new FakeAttribute(type, length, valueData);
        }
      };
    AttributesDecoder decoder = new AttributesDecoder(attrFactory);
    Message msg = createMessage(SAMPLE_REQUEST_1);

    Iterable<Attribute> attrs = decoder.decodeMessageAttributes(msg);
    assertNull(Attributes.findAttributeByType(attrs, Attributes.ATTRIBUTE_MAPPED_ADDRESS));
    assertNotNull(Attributes.findAttributeByType(attrs, Attributes.ATTRIBUTE_SOFTWARE));
  }

  @Test
  public void isComprehensionRequired_throwsOutsideValidRange() {
    try {
      Attributes.isComprehensionRequired(-1);
      fail("Should have thrown an IllegalArgumentException");
    } catch (IllegalArgumentException expected) {}

    try {
      Attributes.isComprehensionRequired(0x10000);
      fail("Should have thrown an IllegalArgumentException");
    } catch (IllegalArgumentException expected) {}
  }

  @Test
  public void isComprehensionRequired_comprehensionRequired() {
    assertTrue(Attributes.isComprehensionRequired(0));
    assertTrue(Attributes.isComprehensionRequired(0x7fff));
  }

  @Test
  public void isComprehensionRequired_comprehensionOptional() {
    assertFalse(Attributes.isComprehensionRequired(0x8000));
    assertFalse(Attributes.isComprehensionRequired(0xffff));
  }

  @Test
  public void isUnknownAttribute_knownAttribute() {
    assertFalse(Attributes.isUnknownAttribute(
        new FakeAttribute(1, 2, new byte[] {0x01, 0x02, 0x03, 0x04})));
  }

  @Test
  public void isUnknownAttribute_unknownAttribute() {
    assertTrue(Attributes.isUnknownAttribute(
        new UnsupportedAttribute(1, 2, new byte[] {0x01, 0x02, 0x03, 0x04})));
  }
}
