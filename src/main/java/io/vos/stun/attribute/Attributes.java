package io.vos.stun.attribute;

/**
 * @see https://www.iana.org/assignments/stun-parameters/stun-parameters.txt
 */
public final class Attributes {

  private Attributes() {}

  /** RFC 5389 Comprehension-required range (0x0000-0x7FFF): */
  public static final int ATTRIBUTE_RESERVED = 0x0000;
  public static final int ATTRIBUTE_MAPPED_ADDRESS = 0x0001;
  public static final int ATTRIBUTE_RESPONSE_ADDRESS = 0x0002; // deprecated
  public static final int ATTRIBUTE_CHANGE_ADDRESS = 0x0003;   // deprecated
  public static final int ATTRIBUTE_SOURCE_ADDRESS = 0x0004;   // deprecated
  public static final int ATTRIBUTE_CHANGED_ADDRESS = 0x0005;  // deprecated
  public static final int ATTRIBUTE_USERNAME = 0x0006;
  public static final int ATTRIBUTE_PASSWORD = 0x0007;         // deprecated
  public static final int ATTRIBUTE_MESSAGE_INTEGRITY = 0x0008;
  public static final int ATTRIBUTE_ERROR_CODE = 0x0009;
  public static final int ATTRIBUTE_UNKNOWN_ATTRIBUTES = 0x000A;
  public static final int ATTRIBUTE_REFLECTED_FROM = 0x000B;   // deprecated
  public static final int ATTRIBUTE_REALM = 0x0014;
  public static final int ATTRIBUTE_NONCE = 0x0015;
  public static final int ATTRIBUTE_XOR_MAPPED_ADDRESS = 0x0020;

  /** RFC 5389 Comprehension-optional range (0x8000-0xFFFF) */
  public static final int ATTRIBUTE_SOFTWARE = 0x8022;
  public static final int ATTRIBUTE_ALTERNATE_SERVER = 0x8023;
  public static final int ATTRIBUTE_FINGERPRINT = 0x8028;

  /** MAPPED_ADDRESS Address Family */
  public static final byte ADDRESS_FAMILY_IPV4 = 0x01;
  public static final byte ADDRESS_FAMILY_IPV6 = 0x02;

  /**
   * Returns whether the given attribute type is in the comprehension-required
   * range. Throws a runtime-exception if the value of type is outside of the
   * valid attribute type range.
   */
  public static boolean isComprehensionRequired(int type) {
    if (type < 0 || type > 0xffff) {
      String errorMsg = "Attribute type outside of valid range 0x0000-0xffff, got: %d";
      throw new IllegalArgumentException(String.format(errorMsg, type));
    }
    return type <= 0x7fff;
  }
}
