package io.vos.stun.attribute;

import static org.junit.Assert.*;

import io.vos.stun.util.Bytes;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class ErrorCodeAttributeTest {

  @Test
  public void createAttribute_valueDataLength() {
    String reasonPhrase = new String("Because I say so");
    int reasonPhraseByteCount = -1;
    try {
      reasonPhraseByteCount = reasonPhrase.getBytes("UTF-8").length;
    } catch(UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    ErrorCodeAttribute attr = ErrorCodeAttribute.createAttribute(420, reasonPhrase);

    int expectedByteCount = 4 + reasonPhraseByteCount;
    assertEquals(expectedByteCount, attr.getLength());
  }

  @Test
  public void createAttribute_reservedErrorCodeAndErrorNumberFormat() {
    // Expect the first 32 bits to look like:
    // 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 1 1 0 0 1 1
    // |-------------- reserved ---------------| | ec| |-- e number -|

    ErrorCodeAttribute attr = ErrorCodeAttribute.createAttribute(499, "An Error");
    byte[] valueData = attr.getValue();

    assertEquals("Unexpected error number", 99, Bytes.byteToInt(valueData[3]));
    assertEquals("Unexpected error code", 4, Bytes.byteToInt(valueData[2]));
    assertEquals("Unexpected reserved bits", 0, Bytes.twoBytesToInt(valueData[0], valueData[1]));
  }

  @Test
  public void createAttribute_minErrorCodeIsValidated() {
    try {
      ErrorCodeAttribute.createAttribute(299, "should fail");
      fail("Failed to validate minimum error code value");
    } catch (IllegalArgumentException expected) {}
  }

  @Test
  public void createAttribute_maxErrorCodeIsValidated() {
    try {
      ErrorCodeAttribute.createAttribute(700, "should fail");
      fail("Failed to validate maximum error code value");
    } catch (IllegalArgumentException expected) {}
  }
}
