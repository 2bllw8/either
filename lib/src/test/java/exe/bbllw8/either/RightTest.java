/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Test;

public class RightTest {

    @Test
    public void hasNoRightValue() {
        try {
            final Method leftValue = Either.class.getDeclaredMethod("leftValue");
            leftValue.setAccessible(true);
            try {
                leftValue.invoke(new Right<>(12));
            } catch (InvocationTargetException e) {
                if (e.getTargetException().getClass() != NoSuchElementException.class) {
                    Assert.fail(e.getTargetException().getMessage());
                }
            } catch (IllegalAccessException e) {
                Assert.fail(e.getMessage());
            } finally {
                leftValue.setAccessible(false);
            }
        } catch (NoSuchMethodException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("Right(12)",
                new Right<>(12).toString());
    }
}
