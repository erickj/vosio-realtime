package io.vos.stun.protocol;

public final class ProtocolException extends Exception {

  public enum ReasonCode  {
    FIRST_TWO_BITS_NOT_ZERO,
    INVALID_MAGIC_COOKIE,
    INVALID_MESSAGE_LENGTH,
    UNSUPPORTED_METHOD,
    UNSUPPORTED_CLASS_FOR_METHOD
  }

  private final ReasonCode code;

  public ProtocolException(ReasonCode code) {
    this(code, "");
  }

  public ProtocolException(ReasonCode code, String message) {
    super(message);
    this.code = code;
  }

  public ReasonCode getReasonCode() {
    return code;
  }
}
