package io.vos.stun.testing;

import io.vos.stun.attribute.Attribute;
import io.vos.stun.message.Message;
import io.vos.stun.protocol.BaseMethodProcessor;

public class FakeMethodProcessor extends BaseMethodProcessor {

  private Message processedRequest;

  public FakeMethodProcessor(int method, int... supportedClasses) {
    super(method, supportedClasses);
  }

  public Message getProcessedRequest() {
    return processedRequest;
  }

  @Override
  public Message processRequestInternal(Message message, Iterable<Attribute> attributes) {
    processedRequest = message;
    return message;
  }
}
