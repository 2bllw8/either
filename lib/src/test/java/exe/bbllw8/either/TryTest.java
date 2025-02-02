/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import org.junit.Assert;
import org.junit.Test;

public class TryTest {

    @Test
    public void flattenInnerSuccess() {
        Assert.assertEquals("The inner value should be returned",
                new Success<>(1),
                Try.flatten(new Success<>(new Success<>(1))));
    }

    @Test
    public void flattenInnerFailure() {
        final Throwable t = new Throwable("An error");
        Assert.assertEquals("The inner value should be returned",
                new Failure<>(t),
                Try.flatten(new Success<>(new Failure<>(t))));
    }

    @Test
    public void flattenOuter() {
        final Throwable t = new Throwable("Oops!");
        Assert.assertEquals("The outer value should be returned",
                new Failure<>(t),
                Try.flatten(new Failure<>(t)));
    }

    @Test
    public void fromThrowingSupplier() {
        Assert.assertTrue("A supplier throwing an exception should return a failure",
                Try.from(() -> {
                    throw new Throwable();
                }).isFailure());
    }

    @Test
    public void fromThrowingSupplierUnchecked() {
        //noinspection DataFlowIssue
        Assert.assertTrue("A supplier throwing an exception should return a failure",
                Try.from(() -> Integer.parseInt("-")).isFailure());
    }

    @Test
    public void fromSuccessfulSupplier() {
        Assert.assertTrue("A supplier that does not throw an exception should return a success",
                Try.from(() -> Integer.parseInt("1")).isSuccess());
    }
}
