package io.vos.stun.protocol;

import static io.vos.stun.message.Messages.*;
import static io.vos.stun.testing.Messages.*;
import static org.junit.Assert.*;

import io.vos.stun.message.Message;
import io.vos.stun.protocol.ProtocolException.ReasonCode;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AgentTest {

  private static final int FAKE_METHOD_1 = Integer.MAX_VALUE;
  private static final int FAKE_METHOD_2 = Integer.MIN_VALUE;

  private static final List<MethodProcessor> EMPTY_METHOD_PROCESSOR_LIST = new ArrayList<>();

  private MethodProcessor fakeMethodProcessor;

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
    Message validMessage = new Message(hexToBytes(SAMPLE_REQUEST_1));
    MethodProcessor proc = new FakeMethodProcessor(MESSAGE_METHOD_BINDING, new int[] {0});
    Agent agent = new Agent(Lists.newArrayList(proc));
    agent.onMessage(validMessage);

    assertEquals(validMessage, ((FakeMethodProcessor)proc).getProcessedRequest());
  }

  @Test
  public void onMessage_validatesNonZeroMessgaeHeaderBits() {
    Agent agent = new Agent(EMPTY_METHOD_PROCESSOR_LIST);
    byte[] nonZeroBits = hexToBytes(SAMPLE_REQUEST_1);
    nonZeroBits[0] = (byte)0x80;

    Message nonZeroBitsMessage = new Message(nonZeroBits);
    try {
      agent.onMessage(nonZeroBitsMessage);
      fail("Should have thrown a ProtocolException");
    } catch (ProtocolException expected) {
      assertEquals(ReasonCode.FIRST_TWO_BITS_NOT_ZERO, expected.getReasonCode());
    }
  }

  @Test
  public void onMessage_validatesRegisteredMethods() throws Exception {
    Agent agent = new Agent(EMPTY_METHOD_PROCESSOR_LIST);
    Message bindingMessage = new Message(hexToBytes(SAMPLE_REQUEST_1));

    try {
      agent.onMessage(bindingMessage);
      fail("Should have thrown a ProtocolException");
    } catch (ProtocolException expected) {
      assertEquals(ReasonCode.UNSUPPORTED_METHOD, expected.getReasonCode());
    }
  }

  @Test
  public void onMessage_validatesMethodProcessorSupportsClass() throws Exception {
    Message bindingMessage = new Message(hexToBytes(SAMPLE_REQUEST_1));
    MethodProcessor proc = new FakeMethodProcessor(1);
    Agent agent = new Agent(Lists.newArrayList(proc));

    try {
      agent.onMessage(bindingMessage);
      fail("Should have thrown a ProtocolException");
    } catch (ProtocolException expected) {
      assertEquals(ReasonCode.UNSUPPORTED_CLASS_FOR_METHOD, expected.getReasonCode());
    }
  }

  private static class FakeMethodProcessor extends BaseMethodProcessor {

    private Message processedRequest;

    FakeMethodProcessor(int method) {
      this(method, new int[] {});
    }

    FakeMethodProcessor(int method, int[] supportedClasses) {
      super(method, supportedClasses);
    }

    public Message getProcessedRequest() {
      return processedRequest;
    }

    @Override
    public void processRequest(Message message) {
      processedRequest = message;
    }
  }

}
