package io.vos.stun.testing;

import io.vos.stun.message.Message;
import io.vos.stun.protocol.BaseMethodProcessor;

public class FakeMethodProcessor extends BaseMethodProcessor {

  private Message processedRequest;

  public FakeMethodProcessor(int method) {
    this(method, new int[] {});
  }

  public FakeMethodProcessor(int method, int[] supportedClasses) {
    super(method, supportedClasses);
  }

  public Message getProcessedRequest() {
    return processedRequest;
  }

  @Override
  public Message processRequest(Message message) {
    processedRequest = message;
    return message;
  }
}
