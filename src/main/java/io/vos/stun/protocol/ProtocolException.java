package io.vos.stun.protocol;

import io.vos.stun.attribute.Attribute;

import javax.annotation.Nullable;

public final class ProtocolException extends Exception {

  public enum ReasonCode  {
    FIRST_TWO_BITS_NOT_ZERO(ErrorCode.ERROR_400),
    INVALID_MESSAGE_LENGTH(ErrorCode.ERROR_400),
    UNKNOWN_ATTRIBUTE(ErrorCode.ERROR_420),
    UNSUPPORTED_METHOD(ErrorCode.ERROR_500),
    UNSUPPORTED_CLASS_FOR_METHOD(ErrorCode.ERROR_500);

    private ErrorCode errorCode;

    ReasonCode(ErrorCode errorCode) {
      this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
      return errorCode;
    }
  }

  private final ReasonCode code;
  private final Attribute errorAttribute;

  public ProtocolException(ReasonCode code) {
    this(code, "");
  }

  public ProtocolException(ReasonCode code, String message) {
    this(code, message, null);
  }

  public ProtocolException(ReasonCode code, String message, @Nullable Attribute errorAttribute) {
    super(message);
    this.code = code;
    this.errorAttribute = errorAttribute;
  }

  public ReasonCode getReasonCode() {
    return code;
  }
}
