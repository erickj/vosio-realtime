package io.vos.stun.protocol;

import io.vos.stun.attribute.Attribute;
import io.vos.stun.message.Message;
import io.vos.stun.message.Messages;

public final class BindingProcessor extends BaseMethodProcessor {

  public BindingProcessor() {
    super(Messages.MESSAGE_METHOD_BINDING, Messages.MESSAGE_CLASS_REQUEST);
  }

  /**
   * Creates a new binding response for either 3489 or 5389 binding requests.
   */
  @Override
  protected Message processRequestInternal(Message message, Iterable<Attribute> attributes) {
    return message.isRFC5389Message()
        ? processRFC5389Request(message, attributes)
        : processRFC3489Request(message, attributes);
  }

  private Message processRFC5389Request(Message message, Iterable<Attribute> attributes) {
    return new Message(new byte[] {});
  }

  private Message processRFC3489Request(Message message, Iterable<Attribute> attributes) {
    return new Message(new byte[] {});
  }
}
