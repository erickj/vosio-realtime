package io.vos.stun.message;

import com.google.common.base.Preconditions;

/**
 * @see https://tools.ietf.org/html/rfc5389#section-6
 * 6. STUN Message Structure
 *
 * STUN messages are encoded in binary using network-oriented format
 * (most significant byte or octet first, also commonly known as big-
 * endian).  The transmission order is described in detail in Appendix B
 * of RFC 791 [RFC0791].  Unless otherwise noted, numeric constants are
 * in decimal (base 10).
 *
 * All STUN messages MUST start with a 20-byte header followed by zero
 * or more Attributes.  The STUN header contains a STUN message type,
 * magic cookie, transaction ID, and message length.
 *
 *     0                   1                   2                   3
 *     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |0 0|     STUN Message Type     |         Message Length        |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                         Magic Cookie                          |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                                                               |
 *    |                     Transaction ID (96 bits)                  |
 *    |                                                               |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 *                 Figure 2: Format of STUN Message Header
 *
 */
public final class Message {

  private final byte[] data;

  public Message(byte[] data) {
    Preconditions.checkArgument(data.length >= Messages.MESSAGE_HEADER_LEN);
    this.data = new byte[data.length];
    System.arraycopy(data, 0, this.data, 0, data.length);
  }

  public byte[] getHeaderBytes() {
    byte[] headerBytes = new byte[Messages.MESSAGE_HEADER_LEN];
    System.arraycopy(data, 0, headerBytes, 0, headerBytes.length);
    return headerBytes;
  }

  public byte[] getAttributeBytes() {
    byte[] attrBytes = new byte[this.data.length - Messages.MESSAGE_HEADER_LEN];
    System.arraycopy(
        data, Messages.MESSAGE_HEADER_LEN, attrBytes, 0, attrBytes.length);
    return attrBytes;
  }

  // public int getMessageTypeClass() {
  //   byte c0 = data[0] >> 4 & 0x1;
  //   byte c1 = data[1] & 0x1;
  //   return c1 << 1 | c0;
  // }

  // public int getMessageTypeMethod() {
  //   return data[1];
  // }
}
