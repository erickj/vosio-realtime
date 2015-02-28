package io.vos.stun.protocol;

import io.vos.stun.message.Message;

public interface MessageReceiver {

  void onMessage(Message message) throws ProtocolException;

}
