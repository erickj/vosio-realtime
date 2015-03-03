package io.vos.stun.attribute;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BaseAttributeTest {

  private byte[] fourByteData;

  @Before
  public void setUp() {
    fourByteData = new byte[] {
      0x01, 0x02, 0x03, 0x04
    };
  }

  @Test
  public void constructorValidatesTypeGreaterThanZero() {
    try {
      new BaseAttribute(-1, 4, fourByteData);
      fail("Should have validated type");
    } catch (IllegalArgumentException expected) {}
  }

  @Test
  public void constructorValidatesDataLengthEndsOn4ByteBoundary() {
    try {
      new BaseAttribute(1, 3, new byte[] { 0x00, 0x00, 0x00 });
      fail("Should have validated data.length boundary");
    } catch (IllegalArgumentException expected) {}
  }

  @Test
  public void constructorValidatesDataNotNull() {
    try {
      new BaseAttribute(1, 4, null);
      fail("Should have validated type");
    } catch (NullPointerException expected) {}
  }

  @Test
  public void getType() {
    Attribute attr = new BaseAttribute(255, 4, fourByteData);
    assertEquals(255, attr.getType());
  }

  @Test
  public void getLength() {
    Attribute attr = new BaseAttribute(255, 4, fourByteData);
    assertEquals(4, attr.getLength());
  }

  @Test
  public void getValue_onBoundary() {
    Attribute attr = new BaseAttribute(255, 4, fourByteData);
    assertNotSame(fourByteData, attr.getValue());
    assertArrayEquals(fourByteData, attr.getValue());
  }

  @Test
  public void getValue_offBoundary() {
    Attribute attr = new BaseAttribute(255, 2, fourByteData);
    byte[] expectedData = new byte[] { fourByteData[0], fourByteData[1] };
    assertArrayEquals(expectedData, attr.getValue());
  }
}
