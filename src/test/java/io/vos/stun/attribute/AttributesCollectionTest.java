package io.vos.stun.attribute;

import static org.junit.Assert.*;

import io.vos.stun.attribute.Attribute;
import io.vos.stun.testing.FakeAttribute;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AttributesCollectionTest {

  private static Attribute TYPE_0_ATTRIBUTE_1 =
      new FakeAttribute(0, 6, new byte[] {
          0x00, 0x00, 0x00, 0x06,
          0x00, 0x04, 0x02, 0x03,
          0x04, 0x05, 0x00, 0x00
        });
  private static Attribute TYPE_0_ATTRIBUTE_2 =
      new FakeAttribute(0, 4, new byte[] {
          0x00, 0x00, 0x00, 0x04,
          0x00, 0x04, 0x02, 0x03,
        });
  private static Attribute TYPE_0_ATTRIBUTE_3 =
      new FakeAttribute(0, 2, new byte[] {
          0x00, 0x00, 0x00, 0x02,
          0x04, 0x05, 0x00, 0x00
        });
  private static Attribute TYPE_1_ATTRIBUTE_1 =
      new FakeAttribute(1, 0, new byte[] {
          0x00, 0x00, 0x00, 0x00
        });

  @Test
  public void isEmpty_trueFroEmptyCollection() throws Exception {
    AttributesCollection collection = AttributesCollection.builder().build();
    assertTrue(collection.isEmpty());
  }

  @Test
  public void isEmpty_falseForNonEmptyCollection() throws Exception {
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .build();
    assertFalse(collection.isEmpty());
  }

  @Test
  public void size() {
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .addAttribute(TYPE_0_ATTRIBUTE_2)
        .addAttribute(TYPE_0_ATTRIBUTE_3)
        .addAttribute(TYPE_1_ATTRIBUTE_1)
        .build();
    assertEquals(4, collection.size());
  }

  @Test
  public void iteration() {
    List<Attribute> iteratedAttributes = new ArrayList<>();
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .addAttribute(TYPE_0_ATTRIBUTE_2)
        .addAttribute(TYPE_0_ATTRIBUTE_3)
        .addAttribute(TYPE_1_ATTRIBUTE_1)
        .build();
    for (Attribute attr : collection) {
      iteratedAttributes.add(attr);
    }
    assertEquals(4, iteratedAttributes.size());
    assertEquals(TYPE_0_ATTRIBUTE_1, iteratedAttributes.get(0));
    assertEquals(TYPE_0_ATTRIBUTE_2, iteratedAttributes.get(1));
    assertEquals(TYPE_0_ATTRIBUTE_3, iteratedAttributes.get(2));
    assertEquals(TYPE_1_ATTRIBUTE_1, iteratedAttributes.get(3));
  }

  @Test
  public void hasAttributeType_falseWhenDontHaveType() {
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .addAttribute(TYPE_0_ATTRIBUTE_2)
        .addAttribute(TYPE_0_ATTRIBUTE_3)
        .addAttribute(TYPE_1_ATTRIBUTE_1)
        .build();
    assertFalse(collection.hasAttributeType(300));
  }

  @Test
  public void hasAttributeType_trueWhenDoHaveType() {
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .addAttribute(TYPE_0_ATTRIBUTE_2)
        .addAttribute(TYPE_0_ATTRIBUTE_3)
        .addAttribute(TYPE_1_ATTRIBUTE_1)
        .build();
    assertTrue(collection.hasAttributeType(0));
    assertTrue(collection.hasAttributeType(1));
  }

  @Test
  public void getAttributesOfType_whenHaveType() {
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .addAttribute(TYPE_0_ATTRIBUTE_2)
        .addAttribute(TYPE_0_ATTRIBUTE_3)
        .addAttribute(TYPE_1_ATTRIBUTE_1)
        .build();

    Iterable<Attribute> type0Attrs = collection.getAttributesOfType(0);
    assertEquals(3, Iterables.size(type0Attrs));

    List<Attribute> listType0Attrs = Lists.newArrayList(type0Attrs);
    assertEquals(TYPE_0_ATTRIBUTE_1, listType0Attrs.get(0));
    assertEquals(TYPE_0_ATTRIBUTE_2, listType0Attrs.get(1));
    assertEquals(TYPE_0_ATTRIBUTE_3, listType0Attrs.get(2));

    Iterable<Attribute> type1Attrs = collection.getAttributesOfType(1);
    assertEquals(1, Iterables.size(type1Attrs));

    List<Attribute> listType1Attrs = Lists.newArrayList(type1Attrs);
    assertEquals(TYPE_1_ATTRIBUTE_1, listType1Attrs.get(0));
  }

  @Test
  public void getAttributesOfType_whenDontHaveType() {
    AttributesCollection collection = AttributesCollection.builder()
        .build();

    Iterable<Attribute> noAttrs = collection.getAttributesOfType(300);
    assertTrue(Iterables.isEmpty(noAttrs));
  }

  @Test
  public void getFirstAttributeOfType_whenHaveType() {
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .addAttribute(TYPE_0_ATTRIBUTE_2)
        .addAttribute(TYPE_0_ATTRIBUTE_3)
        .addAttribute(TYPE_1_ATTRIBUTE_1)
        .build();

    Attribute type0Attr1 = collection.getFirstAttributeOfType(0);
    assertEquals(TYPE_0_ATTRIBUTE_1, type0Attr1);

    Attribute type1Attr1 = collection.getFirstAttributeOfType(1);
    assertEquals(TYPE_1_ATTRIBUTE_1, type1Attr1);
  }

  @Test
  public void getFirstAttributeOfType_whenDontHaveType() {
    AttributesCollection collection = AttributesCollection.builder()
        .build();

    assertNull(collection.getFirstAttributeOfType(300));
  }

  @Test
  public void toByteArray() {
    Attribute attr1a = new FakeAttribute(0, 4, new byte[] { 0x00, 0x04, 0x02, 0x03 });
    Attribute attr1b = new FakeAttribute(0, 2, new byte[] { 0x0f, 0x0f, 0x00, 0x00 });
    Attribute attr2 = new FakeAttribute(1, 2, new byte[] { 0x10, 0x14, 0x00, 0x00 });
    Attribute attr3 = new FakeAttribute(1, 3, new byte[] { 0x20, 0x14, 0x00, 0x00 });

    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(attr1a)
        .addAttribute(attr1b)
        .addAttribute(attr2)
        .addAttribute(attr3)
        .build();

    byte[] expectedData = Bytes.concat(
        attr1a.toByteArray(),
        attr1b.toByteArray(),
        attr2.toByteArray(),
        attr3.toByteArray());
    assertArrayEquals(expectedData, collection.toByteArray());
  }

  @Test
  public void replyBuilder_addAttribute() {
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .addAttribute(TYPE_0_ATTRIBUTE_2)
        .addAttribute(TYPE_0_ATTRIBUTE_3)
        .build();
    assertNull(collection.getFirstAttributeOfType(1));

    AttributesCollection replyCollection = collection.replyBuilder()
        .addAttribute(TYPE_1_ATTRIBUTE_1)
        .build();
    assertNotNull(replyCollection.getFirstAttributeOfType(1));
  }

  @Test
  public void replyBuilder_addAllAttribute() {
    List<Attribute> attrs = new ArrayList<>();
    attrs.add(TYPE_0_ATTRIBUTE_1);
    attrs.add(TYPE_0_ATTRIBUTE_2);
    attrs.add(TYPE_0_ATTRIBUTE_3);

    AttributesCollection collection = AttributesCollection.builder()
        .addAllAttributes(attrs)
        .build();
    assertEquals(3, Iterables.size(collection.getAttributesOfType(0)));
  }

  @Test
  public void replyBuilder_removeAllAttributesByType() {
    AttributesCollection collection = AttributesCollection.builder()
        .addAttribute(TYPE_0_ATTRIBUTE_1)
        .addAttribute(TYPE_0_ATTRIBUTE_2)
        .addAttribute(TYPE_0_ATTRIBUTE_3)
        .build();
    assertEquals(3, Iterables.size(collection.getAttributesOfType(0)));

    AttributesCollection replyCollection = collection.replyBuilder()
        .removeAllAttributesByType(0)
        .build();
    assertTrue(replyCollection.isEmpty());
  }
}
