package io.vos.stun.protocol;

import static io.vos.stun.message.Messages.*;

import io.vos.stun.message.Message;

public final class BindingProcessor extends BaseMethodProcessor {

  public BindingProcessor() {
    super(MESSAGE_METHOD_BINDING, new int[] {MESSAGE_CLASS_REQUEST});
  }

  @Override
  public void processRequest(Message message) {
  }
}
