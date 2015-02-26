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

  public static int twoBytesToInt(byte byteHigh, byte byteLow) {
    return ((byteHigh & 0xff) << 8) | byteLow & 0xff;
  }

}
