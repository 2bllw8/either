/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class EitherTest {

    @Test
    public void fromTry() {
        Assert.assertTrue(Either.fromTry(() -> {
            throw new IllegalStateException();
        }, IllegalStateException.class).isLeft());
    }

    @Test
    public void tryCatch() {
        Assert.assertTrue(Either.tryCatch(() -> Integer.parseInt("pancake"), NumberFormatException.class).isLeft());
        Assert.assertEquals(Either.tryCatch(() -> Integer.parseInt("pancake"), t -> 0), new Left<>(0));

        Assert.assertTrue(Either.tryCatch(() -> Integer.parseInt("12"), NumberFormatException.class).isRight());
        Assert.assertEquals(Either.tryCatch(() -> Integer.parseInt("12"), t -> 0), new Right<>(12));
    }

    @Test
    public void tryCatchUnchecked() {
        Assert.assertEquals(Either.tryCatch(() -> {
            try {
                return Integer.parseInt("one");
            } catch (Exception e) {
                throw new CheckedException(e);
            }
        }, Throwable::getClass), new Left<>(NumberFormatException.class));

        Assert.assertEquals(Either.tryCatch(() -> {
            try {
                return Integer.parseInt("33");
            } catch (Exception e) {
                throw new CheckedException(e);
            }
        }, Throwable::getClass), new Right<>(33));
    }
}
