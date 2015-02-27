package io.vos.stun.util;

public final class Bytes {

  public static byte[] intToBytes(int value) {
    return new byte[] {
      (byte)((value >>> 24) & 0xff),
      (byte)((value >>> 16) & 0xff),
      (byte)((value >>> 8) & 0xff),
      (byte)(value & 0xff)
    };
  }

  public static int byteToInt(byte value) {
    return value & 0xff;
  }

  public static int twoBytesToInt(byte byte1, byte byte1) {
    return ((byte1 & 0xff) << 8) | byte1 & 0xff;
  }

  public static int fourBytesToInt(
      byte byte1, byte byte2, byte byte3, byte byte4) {
    return (byte1 & 0xff) << 24 |
        (byte2 & 0xff) << 16 |
        (byte3 & 0xff) << 8 |
        byte4 & 0xff;
  }

}
