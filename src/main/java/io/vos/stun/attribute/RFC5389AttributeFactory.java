package io.vos.stun.attribute;

import static io.vos.stun.attribute.Attributes.*;

import io.vos.stun.protocol.ProtocolException.ReasonCode;
import io.vos.stun.protocol.ProtocolException;

import javax.annotation.Nullable;

public final class RFC5389AttributeFactory implements AttributeFactory {

  public RFC5389AttributeFactory() {}

  @Nullable
  @Override
  public Attribute createAttribute(int type, int length, byte[] valueData) {
    String errorMsgUnknownType = "Unknown attribute type " + type;
    switch (type) {
      case ATTRIBUTE_MAPPED_ADDRESS:
      case ATTRIBUTE_XOR_MAPPED_ADDRESS:
      case ATTRIBUTE_RESERVED:
      case ATTRIBUTE_RESPONSE_ADDRESS:
      case ATTRIBUTE_CHANGE_ADDRESS:
      case ATTRIBUTE_SOURCE_ADDRESS:
      case ATTRIBUTE_CHANGED_ADDRESS:
      case ATTRIBUTE_PASSWORD:
      case ATTRIBUTE_REFLECTED_FROM:
      default:
        return null;
    }
  }
}
