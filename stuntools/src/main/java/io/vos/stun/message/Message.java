package io.vos.stun.message;

import io.vos.stun.util.Bytes;

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

  /**
   * Gets the message class from the header message type, from RFC 5389:
   * @see https://tools.ietf.org/html/rfc5389#section-6
   *
   * The message type field is decomposed further into the following
   * structure:
   *                      0                 1
   *                      2  3  4 5 6 7 8 9 0 1 2 3 4 5
   *                     +--+--+-+-+-+-+-+-+-+-+-+-+-+-+
   *                     |M |M |M|M|M|C|M|M|M|C|M|M|M|M|
   *                     |11|10|9|8|7|1|6|5|4|0|3|2|1|0|
   *                     +--+--+-+-+-+-+-+-+-+-+-+-+-+-+
   *              Figure 3: Format of STUN Message Type Field
   * Here the bits in the message type field are shown as most significant
   * (M11) through least significant (M0).  M11 through M0 represent a 12-
   * bit encoding of the method.  C1 and C0 represent a 2-bit encoding of
   * the class.  A class of 0b00 is a request, a class of 0b01 is an
   * indication, a class of 0b10 is a success response, and a class of
   * 0b11 is an error response.
   */
  public int getMessageClass() {
    // Who in the holy hell thought this was a good idea for a protocol? The
    // `class` is a 2 bit value constructed from the lowest bit of the highest
    // order byte and the 5th lowest bit of the 2nd highester order byte.
    return (((int)data[0] & 0x01) << 1) | ((int)data[1] & 0x10) >> 4;
  }

  /**
   * Gets the message method from the header message type. See notes in
   * {@code #getMessageClass}.
   */
  public int getMessageMethod() {
    // Like its shitty cousin, the `method` is the 12 bit value (from lowest to
    // highest bit) constructed from the M0-M3 bits, M4-M6 bits, M7-M11 bits.
    return (((int)data[0] & 0x3e00) >> 2) | // M7-M11
        (((int)data[1] & 0xe0) >> 1) | // M4-M6
        (((int)data[1] & 0x0f)); // M0-M3
  }

  /**
   * @see https://tools.ietf.org/html/rfc5389#section-6
   *
   * The message length MUST contain the size, in bytes, of the message
   * not including the 20-byte STUN header.  Since all STUN attributes are
   * padded to a multiple of 4 bytes, the last 2 bits of this field are
   * always zero.
   */
  public int getMessageLength() {
    return Bytes.twoBytesToInt(data[2], data[3]);
  }

  /**
   * @see https://tools.ietf.org/html/rfc5389#section-6
   *
   * The magic cookie field MUST contain the fixed value 0x2112A442 in
   * network byte order.  In RFC 3489 [RFC3489], this field was part of
   * the transaction ID; placing the magic cookie in this location allows
   * a server to detect if the client will understand certain attributes
   * that were added in this revised specification.  In addition, it aids
   * in distinguishing STUN packets from packets of other protocols when
   * STUN is multiplexed with those other protocols on the same port.
   */
  public boolean hasMagicCookie() {
    return Bytes.fourBytesToInt(data[4], data[5], data[6], data[7]) ==
        Messages.MAGIC_COOKIE_FIXED_VALUE;
  }

  /**
   * @see https://tools.ietf.org/html/rfc5389#section-6
   *
   * The transaction ID is a 96-bit identifier, used to uniquely identify
   * STUN transactions.  For request/response transactions, the
   * transaction ID is chosen by the STUN client for the request and
   * echoed by the server in the response.  For indications, it is chosen
   * by the agent sending the indication.  It primarily serves to
   * correlate requests with responses, though it also plays a small role
   * in helping to prevent certain types of attacks.  The server also uses
   * the transaction ID as a key to identify each transaction uniquely
   * across all clients.  As such, the transaction ID MUST be uniformly
   * and randomly chosen from the interval 0 .. 2**96-1, and SHOULD be
   * cryptographically random.
   */
  public byte[] getTransactionId() {
    byte[] id = new byte[12];
    System.arraycopy(data, 8, id, 0, 12);
    return id;
  }

}
