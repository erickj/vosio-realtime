package io.vos.stun.message;

/**
 * @see https://www.iana.org/assignments/stun-parameters/stun-parameters.txt
 */
public class Messages {

  private Messages() {}

  public static final int MAGIC_COOKIE_FIXED_VALUE = 0x2112a442;

  /** Lengths of message header parts in bytes */
  public static final int MESSAGE_LEN_HEADER = 20;
  public static final int MESSAGE_LEN_TYPE = 2;
  public static final int MESSAGE_LEN_LENGTH = 2;
  public static final int MESSAGE_LEN_MAGIC_COOKIE = 4;
  public static final int MESSAGE_LEN_TRANSACTION_ID = 12;

  /** Start position of message header parts in bytes */
  public static final int MESSAGE_POS_TYPE = 0;
  public static final int MESSAGE_POS_LENGTH = 2;
  public static final int MESSAGE_POS_MAGIC_COOKIE = 4;
  public static final int MESSAGE_POS_TRANSACTION_ID = 8;

  /** RFC 5389 message classes */
  public static final int MESSAGE_CLASS_REQUEST = 0;
  public static final int MESSAGE_CLASS_INDICATION = 1;
  public static final int MESSAGE_CLASS_RESPONSE = 2;
  public static final int MESSAGE_CLASS_ERROR_RESPONSE = 3;

  /** RFC 5389 methods */
  public static final int MESSAGE_METHOD_RESERVED = 0x000;
  public static final int MESSAGE_METHOD_BINDING = 0x001;
  public static final int MESSAGE_METHOD_SHARED_SECRET = 0x002;

  /** RFC 5766 methods */
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

  /** STUN Error Codes */
  public enum ErrorCode {
                                                      // 0-299  Reserved
    ERROR_300(300, "Try Alternate"),                  // RFC5389
                                                      // 301-399 Unassigned
    ERROR_400(400, "Bad Request"),                    // RFC5389
    ERROR_401(401, "Unauthorized"),                   // RFC5389
                                                      // 402   Unassigned
    ERROR_403(403, "Forbidden"),                      // RFC5766
                                                      // 404-419 Unassigned
    ERROR_420(420, "Unknown Attribute"),              // RFC5389
                                                      // 421-436 Unassigned
    ERROR_437(437, "Allocation Mismatch"),            // RFC5766
    ERROR_438(438, "Stale Nonce"),                    // RFC5389
                                                      // 439   Unassigned
    ERROR_440(440, "Address Family not Supported"),   // RFC6156
    ERROR_441(441, "Wrong Credentials"),              // RFC5766
    ERROR_442(442, "Unsupported Transport Protocol"), // RFC5766
    ERROR_443(443, "Peer Address Family Mismatch"),   // RFC6156
                                                      // 444-445 Unassigned
    ERROR_446(446, "Connection Already Exists"),      // RFC6062
    ERROR_447(447, "Connection Timeout or Failure"),  // RFC6062
                                                      // 448-485 Unassigned
    ERROR_486(486, "Allocation Quota Reached"),       // RFC5766
    ERROR_487(487, "Role Conflict"),                  // RFC5245
                                                      // 488-499 Unassigned
    ERROR_500(500, "Server Error"),                   // RFC5389
                                                      // 501-507 Unassigned
    ERROR_508(508, "Insufficient Capacity");          // RFC5766
                                                      // 509-699 Unassigned

    private final int code;
    private final String status;

    ErrorCode(int code, String status) {
      this.code = code;
      this.status = status;
    }

    public int getCode() {
      return code;
    }

    public String getStatus() {
      return status;
    }
  }
}
