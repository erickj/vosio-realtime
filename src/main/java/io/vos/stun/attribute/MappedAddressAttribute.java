package io.vos.stun.attribute;

import static io.vos.stun.attribute.Attributes.*;

import io.vos.stun.message.Message;
import io.vos.stun.util.Bytes;

import com.google.common.base.Preconditions;

/**
 * @see https://www.iana.org/assignments/stun-parameters/stun-parameters.txt
 *
 * The MAPPED-ADDRESS attribute indicates a reflexive transport address
 * of the client.  It consists of an 8-bit address family and a 16-bit
 * port, followed by a fixed-length value representing the IP address.
 * If the address family is IPv4, the address MUST be 32 bits.  If the
 * address family is IPv6, the address MUST be 128 bits.  All fields
 * must be in network byte order.
 *
 *     0                   1                   2                   3
 *     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |0 0 0 0 0 0 0 0|    Family     |           Port                |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                                                               |
 *    |                 Address (32 bits or 128 bits)                 |
 *    |                                                               |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 *             Figure 5: Format of MAPPED-ADDRESS Attribute
 */
public final class MappedAddressAttribute extends BaseAttribute {

  /** MAPPED_ADDRESS Address Family */
  public static final byte AF_IPV4 = 0x01;
  public static final byte AF_IPV6 = 0x02;

  private final byte addressFamily;
  private final int port;
  private final byte[] mappedAddress;

  MappedAddressAttribute(int type, int length, byte[] valueData) {
    super(type, length, valueData);

    addressFamily = valueData[1];
    port = Bytes.twoBytesToInt(valueData[2], valueData[3]);

    validateMappedAddressForAddressFamily();
    mappedAddress = new byte[mappedAddressByteLength()];
  }

  private void validateMappedAddressForAddressFamily() {
    int actualAddressByteLength = getLength() - 4; // subtract 4 bytes for the family/port
    Preconditions.checkState(mappedAddressByteLength() == actualAddressByteLength);
  }

  private int mappedAddressByteLength() {
    return isIPv4() ? 4 : 16;
  }

  public boolean isXorMapped() {
    return getType() == ATTRIBUTE_XOR_MAPPED_ADDRESS;
  }

  /**
   * Whether this MappedAddress is an IPv4 address. False indicates IPv6.
   */
  public  boolean isIPv4() {
    return addressFamily == AF_IPV4;
  }

  private static byte[] PADDING = new byte[] {
    0, 0, 0, 0,
    0, 0, 0, 0
  };

  public static MappedAddressAttribute createAttribute(
      byte[] port, byte[] address, boolean isXorMapped) {
    int addressFamily = address.length == 4 ? AF_IPV4 : AF_IPV6;
    byte[] valueData = com.google.common.primitives.Bytes.concat(
          PADDING,
          new byte[] { Bytes.intToBytes(addressFamily)[0] },
          port,
          address);
    return new MappedAddressAttribute(
        isXorMapped ? ATTRIBUTE_XOR_MAPPED_ADDRESS : ATTRIBUTE_MAPPED_ADDRESS,
        valueData.length,
        Bytes.padTo4ByteBoundary(valueData));
  }
}
