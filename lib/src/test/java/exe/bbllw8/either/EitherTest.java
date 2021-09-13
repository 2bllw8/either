/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class EitherTest {

    @Test
    public void contains() {
        Assert.assertTrue(new Right<>("something").contains("something"));
        Assert.assertFalse(new Right<>("something").contains("anything"));
        Assert.assertFalse(new Left<>("something").contains("something"));
    }

    @Test
    public void exists() {
        Assert.assertTrue(new Right<>(12).exists(x -> x > 10));
        Assert.assertFalse(new Right<>(7).exists(x -> x > 10));
        Assert.assertTrue(new Right<>(12).exists(x -> true));
    }

    @Test
    public void filterOrElse() {
        Assert.assertEquals(new Right<>(12),
                new Right<>(12).filterOrElse(x -> x > 10, -1));
        Assert.assertEquals(new Left<>(-1),
                new Right<>(7).filterOrElse(x -> x > 10, -1));
        Assert.assertEquals(new Left<>(7),
                new Left<Integer, Integer>(7).filterOrElse(x -> x > 10, -1));
    }

    @Test
    public void flatMap() {
        Assert.assertEquals(new Right<>(12f),
                new Right<Integer, Integer>(12).flatMap(x -> new Right<>((float) x)));
        Assert.assertEquals(new Left<>(12),
                new Left<Integer, Integer>(12).flatMap(x -> new Right<>(-1)));
        Assert.assertEquals(new Left<>(-1),
                new Right<Integer, Integer>(12).flatMap(x -> new Left<>(-1)));
    }

    @Test
    public void fold() {
        Assert.assertEquals("Hello world",
                new Right<Integer, String>("Hello ").fold(x -> x + 1, x -> x + "world"));
        Assert.assertTrue(new Left<>(12).fold(x -> true, x -> false));
    }

    @Test
    public void forAll() {
        Assert.assertTrue(new Right<>(12).forAll(x -> x > 10));
        Assert.assertFalse(new Right<>(7).forAll(x -> x > 10));
        Assert.assertTrue(new Left<Integer, Integer>(7).forAll(x -> x > 10));
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder();

        new Right<>("Hello").forEach(sb::append);
        Assert.assertEquals("Hello", sb.toString());

        new Left<>(" world").forEach(sb::append);
        Assert.assertEquals("Hello", sb.toString());
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("pancake",
                new Right<String, String>("pancake").getOrElse("cookie"));
        Assert.assertEquals("cookie",
                new Left<String, String>("pancake").getOrElse("cookie"));
    }

    @Test
    public void flatten() {
        Assert.assertEquals(new Left<>("pancake"),
                Either.flatten(new Left<String, Either<String, Integer>>("pancake")));
        Assert.assertEquals(new Left<>("cookie"),
                Either.flatten(new Right<>(new Left<>("cookie"))));
        Assert.assertEquals(new Right<>(7),
                Either.flatten(new Right<>(new Right<>(7))));
    }

    @Test
    public void map() {
        Assert.assertEquals(new Right<>("flower"),
                new Right<>(12).map(x -> "flower"));
        Assert.assertEquals(new Left<>(12),
                new Left<>(12).map(x -> "flower"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals(new Right<>(1),
                new Right<>(1).orElse(new Left<>(2)));
        Assert.assertEquals(new Left<>(2),
                new Left<>(1).orElse(new Left<>(2)));
        Assert.assertEquals(new Right<>(3),
                new Left<>(1).orElse(new Left<>(2)).orElse(new Right<>(3)));
    }

    @Test
    public void stream() {
        Assert.assertEquals("pancake",
                new Right<>("pancake").stream().collect(Collectors.joining()));
        Assert.assertEquals("",
                new Left<String, String>("cookie").stream().collect(Collectors.joining()));
    }

    @Test
    public void swap() {
        Assert.assertEquals(new Left<>("flower"),
                new Right<>("flower").swap());
        Assert.assertEquals(new Right<>(12),
                new Left<>(12).swap());
    }

    @Test
    public void toOptional() {
        Assert.assertEquals(Optional.of(12),
                new Right<>(12).toOptional());
        Assert.assertEquals(Optional.empty(),
                new Left<>(12).toOptional());
    }

    @Test
    public void joinLeft() {
        Assert.assertEquals(new Right<>("flower"),
                Either.joinLeft(new Left<>(new Right<>("flower"))));
        Assert.assertEquals(new Left<>(12),
                Either.joinLeft(new Left<>(new Left<>(12))));
        Assert.assertEquals(new Right<>("daisy"),
                Either.joinLeft(new Right<>("daisy")));
    }

    @Test
    public void joinRight() {
        Assert.assertEquals(new Right<>(12),
                Either.joinRight(new Right<>(new Right<>(12))));
        Assert.assertEquals(new Left<>("flower"),
                Either.joinRight(new Right<>(new Left<>("flower"))));
        Assert.assertEquals(new Left<>("flower"),
                Either.joinRight(new Left<>("flower")));
    }

    @Test
    public void fromTry() {
        Assert.assertTrue(Either.fromTry(() -> Integer.parseInt("12"), NumberFormatException.class).isRight());
        Assert.assertTrue(Either.fromTry(() -> Integer.parseInt("pancake"), NumberFormatException.class).isLeft());
    }

    @Test(expected = NumberFormatException.class)
    public void fromTryUnexpected() {
        Either.fromTry(() -> Integer.parseInt("pancake"), IOException.class);
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void consistentEquality() {
        Assert.assertEquals(new Left<>(12),
                new Left<>(10).left().map(x -> x + 2));
        Assert.assertEquals(new Left<>(12).hashCode(),
                new Left<>(10).left().map(x -> x + 2).hashCode());
        Assert.assertNotEquals(new Right<Integer, Integer>(12),
                new Left<Integer, Integer>(12));
        Assert.assertNotEquals(new Right<>(12).hashCode(),
                new Left<>(12).hashCode());

        Assert.assertEquals(new Right<>(12),
                new Right<>(10).map(x -> x + 2));
        Assert.assertEquals(new Right<>(12).hashCode(),
                new Right<>(10).map(x -> x + 2).hashCode());
        Assert.assertNotEquals(new Left<Integer, Integer>(12),
                new Right<Integer, Integer>(12));
        Assert.assertNotEquals(new Left<>(12).hashCode(),
                new Right<>(12).hashCode());

        Assert.assertNotEquals(12, new Left<>(12));
        Assert.assertNotEquals(12, new Right<>(12));
    }
}
