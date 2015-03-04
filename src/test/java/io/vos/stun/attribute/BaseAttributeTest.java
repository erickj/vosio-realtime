package io.vos.stun.attribute;

import static org.junit.Assert.*;

import com.google.common.primitives.Bytes;

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
  public void getTotalLength() {
    Attribute attr = new BaseAttribute(255, 4, fourByteData);
    assertEquals(8, attr.getTotalLength());

    Attribute attr2 = new BaseAttribute(1, 2, new byte[] {
        0x00, 0x04, 0x00, 0x00,
      });
    assertEquals(8, attr2.getTotalLength());
  }

  @Test
  public void toByteArray() {
    Attribute attr = new BaseAttribute(255, 4, fourByteData);
    byte[] expectedBytes = Bytes.concat(new byte[] { 0x00, (byte)0xff, 0x00, 0x04}, fourByteData);
    assertArrayEquals(expectedBytes, attr.toByteArray());
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


  @Test
  public void hashCodeAndEquals() {
    byte[] bytes1 = new byte[] { 0x01, 0x02, 0x03, 0x04 };
    byte[] bytes2 = new byte[] { 0x05, 0x06, 0x07, 0x08 };

    Attribute attr1 = new BaseAttribute(1, 4, bytes1);
    Attribute attr2 = new BaseAttribute(1, 4, bytes1);
    Attribute attr3 = new BaseAttribute(1, 4, bytes2);
    Attribute attr4 = new BaseAttribute(2, 4, bytes1);
    Attribute attr5 = new BaseAttribute(1, 2, bytes1);

    assertEquals(attr1.hashCode(), attr1.hashCode());
    assertEquals(attr1.hashCode(), attr2.hashCode());
    assertNotEquals(attr1.hashCode(), attr3.hashCode());
    assertNotEquals(attr1.hashCode(), attr4.hashCode());
    assertNotEquals(attr1.hashCode(), attr5.hashCode());

    assertEquals(attr1, attr1);
    assertEquals(attr1, attr2);
    assertNotEquals(attr1, attr3);
    assertNotEquals(attr1, attr4);
    assertNotEquals(attr1, attr5);
    assertNotEquals(attr1, null);
    assertNotEquals(attr1, new Object());
  }

}
