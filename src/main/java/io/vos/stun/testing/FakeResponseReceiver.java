package io.vos.stun.testing;

import io.vos.stun.protocol.ResponseReceiver;

public final class FakeResponseReceiver implements ResponseReceiver {

  byte[] messageData;

  public void onResponse(byte[] messageData) {
    this.messageData = messageData;
  }
}
