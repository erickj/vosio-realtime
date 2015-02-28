package io.vos.stun.protocol;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;

import java.util.Arrays;

import org.junit.Test;

public class AgentTest {

  private static final int FAKE_METHOD_1 = Integer.MAX_VALUE;
  private static final int FAKE_METHOD_2 = Integer.MIN_VALUE;

  private MethodProcessor fakeMethodProcessor;

  @Test
  public void constructorThrowsOnDuplicateMethodProcessors() {
    MethodProcessor p1 = new FakeMethodProcessor(FAKE_METHOD_1);
    MethodProcessor p2 = new FakeMethodProcessor(FAKE_METHOD_1);

    try {
      new Agent(Lists.newArrayList(p1, p2));
      fail("Should have thrown an illegal state exception");
    } catch (IllegalStateException expected) {}
  }

  private static class FakeMethodProcessor extends BaseMethodProcessor {

    private final int[] supportedClasses;

    FakeMethodProcessor(int method) {
      this(method, new int[] {});
    }

    FakeMethodProcessor(int method, int[] supportedClasses) {
      super(method);
      this.supportedClasses = supportedClasses;
    }

    @Override
    public boolean isClassSupported(int methodClass) {
      return Arrays.binarySearch(supportedClasses, methodClass) >= 0;
    }
  }


}
