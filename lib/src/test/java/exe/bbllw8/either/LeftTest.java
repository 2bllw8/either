/*
 * Copyright (c) 2021-2025 2bllw8
 * SPDX-License-Identifier: BSD-3-Clause
 */
package exe.bbllw8.either;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class LeftTest {

    @Test
    public void isLeft() {
        Assert.assertTrue("Should be a left",
                new Left<>(new Object()).isLeft());
    }

    @Test
    public void isRight() {
        Assert.assertFalse("Should not be a right",
                new Left<>(new Object()).isRight());
    }

    @Test
    public void containsValueEqual() {
        Assert.assertFalse("Should return false even if the value is equal",
                new Left<>("something").contains("something"));
    }

    @Test
    public void containsValueNotEqual() {
        Assert.assertFalse("Should return false if the value is not equal",
                new Left<>("something").contains("anything"));
    }

    @Test
    public void existsPredicateSatisfied() {
        Assert.assertFalse("Should return false even if the predicate is satisfied",
                new Left<Integer, Integer>(12).exists(x -> x > 10));
    }

    @Test
    public void existsPredicateNotSatisfied() {
        Assert.assertFalse("Should return false if the predicate is not satisfied",
                new Left<Integer, Integer>(7).exists(x -> x > 10));
    }

    @Test
    public void filterOrElse() {
        Assert.assertEquals("Should always return the its value",
                new Left<>(7),
                new Left<Integer, Integer>(7).filterOrElse(x -> x > 10, -1));
    }

    @Test
    public void flatMap() {
        Assert.assertEquals("Should retain its value",
                new Left<>(12),
                new Left<Integer, Integer>(12).flatMap(x -> new Right<>(-1)));
    }

    @Test
    public void fold() {
        Assert.assertTrue("The correct folding function should be applied",
                new Left<>(12).fold(x -> true, x -> false));
    }

    @Test
    public void forAllPredicateSatisfied() {
        Assert.assertTrue("Should return true if the predicate is satisfied",
                new Left<Integer, Integer>(11).forAll(x -> x > 10));
    }

    @Test
    public void forAllPredicateNotSatisfied() {
        Assert.assertTrue("Should return true even if the predicate is not satisfied",
                new Left<Integer, Integer>(7).forAll(x -> x > 10));
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder("Hello");

        new Left<>("world").forEach(sb::append);
        Assert.assertEquals("The function should not be applied",
                "Hello",
                sb.toString());
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Test
    public void forEachBiRightFunction() {
        final StringBuilder sbLeft = new StringBuilder();
        final StringBuilder sbRight = new StringBuilder();

        new Left<>("moon").forEach(sbLeft::append, sbRight::append);
        Assert.assertEquals("The Right function should not be invoked",
                "",
                sbRight.toString());
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Test
    public void forEachBiLeftFunction() {
        final StringBuilder sbLeft = new StringBuilder();
        final StringBuilder sbRight = new StringBuilder();

        new Left<>("bird").forEach(sbLeft::append, sbRight::append);
        Assert.assertEquals("The Left function should be invoked",
                "bird",
                sbLeft.toString());
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("The fallback value should be returned",
                "cake",
                new Left<String, String>("pancake").getOrElse("cake"));
    }

    @Test
    public void flatten() {
        Assert.assertEquals("The value and type should be retained",
                new Left<>("cookie"),
                Either.flatten(new Left<>("cookie")));
    }

    @Test
    public void map() {
        Assert.assertEquals("The function should not be applied",
                new Left<>(12),
                new Left<>(12).map(x -> "rock"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals("The fallback value should be returned",
                new Left<>(2),
                new Left<>(1).orElse(new Left<>(2)));
    }

    @Test
    public void stream() {
        Assert.assertEquals("The stream should be empty",
                0L,
                new Left<String, String>("cookie").stream().count());
    }

    @Test
    public void swap() {
        Assert.assertEquals("A Right with the same value should be returned",
                new Left<>("star"),
                new Right<>("star").swap());
    }

    @Test
    public void toOptional() {
        Assert.assertEquals("An empty Optional should be returned",
                Optional.empty(),
                new Left<>(12).toOptional());
    }

    @Test
    public void joinLeft() {
        Assert.assertEquals("The inner value should be returned",
                new Left<>(12),
                Either.joinLeft(new Left<>(new Left<>(12))));
    }

    @Test
    public void joinRight() {
        Assert.assertEquals("The type and value should be retained",
                new Left<>("flower"),
                Either.joinRight(new Left<>("flower")));
    }

    @Test
    public void joinRightInnerValue() {
        Assert.assertEquals("The inner value should be returned",
                new Left<>("daisy"),
                Either.joinRight(new Right<>(new Left<>("daisy"))));
    }

    @SuppressWarnings({
            "AssertBetweenInconvertibleTypes",
            "PMD.UnitTestContainsTooManyAsserts",
    })
    @Test
    public void consistentEquality() {
        Assert.assertEquals("Equal values and types should be equal",
                new Left<>(12),
                new Left<>(10).left().map(x -> x + 2));
        Assert.assertEquals("Equal values and types should have the same hashCode",
                new Left<>(12).hashCode(),
                new Left<>(10).left().map(x -> x + 2).hashCode());
        Assert.assertNotEquals("A Left should not be equal to a Right with the same value",
                new Right<Integer, Integer>(12),
                new Left<Integer, Integer>(12));
        Assert.assertNotEquals(
                "A Left should not have the same hashCode of a Right with the same value",
                new Right<>(12).hashCode(),
                new Left<>(12).hashCode());

        Assert.assertNotEquals("A Left should not be equals to its value",
                12,
                new Left<>(12));
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("The string representation should match the documentation",
                "Left(12)",
                new Left<>(12).toString());
    }
}
