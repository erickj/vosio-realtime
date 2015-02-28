package io.vos.stun.protocol;

import io.vos.stun.message.Message;

public interface MethodProcessor {

  /** Returns the int value of the method, as defined by IANA. */
  int getMethod();

  /**
   * Returns true if the MethodProcessor handles the message class. This should
   * be used by clients before calling a processXxx method, to determine if the
   * processor can process the message class. Otherwise a runtime exception will
   * be thrown.
   */
  boolean isClassSupported(int messageClass);

  /**
   * Processes the given message. Throws a runtime exception for any message
   * with a method other than that returned by {@code #getMethod}, or if a
   * request is not supported for the method, as returned by
   * {@code isClassSupported}.
   */
  void processRequest(Message message);

  /**
   * Processes the given message. Throws a runtime exception for any message
   * with a method other than that returned by {@code #getMethod}, or if a
   * indication is not supported for the method, as returned by
   * {@code isClassSupported}.
   */
  void processIndication(Message message);

  /**
   * Processes the given message. Throws a runtime exception for any message
   * with a method other than that returned by {@code #getMethod}, or if a
   * response is not supported for the method, as returned by
   * {@code isClassSupported}.
   */
  void processResponse(Message message);

  /**
   * Processes the given message. Throws a runtime exception for any message
   * with a method other than that returned by {@code #getMethod}, or if a
   * error is not supported for the method, as returned by
   * {@code isClassSupported}.
   */
  void processError(Message message);

}
