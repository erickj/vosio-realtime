package io.vos.stun.attribute;

import io.vos.stun.protocol.ProtocolException;

import javax.annotation.Nullable;

public interface AttributeFactory {

  /**
   * Creates the Attribute instance indicated by type. Returns null if the type
   * is not supported or unknown.
   */
  @Nullable
  Attribute createAttribute(int type, int length, byte[] valueData);

}
