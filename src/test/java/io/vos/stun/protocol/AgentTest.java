package io.vos.stun.protocol;

import static io.vos.stun.message.Messages.*;
import static io.vos.stun.testing.Messages.*;
import static org.junit.Assert.*;

import io.vos.stun.message.Message;
import io.vos.stun.protocol.ProtocolException.ReasonCode;
import io.vos.stun.testing.FakeMethodProcessor;
import io.vos.stun.testing.FakeResponseReceiver;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AgentTest {

  private static final int FAKE_METHOD_1 = Integer.MAX_VALUE;
  private static final int FAKE_METHOD_2 = Integer.MIN_VALUE;
  private static final List<MethodProcessor> EMPTY_METHOD_PROCESSOR_LIST = new ArrayList<>();

  private ResponseReceiver responseReceiver;

  @Before
  public void setUp() {
    responseReceiver = new FakeResponseReceiver();
  }

  @Test
  public void constructorThrowsOnDuplicateMethodProcessors() {
    MethodProcessor p1 = new FakeMethodProcessor(FAKE_METHOD_1);
    MethodProcessor p2 = new FakeMethodProcessor(FAKE_METHOD_1);

    try {
      new Agent(Lists.newArrayList(p1, p2));
      fail("Should have thrown an illegal state exception");
    } catch (IllegalStateException expected) {}
  }

  @Test
  public void onMessage_processesValidRequestMethod() throws Exception {
    byte[] messageBytes = hexToBytes(SAMPLE_REQUEST_1);
    Message expectedMessage = new Message(messageBytes);
    MethodProcessor proc = new FakeMethodProcessor(MESSAGE_METHOD_BINDING, new int[] {0});
    Agent agent = new Agent(Lists.newArrayList(proc));
    agent.onMessage(messageBytes, responseReceiver);

    assertEquals(expectedMessage, ((FakeMethodProcessor)proc).getProcessedRequest());
  }

  @Test
  public void onMessage_validatesNonZeroMessgaeHeaderBits() {
    Agent agent = new Agent(EMPTY_METHOD_PROCESSOR_LIST);
    byte[] nonZeroBits = hexToBytes(SAMPLE_REQUEST_1);
    nonZeroBits[0] = (byte)0x80;

    try {
      agent.onMessage(nonZeroBits, responseReceiver);
      fail("Should have thrown a ProtocolException");
    } catch (ProtocolException expected) {
      assertEquals(ReasonCode.FIRST_TWO_BITS_NOT_ZERO, expected.getReasonCode());
    }
  }

  @Test
  public void onMessage_validatesRegisteredMethods() throws Exception {
    Agent agent = new Agent(EMPTY_METHOD_PROCESSOR_LIST);
    byte[] bindingMessageBytes = hexToBytes(SAMPLE_REQUEST_1);

    try {
      agent.onMessage(bindingMessageBytes, responseReceiver);
      fail("Should have thrown a ProtocolException");
    } catch (ProtocolException expected) {
      assertEquals(ReasonCode.UNSUPPORTED_METHOD, expected.getReasonCode());
    }
  }

  @Test
  public void onMessage_validatesMethodProcessorSupportsClass() throws Exception {
    MethodProcessor proc = new FakeMethodProcessor(1);
    Agent agent = new Agent(Lists.newArrayList(proc));
    byte[] bindingMessageBytes = hexToBytes(SAMPLE_REQUEST_1);

    try {
      agent.onMessage(bindingMessageBytes, responseReceiver);
      fail("Should have thrown a ProtocolException");
    } catch (ProtocolException expected) {
      assertEquals(ReasonCode.UNSUPPORTED_CLASS_FOR_METHOD, expected.getReasonCode());
    }
  }
}
