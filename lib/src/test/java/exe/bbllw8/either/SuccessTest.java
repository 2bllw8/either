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
        Assert.assertFalse("A Success should not be a failure", new Success<>(12).isFailure());
        Assert.assertFalse("A supplier that does not throw an exception should not be a failure",
                Try.from(Object::new).isFailure());
    }

    @Test
    public void isSuccess() {
        Assert.assertTrue("A Success should be a success", new Success<>(3).isSuccess());
        Assert.assertTrue("A supplier that does not throw an exception should be a success",
                Try.from(Object::new).isSuccess());
    }

    @Test
    public void get() {
        Assert.assertNotNull("The value should be returned", Try.from(() -> "something").get());
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder("Hello ");
        Try.from(() -> "world").forEach(sb::append);
        Assert.assertEquals("The function should have been applied", "Hello world", sb.toString());
    }

    @Test
    public void biForEach() {
        final StringBuilder sb = new StringBuilder();
        new Success<>(0).forEach(
                sb::append,
                t -> sb.append(t.getMessage()));
        Assert.assertEquals("Should execute the right function",
                "0",
                sb.toString());
    }

    @Test
    public void flatMap() {
        Assert.assertEquals("The function should be applied",
                new Success<>(12),
                Try.from(() -> "12").flatMap(value -> Try.from(() -> Integer.parseInt(value))));
    }

    @Test
    public void map() {
        Assert.assertEquals("The function should be applied",
                new Success<>(6),
                Try.from(() -> "flower").map(String::length));
    }

    @Test
    public void filter() {
        Assert.assertTrue("Should return a success if the predicate holds",
                new Success<>(2).filter(i -> i % 2 == 0).isSuccess());
        Assert.assertFalse("Should not return a success if the predicate does not hold",
                new Success<>(3).filter(i -> i % 2 == 0).isSuccess());
    }

    @Test
    public void recoverWith() {
        Assert.assertEquals("Should return itself",
                new Success<>(123),
                new Success<>(123).recoverWith(t -> Try.from(() -> 456)));
    }

    @Test
    public void recover() {
        Assert.assertEquals("Should return itself",
                new Success<>(123),
                new Success<>(123).recover(t -> 456));
    }

    @Test
    public void toOptional() {
        Assert.assertEquals("Should return an Optional with the same value",
                Optional.of("cookie"),
                new Success<>("cookie").tOptional());
    }

    @Test
    public void toEither() {
        Assert.assertEquals("Should return a Right with the same value",
                new Right<>("flower"),
                new Success<>("flower").toEither());
    }

    @Test
    public void failed() {
        Assert.assertTrue("Should return a Failure", new Success<>(1).failed().isFailure());
    }

    @Test
    public void transform() {
        Assert.assertEquals("Should apply the correct transformation",
                new Success<>(12),
                Try.from(() -> "12").transform(value -> Try.from(() -> Integer.parseInt(value)),
                        t -> Try.from(() -> {
                            throw t;
                        })));
    }

    @Test
    public void fold() {
        Assert.assertEquals("The correct folding function should be applied",
                "Operation completed with result pancake",
                Try.from(() -> "pancake").fold(t -> "Operation failed with error " + t,
                        value -> "Operation completed with result " + value));
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("The fallback value should not be returned",
                "1",
                new Success<>("1").getOrElse("2"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals("The fallback result should not be returned",
                new Success<>(1),
                Try.from(() -> 1).orElse(Try.from(() -> 2)));
    }

    @Test
    public void stream() {
        Assert.assertEquals("Stream contains the right value",
                new Success<>(8).stream().mapToInt(i -> i).sum(),
                8);
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void consistentEquality() {
        Assert.assertEquals("Equal values should be equal",
                new Success<>(12),
                new Success<>(10).map(x -> x + 2));
        Assert.assertEquals("Equal values should have the same hashCode",
                new Success<>(12).hashCode(),
                new Success<>(10).map(x -> x + 2).hashCode());

        final NumberFormatException nfe = new NumberFormatException();
        Assert.assertNotEquals("Should not be equal of a Failure with the same value",
                new Success<>(nfe),
                new Failure<>(nfe));
        Assert.assertNotEquals("Should not have the same hashCode of a Failure with the same value",
                new Success<>(nfe).hashCode(),
                new Failure<>(nfe).hashCode());

        Assert.assertNotEquals("Should not be equal to its value", 12, new Success<>(12));
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("The string representation should match the documentation",
                "Success(something)",
                Try.from(() -> "something").toString());
    }
}
