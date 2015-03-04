package io.vos.stun.attribute;

import io.vos.stun.message.Message;
import io.vos.stun.protocol.ProtocolException;

public interface AttributeFactory {

  /**
   * Creates the Attribute instance indicated by type.
   */
  Attribute createAttribute(int type, int length, byte[] valueData, Message message);

}
