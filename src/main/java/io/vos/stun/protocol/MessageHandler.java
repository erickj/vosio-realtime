package io.vos.stun.protocol;

public interface MessageHandler {

  void onMessage(byte[] messageData, ResponseHandler responseHandler)
      throws ProtocolException;

}
