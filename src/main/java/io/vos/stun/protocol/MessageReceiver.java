package io.vos.stun.protocol;

public interface MessageReceiver {

  void onMessage(byte[] messageData, ResponseReceiver responseReceiver)
      throws ProtocolException;

}
