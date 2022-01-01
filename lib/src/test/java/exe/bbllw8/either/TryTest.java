/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import org.junit.Assert;
import org.junit.Test;

public class TryTest {

    @Test
    public void flatten() {
        Assert.assertEquals(new Success<>(1),
                Try.flatten(new Success<>(new Success<>(1))));

        final Throwable t = new Throwable("An error");
        Assert.assertEquals(new Failure<>(t),
                Try.flatten(new Success<>(new Failure<>(t))));
        Assert.assertEquals(new Failure<>(t),
                Try.flatten(new Failure<>(t)));
    }

    @Test
    public void from() {
        Assert.assertTrue(Try.from(() -> {
            throw new CheckedException(new Throwable());
        }).isFailure());
        Assert.assertTrue(Try.from(() -> Integer.parseInt("-")).isFailure());
        Assert.assertTrue(Try.from(() -> Integer.parseInt("1")).isSuccess());
        Assert.assertTrue(Try.from(() -> 1).isSuccess());
    }
}
