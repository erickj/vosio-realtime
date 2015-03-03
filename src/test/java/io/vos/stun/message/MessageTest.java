package io.vos.stun.message;

import static io.vos.stun.testing.Messages.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MessageTest {

  @Test
  public void constructorValidatesMinByteArraySize() {
    try {
      new Message(new byte[0]);
      fail("Should have failed for a byte array less than the min size.");
    } catch (IllegalArgumentException expected) {}
  }

  @Test
  public void constructorIsDefensive() {
    byte[] bytes =
        hexToBytes("000102030405060708090a0b0c0d0e0f1011121314");
    Message m = new Message(bytes);
    bytes[0] = 99;
    assertEquals(0, m.getHeaderBytes()[0]);
  }

  @Test
  public void hashCodeAndEquals() {
    byte[] bytes1 = hexToBytes("0000000000000000000000000000000000000000");
    byte[] bytes2 = hexToBytes("1111111111111111111111111111111111111111");

    Message message1 = new Message(bytes1);
    Message message2 = new Message(bytes1);
    Message message3 = new Message(bytes2);

    assertEquals(message1.hashCode(), message1.hashCode());
    assertEquals(message1.hashCode(), message2.hashCode());
    assertNotEquals(message1.hashCode(), message3.hashCode());

    assertEquals(message1, message1);
    assertEquals(message1, message2);
    assertNotEquals(message1, message3);
    assertNotEquals(message1, null);
    assertNotEquals(message1, new Object());
  }

  @Test
  public void getHeaderBytes() {
    Message m = new Message(
        hexToBytes("000102030405060708090a0b0c0d0e0f1011121314151617"));
    byte[] header = m.getHeaderBytes();
    assertEquals(20, header.length);
    for (int i = 0; i < 20; i++) {
      assertEquals(i, header[i]);
    }
  }

  @Test
  public void getHeaderBytesIsDefensive() {
    byte[] bytes =
        hexToBytes("000102030405060708090a0b0c0d0e0f1011121314");
    Message m = new Message(bytes);
    byte[] header = m.getHeaderBytes();
    header[0] = 99;
    assertEquals(0, m.getHeaderBytes()[0]);
  }

  @Test
  public void getAttributeBytes() {
    Message m = new Message(
        hexToBytes("000102030405060708090a0b0c0d0e0f1011121314151617"));
    byte[] attrBytes = m.getAttributeBytes();
    assertEquals(4, attrBytes.length);
    assertEquals(0x14, attrBytes[0]);
  }

  @Test
  public void getAttributeBytesIsDefensive() {
    Message m = new Message(
        hexToBytes("000102030405060708090a0b0c0d0e0f1011121314151617"));
    byte[] attrBytes = m.getAttributeBytes();
    attrBytes[0] = 0;
    assertEquals(0x14, m.getAttributeBytes()[0]);
  }

  @Test
  public void hasNonZeroHeaderBits() throws Exception {
    Message firstTwoBitsAreOne =
        new Message(hexToBytes("c000000000000000000000000000000000000000"));
    Message firstBitIsOne =
        new Message(hexToBytes("8000000000000000000000000000000000000000"));
    Message secondBitIsOne =
        new Message(hexToBytes("4000000000000000000000000000000000000000"));

    assertFalse(firstTwoBitsAreOne.hasNonZeroHeaderBits());
    assertFalse(firstBitIsOne.hasNonZeroHeaderBits());
    assertFalse(secondBitIsOne.hasNonZeroHeaderBits());

    Message zeroBits1 =
        new Message(hexToBytes("0000000000000000000000000000000000000000"));
    Message zeroBits2 =
        new Message(hexToBytes("1000000000000000000000000000000000000000"));
    Message zeroBits3 =
        new Message(hexToBytes("2000000000000000000000000000000000000000"));

    assertTrue(zeroBits1.hasNonZeroHeaderBits());
    assertTrue(zeroBits2.hasNonZeroHeaderBits());
    assertTrue(zeroBits3.hasNonZeroHeaderBits());
  }

  @Test
  public void getMessageClass_request() {
    Message request = new Message(
        hexToBytes("000100000000000000000000000000000000000000000000"));
    assertEquals(Messages.MESSAGE_CLASS_REQUEST, request.getMessageClass());
  }

  @Test
  public void getMessageClass_indication() {
    Message indication = new Message(
        hexToBytes("001100000000000000000000000000000000000000000000"));
    assertEquals(
        Messages.MESSAGE_CLASS_INDICATION, indication.getMessageClass());
  }

  @Test
  public void getMessageClass_succesResponse() {
    Message response = new Message(
        hexToBytes("010100000000000000000000000000000000000000000000"));
    assertEquals(Messages.MESSAGE_CLASS_RESPONSE, response.getMessageClass());
  }

  @Test
  public void getMessageClass_errorResponse() {
    Message errorResponse = new Message(
        hexToBytes("011100000000000000000000000000000000000000000000"));
    assertEquals(
        Messages.MESSAGE_CLASS_ERROR_RESPONSE, errorResponse.getMessageClass());
  }

  /**
   * Tests the max method values in the RFC defined range 0x000 - 0xfff, with all message
   * class bytes set to 0.
   */
  @Test
  public void getMessageMethod_maxValue() {
    // The message header bits are 0011 1110 1110 1111
    Message max = new Message(
        hexToBytes("3eef00000000000000000000000000000000000000000000"));
    assertEquals(0xfff, max.getMessageMethod());
  }

  /**
   * Tests the min method value in the RFC defined range 0x000 - 0xfff, with all message
   * class bytes set to 1.
   */
  @Test
  public void getMessageMethod_minValue() {
    // The message header bits are 0000 0001 0001 0000
    Message min = new Message(
        hexToBytes("011000000000000000000000000000000000000000000000"));
    assertEquals(0, min.getMessageMethod());
  }

  @Test
  public void getMessageMethod_binding() {
    Message binding = new Message(
        hexToBytes("010100000000000000000000000000000000000000000000"));
    assertEquals(Messages.MESSAGE_METHOD_BINDING, binding.getMessageMethod());
  }

  @Test
  public void getMessageMethod_allocate() {
    Message allocate = new Message(
        hexToBytes("010300000000000000000000000000000000000000000000"));
    assertEquals(Messages.MESSAGE_METHOD_ALLOCATE, allocate.getMessageMethod());
  }

  @Test
  public void getMessageLength() {
    Message message = new Message(
        hexToBytes("010012bc0000000000000000000000000000000000000000"));
    assertEquals(0x12bc, message.getMessageLength());
  }

  @Test
  public void hasMagicCookie() {
    Message msgWithCookie = new Message(hexToBytes(SAMPLE_REQUEST_1));
    assertTrue(msgWithCookie.hasMagicCookie());

    Message msgWithoutCookie = new Message(
        hexToBytes("000100000000000000000000000000000000000000000000"));
    assertFalse(msgWithoutCookie.hasMagicCookie());
  }

  @Test
  public void getTransactionId() {
    byte[] expectedId = new byte[] {
      (byte)0xb7, (byte)0xe7, (byte)0xa7, (byte)0x01,
      (byte)0xbc, (byte)0x34, (byte)0xd6, (byte)0x86,
      (byte)0xfa, (byte)0x87, (byte)0xdf, (byte)0xae
    };
    Message message = new Message(hexToBytes(SAMPLE_REQUEST_1));
    assertArrayEquals(expectedId, message.getTransactionId());
  }

  @Test
  public void builderCreatesValidMessage_withAttributes() throws Exception {
    Message.Builder builder = Message.builder();
    byte[] transactionId = hexToBytes("deadbeeffeedd0d012345678");
    byte[] attributes = hexToBytes("0123fedc");

    Message message = builder
        .setMessageClass(3)
        .setMessageMethod(0xf0f)
        .setTransactionId(transactionId)
        .setAttributeBytes(attributes)
        .build();

    assertEquals(3, message.getMessageClass());
    assertEquals(0xf0f, message.getMessageMethod());
    assertTrue(message.hasMagicCookie());
    assertEquals(4, message.getMessageLength());
    assertArrayEquals(transactionId, message.getTransactionId());
    assertArrayEquals(attributes, message.getAttributeBytes());
  }

  @Test
  public void builderCreatesValidMessage_noAttributes() throws Exception {
    Message.Builder builder = Message.builder();
    byte[] transactionId = hexToBytes("deadbeeffeedd0d012345678");

    Message message = builder
        .setMessageClass(3)
        .setMessageMethod(0xf0f)
        .setTransactionId(transactionId)
        .build();

    assertEquals(3, message.getMessageClass());
    assertEquals(0xf0f, message.getMessageMethod());
    assertTrue(message.hasMagicCookie());
    assertEquals(0, message.getMessageLength());
    assertArrayEquals(transactionId, message.getTransactionId());
  }
}
