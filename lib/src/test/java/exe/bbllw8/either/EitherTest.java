/*
 * Copyright (c) 2021-2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class EitherTest {

    @Test
    public void fromTry() {
        Assert.assertTrue("A supplier that throws an exception should return a Left",
                Either.fromTry(() -> {
                    throw new IllegalStateException();
                }, IllegalStateException.class).isLeft());
    }

    @Test
    public void tryCatch() {
        Assert.assertTrue("Supplier that throws an expected exception should return a Left",
                Either.tryCatch(() -> Integer.parseInt("pancake"),
                        NumberFormatException.class).isLeft());
        Assert.assertEquals(
                "Supplier that throws an expected exception should the result of the function",
                Either.tryCatch(() -> Integer.parseInt("pancake"), t -> 0),
                new Left<>(0));

        Assert.assertTrue("Supplier that does not throw an exception should return a Right",
                Either.tryCatch(() -> Integer.parseInt("12"),
                        NumberFormatException.class).isRight());
        Assert.assertEquals("Supplier that does not throw an exception should return its result",
                Either.tryCatch(() -> Integer.parseInt("12"), t -> 0),
                new Right<>(12));
    }

    @Test
    public void tryCatchUnchecked() {
        Assert.assertEquals(
                "Supplier that throws should return a Left containing the throwable class",
                Either.tryCatch(() -> {
                    try {
                        return Integer.parseInt("one");
                    } catch (Exception e) {
                        throw new CheckedException(e);
                    }
                }, Throwable::getClass),
                new Left<>(NumberFormatException.class));

        Assert.assertEquals(
                "Supplier that does not throw should return a Right with the proper value",
                Either.tryCatch(() -> {
                    try {
                        return Integer.parseInt("33");
                    } catch (Exception e) {
                        throw new CheckedException(e);
                    }
                }, Throwable::getClass),
                new Right<>(33));
    }
}
