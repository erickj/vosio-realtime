package io.vos.stun.attribute;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Objects;

public class BaseAttribute implements Attribute {

  private final int type;
  private final int length;
  private final byte[] data;

  public BaseAttribute(int type, int length, byte[] data) {
    Preconditions.checkArgument(type >= 0 && type <= 0xFFFF);
    Preconditions.checkNotNull(data);
    Preconditions.checkArgument(data.length % 4 == 0);

    this.type = type;
    this.length = length;
    this.data = new byte[length];
    System.arraycopy(data, 0, this.data, 0, length);
  }

  @Override
  public final int getType() {
    return type;
  }

  @Override
  public final int getLength() {
    return length;
  }

  @Override
  public final byte[] getValue() {
    byte[] valueData = new byte[length];
    System.arraycopy(data, 0, valueData, 0, length);
    return valueData;
  }

  @Override
  public final boolean isComprehensionRequired() {
    return Attributes.isComprehensionRequired(type);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(type, length, Arrays.hashCode(data));
  }

  @Override
  public final boolean equals(Object other) {
    if (other == null || !(other instanceof Attribute)) {
      return false;
    } else if (this == other) {
      return true;
    }
    Attribute attrOther = (Attribute) other;
    return type == attrOther.getType()
        && length == attrOther.getLength()
        && Arrays.equals(data, attrOther.getValue());
  }
}
