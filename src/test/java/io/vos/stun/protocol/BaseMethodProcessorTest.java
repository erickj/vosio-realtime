package io.vos.stun.protocol;

import static io.vos.stun.attribute.Attributes.*;
import static io.vos.stun.message.Messages.*;
import static io.vos.stun.testing.Messages.*;
import static org.junit.Assert.*;

import io.vos.stun.attribute.Attribute;
import io.vos.stun.attribute.RFC5389AttributeFactory;
import io.vos.stun.message.Message;
import io.vos.stun.testing.FakeMethodProcessor;

import com.google.common.collect.Lists;

import java.util.List;

import org.junit.Test;

public class BaseMethodProcessorTest {

  @Test
  public void constructorThrowsForEmptySupportedClassesList() {
    try {
      new BaseMethodProcessor(1);
      fail("Should have validated against empty supported classes");
    } catch (IllegalArgumentException expected) {}

    try {
      new BaseMethodProcessor(1, new int[0]);
      fail("Should have validated against empty supported classes");
    } catch (IllegalArgumentException expected) {}
  }

  @Test
  public void getMethod() {
    assertEquals(100, new BaseMethodProcessor(100, 1).getMethod());
  }

  @Test
  public void isClassSupported() {
    MethodProcessor m = new BaseMethodProcessor(100, 99);
    assertTrue(m.isClassSupported(99));
    assertFalse(m.isClassSupported(1));
  }

  @Test
  public void processRequest_throwsProtocolException_onUnknownAttributes() {
    Message message = new Message(hexToBytes(SAMPLE_REQUEST_1));
    List<Attribute> unsupportedAttrList = Lists.newArrayList(new RFC5389AttributeFactory()
        .createAttribute(ATTRIBUTE_RESERVED, 0, new byte[] {}, message));
    MethodProcessor methodProcessor = new BaseMethodProcessor(100, MESSAGE_CLASS_REQUEST);
    try {
      methodProcessor.processRequest(message, unsupportedAttrList);
      fail("Failed to catch expected ProtocolException");
    } catch (ProtocolException expected) {
      assertEquals(ProtocolException.ReasonCode.UNKNOWN_ATTRIBUTE, expected.getReasonCode());
    }
  }

  @Test
  public void processRequest_onValidAttrs_passesResponsibilityToSubclass() throws Exception {
    Message message = new Message(hexToBytes(SAMPLE_REQUEST_1));
    FakeMethodProcessor methodProcessor = new FakeMethodProcessor(99, MESSAGE_CLASS_REQUEST);

    methodProcessor.processRequest(message, Lists.<Attribute>newArrayList());
    assertEquals(message, methodProcessor.getProcessedRequest());
  }
}
