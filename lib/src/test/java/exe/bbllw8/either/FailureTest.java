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
        Assert.assertTrue(new Failure<>(new Throwable("pancake")).isFailure());
        Assert.assertTrue(Try.from(() -> {
            throw new IllegalStateException();
        }).isFailure());
    }

    @Test
    public void isSuccess() {
        Assert.assertFalse(new Failure<>(new Throwable("cookie")).isSuccess());
        Assert.assertFalse(Try.from(() -> {
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
        Assert.assertEquals("Hello ", sb.toString());
    }

    @Test
    public void flatMap() {
        final NullPointerException npe = new NullPointerException();

        Assert.assertEquals(new Failure<>(npe), Try.from(() -> {
            throw npe;
        }).flatMap(value -> Try.from(value::toString)));
    }

    @Test
    public void map() {
        final NullPointerException npe = new NullPointerException();

        Assert.assertEquals(new Failure<>(npe), Try.from(() -> {
            throw npe;
        }).map(Object::hashCode));
    }

    @Test
    public void filter() {
        Assert.assertTrue(new Failure<Integer>(new Throwable()).filter(i -> i % 2 == 0).isFailure());
    }

    @Test
    public void recoverWith() {
        Assert.assertEquals(new Success<>(1), new Failure<>(new Throwable("1")).recoverWith(t -> Try.from(() -> Integer.parseInt(t.getMessage()))));
    }

    @Test
    public void recover() {
        Assert.assertEquals(new Success<>(1), new Failure<>(new Throwable("1")).recover(t -> Integer.parseInt(t.getMessage())));
    }

    @Test
    public void toOptional() {
        Assert.assertEquals(Optional.empty(), new Failure<>(new Throwable()).tOptional());
    }

    @Test
    public void toEither() {
        final IllegalAccessException iae = new IllegalAccessException();

        Assert.assertEquals(new Left<>(iae),
                new Failure<>(iae).toEither());
    }

    @Test
    public void failed() {
        Assert.assertTrue(new Failure<Integer>(new Throwable()).failed().isSuccess());
    }

    @Test
    public void transform() {
        Assert.assertEquals(new Success<>(12), Try.from(() -> "12").transform(value -> Try.from(() -> Integer.parseInt(value)), t -> Try.from(() -> {
            throw new CheckedException(t);
        })));
    }

    @Test
    public void fold() {
        Assert.assertEquals("Operation failed with error: Not enough pancakes", Try.from(() -> {
            throw new IllegalStateException("Not enough pancakes");
        }).fold(t -> "Operation failed with error: " + t.getMessage(),
                value -> "Operation completed with result: " + value));
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("something", new Failure<>(new Throwable()).getOrElse("something"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals(new Success<>("pancake"), Try.from(() -> {
            throw new ArrayIndexOutOfBoundsException();
        }).orElse(Try.from(() -> "pancake")));
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("Failure(java.lang.IllegalStateException: something)", Try.from(() -> {
            throw new IllegalStateException("something");
        }).toString());
    }
}
