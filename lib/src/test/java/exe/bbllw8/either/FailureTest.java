/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

public class FailureTest {

    @Test
    public void isFailure() {
        Assert.assertTrue("Should be a failure",
                new Failure<>(new Throwable("pancake")).isFailure());
        Assert.assertTrue("A supplier that throws an exception should be a failure",
                Try.from(() -> {
                    throw new IllegalStateException();
                }).isFailure());
    }

    @Test
    public void isSuccess() {
        Assert.assertFalse("Should not be a success",
                new Failure<>(new Throwable("cookie")).isSuccess());
        Assert.assertFalse("A supplier that throws an exception should not be a success",
                Try.from(() -> {
                    throw new IllegalStateException();
                }).isSuccess());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void get() {
        Try.from(() -> {
            throw new NullPointerException();
        }).get();
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder("Hello ");
        new Failure<>(new Throwable()).forEach(sb::append);
        Assert.assertEquals("Nothing should happen",
                "Hello ",
                sb.toString());
    }

    @Test
    public void biForEach() {
        final StringBuilder sb = new StringBuilder();
        new Failure<Integer>(new Throwable("dandelion")).forEach(
                sb::append,
                t -> sb.append(t.getMessage()));
        Assert.assertEquals("Should execute the right function",
                "dandelion",
                sb.toString());
    }

    @Test
    public void flatMap() {
        final NullPointerException npe = new NullPointerException();

        Assert.assertEquals("Should return a copy of itself from a flatMap invocation",
                new Failure<>(npe), Try.from(() -> {
                    throw npe;
                }).flatMap(value -> Try.from(value::toString)));
    }

    @Test
    public void map() {
        final NullPointerException npe = new NullPointerException();

        Assert.assertEquals("Should return a copy of itself from a map invocation",
                new Failure<>(npe), Try.from(() -> {
                    throw npe;
                }).map(Object::hashCode));
    }

    @Test
    public void filter() {
        Assert.assertTrue("Should remain a failure regardless of the filter",
                new Failure<Integer>(new Throwable()).filter(i -> i % 2 == 0).isFailure());
    }

    @Test
    public void recoverWith() {
        Assert.assertEquals("Should be recovered with the result of the given function",
                new Success<>(1),
                new Failure<>(new Throwable("1"))
                        .recoverWith(t -> Try.from(() -> Integer.parseInt(t.getMessage()))));
    }

    @Test
    public void recover() {
        Assert.assertEquals("Should be recovered with the result of the given function",
                new Success<>(1),
                new Failure<>(new Throwable("1")).recover(t -> Integer.parseInt(t.getMessage())));
    }

    @Test
    public void toOptional() {
        Assert.assertEquals("Should be mapped to an empty Optional",
                Optional.empty(),
                new Failure<>(new Throwable()).tOptional());
    }

    @Test
    public void toEither() {
        final IllegalAccessException iae = new IllegalAccessException();

        Assert.assertEquals("Should be mapped to a Left containing the throwable",
                new Left<>(iae),
                new Failure<>(iae).toEither());
    }

    @Test
    public void failed() {
        Assert.assertTrue("Should return a Success when applying the failed method",
                new Failure<Integer>(new Throwable()).failed().isSuccess());
    }

    @Test
    public void transform() {
        Assert.assertEquals("The correct transformation should be applied",
                new Success<>(2),
                new Failure<>(new Throwable()).transform(v -> new Success<>(1),
                        t -> new Success<>(2)));
    }

    @Test
    public void fold() {
        Assert.assertEquals("The correct folding function should be applied",
                "Operation failed with error: Not enough pancakes",
                Try.from(() -> {
                    throw new IllegalStateException("Not enough pancakes");
                }).fold(t -> "Operation failed with error: " + t.getMessage(),
                        value -> "Operation completed with result: " + value));
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("The fallback value should be returned",
                "something",
                new Failure<>(new Throwable()).getOrElse("something"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals("The fallback result should be returned",
                new Success<>("pancake"),
                Try.from(() -> {
                    throw new ArrayIndexOutOfBoundsException();
                }).orElse(Try.from(() -> "pancake")));
    }

    @Test
    public void stream() {
        Assert.assertEquals("Stream is empty",
                new Failure<>(new Throwable()).stream().count(),
                0);
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void consistentEquality() {
        final NumberFormatException nfe = new NumberFormatException();

        Assert.assertEquals("Equal values should be equal",
                new Failure<>(nfe),
                new Failure<>(nfe));
        Assert.assertEquals("Equal values should have the same hashCode",
                new Failure<>(nfe).hashCode(),
                new Failure<>(nfe).hashCode());

        Assert.assertNotEquals("Should not be equal of a Success with the same value",
                new Failure<>(nfe),
                new Success<>(nfe));
        Assert.assertNotEquals(
                "Should not have the same hashCode of a Success with the same value",
                new Failure<>(nfe).hashCode(),
                new Success<>(nfe).hashCode());

        Assert.assertNotEquals("Should not be equal to its value",
                nfe,
                new Failure<>(nfe));
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("The string representation should match the documentation",
                "Failure(java.lang.IllegalStateException: something)",
                Try.from(() -> {
                    throw new IllegalStateException("something");
                }).toString());
    }

    @Test
    public void multipleThrow() {
        Assert.assertTrue("Multiple thrown classes are handled (throwable #1)",
                Try.from(() -> new IntToBoolean().convert(2))
                        .recover(t -> t instanceof IllegalAccessException)
                        .get());
        Assert.assertTrue("Multiple thrown classes are handled (throwable #2)",
                Try.from(() -> new IntToBoolean().convert(3))
                        .recover(t -> t instanceof IllegalArgumentException)
                        .get());
    }

    private static class IntToBoolean {

        boolean convert(int result) throws IllegalAccessException, IllegalArgumentException {
            switch (result) {
                case 0:
                    return false;
                case 1:
                    return true;
                case 2:
                    // You have no authority to discover the dark truth about
                    // the third hidden boolean value
                    throw new IllegalAccessException();
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
