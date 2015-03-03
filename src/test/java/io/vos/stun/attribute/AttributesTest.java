package io.vos.stun.attribute;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {

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
}
