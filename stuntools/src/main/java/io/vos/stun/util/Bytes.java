package io.vos.stun.util;

import com.google.common.base.Preconditions;

public final class Bytes {

  public static int byteToInt(byte value) {
    return value & 0xff;
  }

  public static int twoBytesToInt(byte[] bytes) {
    Preconditions.checkArgument(bytes.length == 2, "Byte array must be two");
    return ((bytes[0] & 0xff) << 8) | bytes[1] & 0xff;
  }

}
