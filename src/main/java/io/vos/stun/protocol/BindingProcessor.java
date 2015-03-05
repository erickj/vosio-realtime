package io.vos.stun.protocol;

import static io.vos.stun.attribute.Attributes.*;
import static io.vos.stun.message.Messages.*;

import io.vos.stun.attribute.Attribute;
import io.vos.stun.attribute.AttributesCollection;
import io.vos.stun.attribute.MappedAddressAttribute;
import io.vos.stun.message.Message;
import io.vos.stun.util.Bytes;

import com.google.common.base.Preconditions;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;

final class BindingProcessor extends BaseMethodProcessor {

  BindingProcessor() {
    super(MESSAGE_METHOD_BINDING, MESSAGE_CLASS_REQUEST);
  }

  /**
   * Creates a new binding response for either 3489 or 5389 binding requests.
   */
  @Override
  protected byte[] processRequestInternal(RequestContext requestContext) {
    AttributesCollection attributes = requestContext.getAttributesCollection();

    Attribute mappedAddress = requestContext.getMessage().isRFC5389Message()
        ? getXorMappedAddress(requestContext)
        : getMappedAddress(requestContext);
    return attributes.replyBuilder()
        .removeAllAttributesByType(ATTRIBUTE_XOR_MAPPED_ADDRESS)
        .addAttribute(mappedAddress)
        .build()
        .toByteArray();
  }

  private Attribute getMappedAddress(RequestContext requestContext) {
    InetSocketAddress replyAddress = requestContext.getReplyAddress();
    byte[] portBytes = Bytes.intToBytes(replyAddress.getPort());
    byte[] addressBytes = replyAddress.getAddress().getAddress();
    return MappedAddressAttribute.createAttribute(portBytes, addressBytes, false /* isXor */);
  }

  private Attribute getXorMappedAddress(RequestContext requestContext) {
    InetSocketAddress replyAddress = requestContext.getReplyAddress();
    byte[] magicCookieBytes = Bytes.intToBytes(MAGIC_COOKIE_FIXED_VALUE);
    byte[] portBytes = Bytes.intToBytes(replyAddress.getPort());
    byte[] xPortBytes = new byte[] {
      (byte)(portBytes[2] ^ magicCookieBytes[0]),
      (byte)(portBytes[3] ^ magicCookieBytes[1])
    };

    byte attributeAddressFamily;
    byte[] addressBytes = replyAddress.getAddress().getAddress();
    byte[] xAddressBytes = new byte[addressBytes.length];
    byte[] xorBytes;
    if (replyAddress.getAddress() instanceof Inet4Address) {
      xorBytes = magicCookieBytes;
      attributeAddressFamily = MappedAddressAttribute.AF_IPV4;
    } else if (replyAddress.getAddress() instanceof Inet6Address) {
      xorBytes = com.google.common.primitives.Bytes.concat(
          magicCookieBytes, requestContext.getMessage().getTransactionId());
      attributeAddressFamily = MappedAddressAttribute.AF_IPV6;
    } else {
      throw new AssertionError("Should either have an IPv4 or IPv6 address");
    }

    for (int i = 0; i < addressBytes.length; i++) {
      xAddressBytes[i] = (byte)(addressBytes[i] ^ xorBytes[i]);
    }

    return MappedAddressAttribute.createAttribute(xPortBytes, xAddressBytes, true /* isXor */);
  }
}
