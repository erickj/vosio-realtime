package io.vos.stun.testing;

import io.vos.stun.protocol.ResponseHandler;

public final class FakeResponseHandler implements ResponseHandler {

  byte[] messageData;

  public void onResponse(byte[] messageData) {
    this.messageData = messageData;
  }
}
