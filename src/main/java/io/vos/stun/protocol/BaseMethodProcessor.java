package io.vos.stun.protocol;

import static io.vos.stun.message.Messages.*;

import io.vos.stun.message.Message;

import com.google.common.base.Preconditions;

import java.util.Arrays;

public class BaseMethodProcessor implements MethodProcessor {

  private final int method;
  private final int[] supportedClasses;

  public BaseMethodProcessor(int method, int[] supportedClasses) {
    this.method = method;
    this.supportedClasses = Preconditions.checkNotNull(supportedClasses);
  }

  @Override
  public final int getMethod() {
    return method;
  }

  @Override
  public final boolean isClassSupported(int methodClass) {
    return Arrays.binarySearch(supportedClasses, methodClass) >= 0;
  }

  @Override
  public Message processRequest(Message message) {
    if (!isClassSupported(MESSAGE_CLASS_REQUEST)) {
      throw new UnsupportedOperationException();
    }
    return new Message(new byte[] {});
  }

  @Override
  public void processIndication(Message message) {
    if (!isClassSupported(MESSAGE_CLASS_INDICATION)) {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public void processResponse(Message message) {
    if (!isClassSupported(MESSAGE_CLASS_RESPONSE)) {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public void processError(Message message) {
    if (!isClassSupported(MESSAGE_CLASS_ERROR_RESPONSE)) {
      throw new UnsupportedOperationException();
    }
  }
}
