package io.vos.stun.protocol;

import io.vos.stun.message.Message;

public class BaseMethodProcessor implements MethodProcessor {

  private final int method;

  public BaseMethodProcessor(int method) {
    this.method = method;
  }

  @Override
  public final int getMethod() {
    return method;
  }

  @Override
  public boolean isClassSupported(int messageClass) {
    return false;
  }

  @Override
  public void processRequest(Message message) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void processIndication(Message message) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void processResponse(Message message) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void processError(Message message) {
    throw new UnsupportedOperationException();
  }
}
