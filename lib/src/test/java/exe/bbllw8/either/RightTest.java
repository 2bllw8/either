/*
 * Copyright (c) 2021-2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class RightTest {

    @Test
    public void isLeft() {
        Assert.assertFalse(new Right<>(new Object()).isLeft());
    }

    @Test
    public void isRight() {
        Assert.assertTrue(new Right<>(new Object()).isRight());
    }

    @Test
    public void contains() {
        Assert.assertTrue(new Right<>("something").contains("something"));
        Assert.assertFalse(new Right<>("something").contains("anything"));
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
    }

    @Test
    public void flatMap() {
        Assert.assertEquals(new Right<>(12f),
                new Right<Integer, Integer>(12).flatMap(x -> new Right<>((float) x)));
        Assert.assertEquals(new Right<>(-1),
                new Right<Integer, Integer>(12).flatMap(x -> new Right<>(-1)));
    }

    @Test
    public void fold() {
        Assert.assertEquals("Hello world",
                new Right<Integer, String>("Hello ").fold(x -> x + 1, x -> x + "world"));
    }

    @Test
    public void forAll() {
        Assert.assertTrue(new Right<>(12).forAll(x -> x > 10));
        Assert.assertFalse(new Right<>(7).forAll(x -> x > 10));
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder();

        new Right<>("someone").forEach(sb::append);
        Assert.assertEquals("someone", sb.toString());
    }

    @Test
    public void forEachBi() {
        final StringBuilder sbLeft = new StringBuilder();
        final StringBuilder sbRight = new StringBuilder();

        new Right<>("cookie").forEach(sbLeft::append, sbRight::append);
        Assert.assertEquals("cookie", sbRight.toString());
        Assert.assertEquals("", sbLeft.toString());
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("pancake",
                new Right<String, String>("pancake").getOrElse("cookie"));
    }

    @Test
    public void flatten() {
        Assert.assertEquals(new Left<>("cookie"),
                Either.flatten(new Right<>(new Left<>("cookie"))));
        Assert.assertEquals(new Right<>(7),
                Either.flatten(new Right<>(new Right<>(7))));
    }

    @Test
    public void map() {
        Assert.assertEquals(new Right<>("flower"),
                new Right<>(12).map(x -> "flower"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals(new Right<>(1),
                new Right<>(1).orElse(new Left<>(2)));
        Assert.assertEquals(new Right<>(3),
                new Left<>(1).orElse(new Left<>(2)).orElse(new Right<>(3)));
    }

    @Test
    public void stream() {
        Assert.assertEquals("pancake",
                new Right<>("pancake").stream().collect(Collectors.joining()));
    }

    @Test
    public void swap() {
        Assert.assertEquals(new Right<>(12),
                new Left<>(12).swap());
    }

    @Test
    public void toOptional() {
        Assert.assertEquals(Optional.of(12),
                new Right<>(12).toOptional());
    }

    @Test
    public void joinLeft() {
        Assert.assertEquals(new Right<>("flower"),
                Either.joinLeft(new Left<>(new Right<>("flower"))));
        Assert.assertEquals(new Right<>("daisy"),
                Either.joinLeft(new Right<>("daisy")));
    }

    @Test
    public void joinRight() {
        Assert.assertEquals(new Right<>(12),
                Either.joinRight(new Right<>(new Right<>(12))));
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void consistentEquality() {
        Assert.assertEquals(new Right<>(12),
                new Right<>(10).map(x -> x + 2));
        Assert.assertEquals(new Right<>(12).hashCode(),
                new Right<>(10).map(x -> x + 2).hashCode());
        Assert.assertNotEquals(new Left<Integer, Integer>(12),
                new Right<Integer, Integer>(12));
        Assert.assertNotEquals(new Left<>(12).hashCode(),
                new Right<>(12).hashCode());

        Assert.assertNotEquals(12, new Right<>(12));
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("Right(12)",
                new Right<>(12).toString());
    }
}
