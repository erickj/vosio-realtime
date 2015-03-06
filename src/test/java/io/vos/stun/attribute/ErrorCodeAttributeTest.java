package io.vos.stun.attribute;

import org.junit.Test;

public class ErrorCodeAttributeTest {

  @Test
  public void createAttribute() {
    ErrorCodeAttribute attr = ErrorCodeAttribute.createAttribute(420, "Because I say so");
  }
}
