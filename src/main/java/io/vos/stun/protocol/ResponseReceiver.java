package io.vos.stun.protocol;

public interface ResponseReceiver {

  void onResponse(byte[] messageData);

}
