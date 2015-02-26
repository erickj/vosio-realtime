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
}
