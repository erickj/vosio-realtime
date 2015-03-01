package io.vos.stun.protocol;

import static io.vos.stun.message.Messages.*;

import io.vos.stun.message.Message;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * A generic STUN agent that follows the message processing rules described in
 * RFC 5389.
 *
 * {@see https://tools.ietf.org/html/rfc5389#section-7.3}
 */
public class Agent implements MessageReceiver {

  private final Map<Integer, MethodProcessor> registeredMethodProcessors;

  public Agent(Iterable<MethodProcessor> methodProcessors) {
    registeredMethodProcessors = Maps.<Integer, MethodProcessor>newHashMap();
    for (MethodProcessor p : methodProcessors) {
      Preconditions.checkNotNull(p);
      int method = p.getMethod();
      Preconditions.checkState(!registeredMethodProcessors.containsKey(method));
      registeredMethodProcessors.put(method, p);
    }
  }

  @Override
  public final void onMessage(Message message) throws ProtocolException {
    validateMessage(Preconditions.checkNotNull(message));

    MethodProcessor proc =
        Preconditions.checkNotNull(registeredMethodProcessors.get(message.getMessageMethod()));
    switch (message.getMessageClass()) {
      case MESSAGE_CLASS_REQUEST:
        proc.processRequest(message);
        return;
      case MESSAGE_CLASS_INDICATION:
        proc.processIndication(message);
        return;
      case MESSAGE_CLASS_RESPONSE:
        proc.processResponse(message);
        return;
      case MESSAGE_CLASS_ERROR_RESPONSE:
        proc.processError(message);
        return;
      default:
        throw new AssertionError("Handling invalid message class, this should have been validated");
    }
  }

  private void validateMessage(Message message) throws ProtocolException {
    if (!message.hasNonZeroHeaderBits()) {
      byte[] headerBytes = message.getHeaderBytes();
      String errorMsg = String.format("bit 0: %d, byte 1: %d", headerBytes[0], headerBytes[1]);
      throw new ProtocolException(ProtocolException.ReasonCode.FIRST_TWO_BITS_NOT_ZERO, errorMsg);
    } else if (!message.hasMagicCookie()) {
      throw new ProtocolException(ProtocolException.ReasonCode.INVALID_MAGIC_COOKIE);
    }

    int msgLength = message.getMessageLength();
    int actualMessageLength = message.getAttributeBytes().length;
    if (msgLength != actualMessageLength) {
      String errorMsg = String.format(
          "message length from header was %d but was actually %d", msgLength, actualMessageLength);
      throw new ProtocolException(ProtocolException.ReasonCode.INVALID_MESSAGE_LENGTH, errorMsg);
    }

    int msgMethod = message.getMessageMethod();
    if (!registeredMethodProcessors.containsKey(msgMethod)) {
      String errorMsg =
          String.format("unsupported message method %d", msgMethod);
      throw new ProtocolException(ProtocolException.ReasonCode.UNSUPPORTED_METHOD, errorMsg);
    }

    int msgClass = message.getMessageClass();
    if (!registeredMethodProcessors.get(msgMethod).isClassSupported(msgClass)) {
      String errorMsg =
          String.format("unsupported message class %d for method %d", msgClass, msgMethod);
      throw new ProtocolException(
          ProtocolException.ReasonCode.UNSUPPORTED_CLASS_FOR_METHOD, errorMsg);
    }
  }

}
