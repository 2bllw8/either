/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

public class SuccessTest {

    @Test
    public void isFailure() {
        Assert.assertFalse(new Success<>("pancake").isFailure());
        Assert.assertFalse(new Success<>(12).isFailure());
        Assert.assertFalse(Try.from(Object::new).isFailure());
    }

    @Test
    public void isSuccess() {
        Assert.assertTrue(new Success<>("something").isSuccess());
        Assert.assertTrue(new Success<>(3).isSuccess());
        Assert.assertTrue(Try.from(Object::new).isSuccess());
    }

    @Test
    public void get() {
        Assert.assertNotNull(Try.from(() -> "something").get());
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder("Hello ");
        Try.from(() -> "world").forEach(sb::append);
        Assert.assertEquals("Hello world", sb.toString());
    }

    @Test
    public void flatMap() {
        Assert.assertEquals(new Success<>(12),
                Try.from(() -> "12").flatMap(value -> Try.from(() -> Integer.parseInt(value))));
    }

    @Test
    public void map() {
        Assert.assertEquals(new Success<>(6),
                Try.from(() -> "flower").map(String::length));
    }

    @Test
    public void filter() {
        Assert.assertTrue(new Success<>(2).filter(i -> i % 2 == 0).isSuccess());
        Assert.assertFalse(new Success<>(3).filter(i -> i % 2 == 0).isSuccess());
    }

    @Test
    public void recoverWith() {
        Assert.assertEquals(new Success<>(123),
                new Success<>(123).recoverWith(t -> Try.from(() -> 456)));
    }

    @Test
    public void recover() {
        Assert.assertEquals(new Success<>(123),
                new Success<>(123).recover(t -> 456));
    }

    @Test
    public void toOptional() {
        Assert.assertEquals(Optional.of("cookie"), new Success<>("cookie").tOptional());
    }

    @Test
    public void toEither() {
        Assert.assertEquals(new Right<>("flower"), new Success<>("flower").toEither());
    }

    @Test
    public void failed() {
        Assert.assertTrue(new Success<>(1).failed().isFailure());
    }

    @Test
    public void transform() {
        Assert.assertEquals(new Success<>(12),
                Try.from(() -> "12").transform(value -> Try.from(() -> Integer.parseInt(value)),
                        t -> Try.from(() -> {
                            throw new CheckedException(t);
                        })));
    }

    @Test
    public void fold() {
        Assert.assertEquals("Operation completed with result pancake",
                Try.from(() -> "pancake").fold(t -> "Operation failed with error " + t,
                        value -> "Operation completed with result " + value));
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("1", new Success<>("1").getOrElse("2"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals(new Success<>(1),
                Try.from(() -> 1).orElse(Try.from(() -> 2)));
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void consistentEquality() {
        Assert.assertEquals(new Success<>(12),
                new Success<>(10).map(x -> x + 2));
        Assert.assertEquals(new Success<>(12).hashCode(),
                new Success<>(10).map(x -> x + 2).hashCode());

        final NumberFormatException nfe = new NumberFormatException();
        Assert.assertNotEquals(new Success<>(nfe),
                new Failure<>(nfe));
        Assert.assertNotEquals(new Success<>(nfe).hashCode(),
                new Failure<>(nfe).hashCode());

        Assert.assertNotEquals(12, new Success<>(12));
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("Success(something)",
                Try.from(() -> "something").toString());
    }
}
