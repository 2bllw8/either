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
        Assert.assertEquals("The inner value should be returned",
                new Success<>(1),
                Try.flatten(new Success<>(new Success<>(1))));

        final Throwable t = new Throwable("An error");
        Assert.assertEquals("The inner value should be returned",
                new Failure<>(t),
                Try.flatten(new Success<>(new Failure<>(t))));
        Assert.assertEquals("The outer value should be returned",
                new Failure<>(t),
                Try.flatten(new Failure<>(t)));
    }

    @Test
    public void from() {
        Assert.assertTrue("A supplier throwing an exception should return a failure",
                Try.from(() -> {
                    throw new CheckedException(new Throwable());
                }).isFailure());
        Assert.assertTrue("A supplier throwing an exception should return a failure",
                Try.from(() -> Integer.parseInt("-")).isFailure());
        Assert.assertTrue("A supplier that does not throw an exception should return a success",
                Try.from(() -> Integer.parseInt("1")).isSuccess());
        Assert.assertTrue("A supplier that does not throw an exception should return a success",
                Try.from(() -> 1).isSuccess());
    }
}
