package io.vos.stun.protocol;

public interface ResponseHandler {

  void onResponse(byte[] messageData);

}
