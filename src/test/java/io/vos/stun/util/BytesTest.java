package io.vos.stun.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class BytesTest {

  @Test
  public void intToBytes() {
    // 1011 1010 1010 1011 0001 0000 0000 0001
    byte[] expected = new byte[] {
      (byte)0xba,
      (byte)0xab,
      (byte)0x10,
      (byte)0x01
    };
    assertArrayEquals(expected, Bytes.intToBytes(-1163194367));
  }

  @Test
  public void intToBytes_max1() {
    // 1011 1010 1010 1011 0001 0000 0000 0001
    byte[] expected = new byte[] {
      (byte)0x01
    };
    assertArrayEquals(expected, Bytes.intToBytes(-1163194367, 1));
  }

  @Test
  public void intToBytes_max2() {
    // 1011 1010 1010 1011 0001 0000 0000 0001
    byte[] expected = new byte[] {
      (byte)0x10,
      (byte)0x01
    };
    assertArrayEquals(expected, Bytes.intToBytes(-1163194367, 2));
  }

  @Test
  public void intToBytes_max3() {
    // 1011 1010 1010 1011 0001 0000 0000 0001
    byte[] expected = new byte[] {
      (byte)0xab,
      (byte)0x10,
      (byte)0x01
    };
    assertArrayEquals(expected, Bytes.intToBytes(-1163194367, 3));
  }

  @Test
  public void byteToInt() {
    assertEquals(0, Bytes.byteToInt((byte) 0));
    assertEquals(1, Bytes.byteToInt((byte) 1));
    assertEquals(2, Bytes.byteToInt((byte) 2));
    assertEquals(13, Bytes.byteToInt((byte) 0x0d));
    assertEquals(14, Bytes.byteToInt((byte) 0x0e));
    assertEquals(15, Bytes.byteToInt((byte) 0x0f));
    assertEquals(254, Bytes.byteToInt((byte) 0xfe));
    assertEquals(255, Bytes.byteToInt((byte) 0xff));
  }

  @Test
  public void twoBytesToInt() {
    assertEquals(0, Bytes.twoBytesToInt((byte)0x00, (byte)0x00));
    assertEquals(1, Bytes.twoBytesToInt((byte)0x00, (byte)0x01));
    assertEquals(10, Bytes.twoBytesToInt((byte)0x00, (byte)0x0a));
    assertEquals(15, Bytes.twoBytesToInt((byte)0x00, (byte)0x0f));
    assertEquals(255, Bytes.twoBytesToInt((byte)0x00, (byte)0xff));

    assertEquals(256, Bytes.twoBytesToInt((byte)0x01, (byte)0x00));
    assertEquals(257, Bytes.twoBytesToInt((byte)0x01, (byte)0x01));

    assertEquals(65534, Bytes.twoBytesToInt((byte)0xff, (byte)0xfe));
    assertEquals(65535, Bytes.twoBytesToInt((byte)0xff, (byte)0xff));
  }

  @Test
  public void fourBytesToInt() {
    assertEquals(
        0, Bytes.fourBytesToInt((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00));
    assertEquals(
        1, Bytes.fourBytesToInt((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01));
    assertEquals(
        15, Bytes.fourBytesToInt((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0f));

    assertEquals(256,
        Bytes.fourBytesToInt((byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00));
    assertEquals(65535,
        Bytes.fourBytesToInt((byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff));

    assertEquals(65536,
        Bytes.fourBytesToInt((byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00));
    assertEquals(16777215,
        Bytes.fourBytesToInt((byte)0x00, (byte)0xff, (byte)0xff, (byte)0xff));

    assertEquals(16777216,
        Bytes.fourBytesToInt((byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00));
    assertEquals(2147483647,
        Bytes.fourBytesToInt((byte)0x7f, (byte)0xff, (byte)0xff, (byte)0xff));

    assertEquals(-2147483648,
        Bytes.fourBytesToInt((byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00));
    assertEquals(-1,
        Bytes.fourBytesToInt((byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff));
  }

  @Test
  public void padTo4ByteBoundary_onBoundary() {
    byte[] fourByteArray = new byte[] { 0x01, 0x02, 0x03, 0x04 };
    assertSame(fourByteArray, Bytes.padTo4ByteBoundary(fourByteArray));
  }

  @Test
  public void padTo4ByteBoundary_offBoundary() {
    byte[] twoByteArray = new byte[] { 0x01 };
    byte[] expectedArray = new byte[] { 0x01, 0x00, 0x00, 0x00 };
    assertArrayEquals(expectedArray, Bytes.padTo4ByteBoundary(twoByteArray));
  }

  @Test
  public void concat() {
    byte[] bytes1 = new byte[] { 0x01, 0x02, 0x03 };
    assertArrayEquals(bytes1, Bytes.concat(bytes1));

    byte[] bytes2 = new byte[] { 0x04, 0x05 };
    assertArrayEquals(
        new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 }, Bytes.concat(bytes1, bytes2));

    byte[] bytes3 = new byte[0];
    assertArrayEquals(
        new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 }, Bytes.concat(bytes1, bytes2, bytes3));
  }
}
