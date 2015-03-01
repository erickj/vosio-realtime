package io.vos.stun.message;

/**
 * @see https://www.iana.org/assignments/stun-parameters/stun-parameters.txt
 */
public class Messages {

  private Messages() {}

  public static final int MAGIC_COOKIE_FIXED_VALUE = 0x2112a442;

  /**
   * Lengths of various message parts in bytes
   */
  public static final int MESSAGE_HEADER_LEN = 20;
  public static final int MESSAGE_TRANSACTION_ID_LEN = 12;

  public static final int MESSAGE_CLASS_REQUEST = 0;
  public static final int MESSAGE_CLASS_INDICATION = 1;
  public static final int MESSAGE_CLASS_RESPONSE = 2;
  public static final int MESSAGE_CLASS_ERROR_RESPONSE = 3;

  /** RFC 5389 */
  public static final int MESSAGE_METHOD_RESERVED = 0x000;
  public static final int MESSAGE_METHOD_BINDING = 0x001;
  public static final int MESSAGE_METHOD_SHARED_SECRET = 0x002;

  /** RFC 5766 */
  public static final int MESSAGE_METHOD_ALLOCATE = 0x003;
  public static final int MESSAGE_METHOD_REFRESH = 0x004;
  public static final int MESSAGE_METHOD_UNASSIGNED_1 = 0x005;
  public static final int MESSAGE_METHOD_SEND = 0x006;
  public static final int MESSAGE_METHOD_DATA = 0x007;
  public static final int MESSAGE_METHOD_CREATE_PERMISSION = 0x008;
  public static final int MESSAGE_METHOD_CHANNEL_BIND = 0x009;
  public static final int MESSAGE_METHOD_CONNECT = 0x00A;
  public static final int MESSAGE_METHOD_CONNECTION_BIND = 0x00B;
  public static final int MESSAGE_METHOD_CONNECTION_ATTEMPT = 0x00C;
}
