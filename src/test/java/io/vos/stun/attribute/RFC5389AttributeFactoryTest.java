package io.vos.stun.attribute;

import static io.vos.stun.attribute.Attributes.*;
import static org.junit.Assert.*;

import io.vos.stun.util.Bytes;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class RFC5389AttributeFactoryTest {

  @Test
  public void createsAttribute_mappedAddress() throws UnknownHostException {
    int port = 53535;
    byte[] portBytes = Bytes.intToBytes(port, 2);
    byte[] ipv4AddressBytes = InetAddress.getByName("192.168.0.1").getAddress();
    assertEquals(4, ipv4AddressBytes.length);

    Attribute expected = MappedAddressAttribute.createAttribute(
        MappedAddressAttribute.AF_IPV4, portBytes, ipv4AddressBytes,
        false /* isXorMapped */);

    AttributeFactory factory = new RFC5389AttributeFactory();
    Attribute createdAttribute =
        factory.createAttribute(expected.getType(), expected.getLength(), expected.getValue());
    assertEquals(expected, createdAttribute);
  }

  @Test
  public void createsAttribute_xorMappedAddress() throws UnknownHostException {
    int port = 53535;
    byte[] portBytes = Bytes.intToBytes(port, 2);
    byte[] ipv4AddressBytes = InetAddress.getByName("192.168.0.1").getAddress();
    assertEquals(4, ipv4AddressBytes.length);

    Attribute expected = MappedAddressAttribute.createAttribute(
        MappedAddressAttribute.AF_IPV4, portBytes, ipv4AddressBytes,
        true /* isXorMapped */);

    AttributeFactory factory = new RFC5389AttributeFactory();
    Attribute createdAttribute =
        factory.createAttribute(expected.getType(), expected.getLength(), expected.getValue());
    assertEquals(expected, createdAttribute);
  }

  @Test
  public void createAttribute_errorCode() {
    Attribute expected = ErrorCodeAttribute.createAttribute(400, "factory test");

    AttributeFactory factory = new RFC5389AttributeFactory();
    Attribute createdAttribute =
        factory.createAttribute(expected.getType(), expected.getLength(), expected.getValue());
    assertEquals(expected, createdAttribute);
  }
}
