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

public class LeftTest {

    @Test
    public void hasNoRightValue() {
        try {
            final Method rightValue = Either.class.getDeclaredMethod("rightValue");
            rightValue.setAccessible(true);
            try {
                rightValue.invoke(new Left<>(12));
            } catch (InvocationTargetException e) {
                if (e.getTargetException().getClass() != NoSuchElementException.class) {
                    Assert.fail(e.getTargetException().getMessage());
                }
            } catch (IllegalAccessException e) {
                Assert.fail(e.getMessage());
            } finally {
                rightValue.setAccessible(false);
            }
        } catch (NoSuchMethodException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("Left(12)",
                new Left<>(12).toString());
    }
}
