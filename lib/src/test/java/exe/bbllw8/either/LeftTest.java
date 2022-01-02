/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class LeftTest {

    @Test
    public void isLeft() {
        Assert.assertTrue(new Left<>(new Object()).isLeft());
    }

    @Test
    public void isRight() {
        Assert.assertFalse(new Left<>(new Object()).isRight());
    }

    @Test
    public void contains() {
        Assert.assertFalse(new Left<>("something").contains("something"));
        Assert.assertFalse(new Left<>("something").contains("anything"));
    }

    @Test
    public void exists() {
        Assert.assertFalse(new Left<Integer, Integer>(12).exists(x -> x > 10));
        Assert.assertFalse(new Left<Integer, Integer>(7).exists(x -> x > 10));
    }

    @Test
    public void filterOrElse() {
        Assert.assertEquals(new Left<>(-1),
                new Right<>(7).filterOrElse(x -> x > 10, -1));
        Assert.assertEquals(new Left<>(7),
                new Left<Integer, Integer>(7).filterOrElse(x -> x > 10, -1));
    }

    @Test
    public void flatMap() {
        Assert.assertEquals(new Left<>(12),
                new Left<Integer, Integer>(12).flatMap(x -> new Right<>(-1)));
        Assert.assertEquals(new Left<>(-1),
                new Right<Integer, Integer>(12).flatMap(x -> new Left<>(-1)));
    }

    @Test
    public void fold() {
        Assert.assertTrue(new Left<>(12).fold(x -> true, x -> false));
    }

    @Test
    public void forAll() {
        Assert.assertTrue(new Left<Integer, Integer>(11).forAll(x -> x > 10));
        Assert.assertTrue(new Left<Integer, Integer>(7).forAll(x -> x > 10));
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder("Hello");

        new Left<>("world").forEach(sb::append);
        Assert.assertEquals("Hello", sb.toString());
    }

    @Test
    public void forEachBi() {
        final StringBuilder sbLeft = new StringBuilder();
        final StringBuilder sbRight = new StringBuilder();

        new Left<>("cookie").forEach(sbLeft::append, sbRight::append);
        Assert.assertEquals("", sbRight.toString());
        Assert.assertEquals("cookie", sbLeft.toString());
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("cookie",
                new Left<String, String>("pancake").getOrElse("cookie"));
    }

    @Test
    public void flatten() {
        Assert.assertEquals(new Left<>("pancake"),
                Either.flatten(new Left<String, Either<String, Integer>>("pancake")));
        Assert.assertEquals(new Left<>("cookie"),
                Either.flatten(new Left<>("cookie")));
    }

    @Test
    public void map() {
        Assert.assertEquals(new Left<>(12),
                new Left<>(12).map(x -> "flower"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals(new Left<>(2),
                new Left<>(1).orElse(new Left<>(2)));
    }

    @Test
    public void stream() {
        Assert.assertEquals("",
                new Left<String, String>("cookie").stream().collect(Collectors.joining()));
    }

    @Test
    public void swap() {
        Assert.assertEquals(new Left<>("flower"),
                new Right<>("flower").swap());
    }

    @Test
    public void toOptional() {
        Assert.assertEquals(Optional.empty(),
                new Left<>(12).toOptional());
    }

    @Test
    public void joinLeft() {
        Assert.assertEquals(new Left<>(12),
                Either.joinLeft(new Left<>(new Left<>(12))));
    }

    @Test
    public void joinRight() {
        Assert.assertEquals(new Left<>("daisy"),
                Either.joinRight(new Right<>(new Left<>("daisy"))));
        Assert.assertEquals(new Left<>("flower"),
                Either.joinRight(new Left<>("flower")));
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

        Assert.assertNotEquals(12, new Left<>(12));
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("Left(12)",
                new Left<>(12).toString());
    }
}
