package io.vos.stun.protocol;

import static io.vos.stun.message.Messages.*;
import static io.vos.stun.testing.Messages.*;
import static org.junit.Assert.*;

import io.vos.stun.message.Message;
import io.vos.stun.protocol.ProtocolException.ReasonCode;
import io.vos.stun.testing.FakeMethodProcessor;
import io.vos.stun.testing.FakeResponseHandler;

import com.google.common.collect.Lists;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AgentTest {

  private static final int FAKE_METHOD_1 = Integer.MAX_VALUE;
  private static final int FAKE_METHOD_2 = Integer.MIN_VALUE;
  private static final List<MethodProcessor> EMPTY_METHOD_PROCESSOR_LIST = new ArrayList<>();
  private static final InetSocketAddress INET_SOCKET = createInetSocketAddress();

  private static InetSocketAddress createInetSocketAddress() {
    try {
      return new InetSocketAddress(InetAddress.getByName("20.40.80.160"), 54321);
    } catch (UnknownHostException e) {
      throw new Error(e);
    }
  }

  private ResponseHandler responseHandler;

  @Before
  public void setUp() {
    responseHandler = new FakeResponseHandler();
  }

  @Test
  public void constructorThrowsOnDuplicateMethodProcessors() {
    MethodProcessor p1 = new FakeMethodProcessor(FAKE_METHOD_1, 1);
    MethodProcessor p2 = new FakeMethodProcessor(FAKE_METHOD_1, 1);

    try {
      new Agent(Lists.newArrayList(p1, p2));
      fail("Should have thrown an illegal state exception");
    } catch (IllegalStateException expected) {}
  }

  @Test
  public void onMessage_processesValidRequestMethod() throws Exception {
    byte[] messageBytes = hexToBytes(EMPTY_BINDING_REQUEST);
    Message expectedMessage = new Message(messageBytes);
    MethodProcessor proc =
        new FakeMethodProcessor(MESSAGE_METHOD_BINDING, MESSAGE_CLASS_REQUEST);
    Agent agent = new Agent(Lists.newArrayList(proc));
    agent.onMessage(messageBytes, INET_SOCKET, responseHandler);

    assertEquals(expectedMessage, ((FakeMethodProcessor)proc).getProcessedRequest());
  }

  // @Test
  // public void onMessage_validatesNonZeroMessgaeHeaderBits() {
  //   Agent agent = new Agent(EMPTY_METHOD_PROCESSOR_LIST);
  //   byte[] nonZeroBits = hexToBytes(SAMPLE_REQUEST_1);
  //   nonZeroBits[0] = (byte)0x80;

  //   try {
  //     agent.onMessage(nonZeroBits, INET_SOCKET, responseHandler);
  //     fail("Should have thrown a ProtocolException");
  //   } catch (ProtocolException expected) {
  //     assertEquals(ReasonCode.FIRST_TWO_BITS_NOT_ZERO, expected.getReasonCode());
  //   }
  // }

  // @Test
  // public void onMessage_validatesRegisteredMethods() throws Exception {
  //   Agent agent = new Agent(EMPTY_METHOD_PROCESSOR_LIST);
  //   byte[] bindingMessageBytes = hexToBytes(SAMPLE_REQUEST_1);

  //   try {
  //     agent.onMessage(bindingMessageBytes, INET_SOCKET, responseHandler);
  //     fail("Should have thrown a ProtocolException");
  //   } catch (ProtocolException expected) {
  //     assertEquals(ReasonCode.UNSUPPORTED_METHOD, expected.getReasonCode());
  //   }
  // }

  // @Test
  // public void onMessage_validatesMethodProcessorSupportsClass() throws Exception {
  //   MethodProcessor proc = new FakeMethodProcessor(1, 99);
  //   Agent agent = new Agent(Lists.newArrayList(proc));
  //   byte[] bindingMessageBytes = hexToBytes(SAMPLE_REQUEST_1);

  //   try {
  //     agent.onMessage(bindingMessageBytes, INET_SOCKET, responseHandler);
  //     fail("Should have thrown a ProtocolException");
  //   } catch (ProtocolException expected) {
  //     assertEquals(ReasonCode.UNSUPPORTED_CLASS_FOR_METHOD, expected.getReasonCode());
  //   }
  // }
}
