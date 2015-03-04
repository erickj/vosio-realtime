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

  public static int twoBytesToInt(byte byte1, byte byte2) {
    return ((byte1 & 0xff) << 8) | byte2 & 0xff;
  }

  public static int fourBytesToInt(
      byte byte1, byte byte2, byte byte3, byte byte4) {
    return (byte1 & 0xff) << 24 |
        (byte2 & 0xff) << 16 |
        (byte3 & 0xff) << 8 |
        byte4 & 0xff;
  }

  public static byte[] padTo4ByteBoundary(byte[] data) {
    int remainder = data.length % 4;
    if (remainder % 4 == 0) {
      return data;
    }
    byte[] padding = new byte[remainder];
    for (int i = 0; i < remainder; i++) {
      padding[i] = 0x00;
    }
    return com.google.common.primitives.Bytes.concat(data, padding);
  }
}
