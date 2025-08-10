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
        Assert.assertFalse("Should not be a left",
                new Right<>(new Object()).isLeft());
    }

    @Test
    public void isRight() {
        Assert.assertTrue("Should be a right",
                new Right<>(new Object()).isRight());
    }

    @Test
    public void containsValueEqual() {
        Assert.assertTrue("Should return true if the value is equal",
                new Right<>("something").contains("something"));
    }

    @Test
    public void containsValueNotEqual() {
        Assert.assertFalse("Should return false if the value is not equal",
                new Right<>("something").contains("anything"));
    }

    @Test
    public void existsPredicateSatisfied() {
        Assert.assertTrue("Should return true if the predicate is satisfied",
                new Right<>(12).exists(x -> x > 10));
    }

    @Test
    public void existsPredicateNotSatisfied() {
        Assert.assertFalse("Should return false if the predicate is not satisfied",
                new Right<>(7).exists(x -> x > 10));
    }

    @Test
    public void filterOrElsePredicateSatisfied() {
        Assert.assertEquals("Should return itself if the predicate is satisfied",
                new Right<>(12),
                new Right<>(12).filterOrElse(x -> x > 10, -1));
    }

    @Test
    public void filterOrElsePredicateNotSatisfied() {
        Assert.assertEquals(
                "Should return a Left with the fallback value if the predicate is not satisfied",
                new Left<>(-1),
                new Right<>(7).filterOrElse(x -> x > 10, -1));
    }

    @Test
    public void flatMapAppliedRight() {
        Assert.assertEquals("Should apply the function (right)",
                new Right<>(12f),
                new Right<Integer, Integer>(12).flatMap(x -> new Right<>((float) x)));
    }

    @Test
    public void flatMapAppliedLeft() {
        Assert.assertEquals("Should apply the function (left)",
                new Left<>(-1),
                new Right<Integer, Integer>(12).flatMap(x -> new Left<>(-1)));
    }

    @Test
    public void fold() {
        Assert.assertEquals("Should apply the correct function",
                "Hello world",
                new Right<Integer, String>("Hello ").fold(x -> x + 1, x -> x + "world"));
    }

    @Test
    public void forAllPredicateSatisfied() {
        Assert.assertTrue("Should return true if the predicate is satisfied",
                new Right<>(12).forAll(x -> x > 10));
    }

    @Test
    public void forAllPredicateNotSatisfied() {
        Assert.assertFalse("Should return false if the predicate is not satisfied",
                new Right<>(7).forAll(x -> x > 10));
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder();

        new Right<>("someone").forEach(sb::append);
        Assert.assertEquals("The function should have been applied",
                "someone",
                sb.toString());
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Test
    public void forEachBiRight() {
        final StringBuilder sbLeft = new StringBuilder();
        final StringBuilder sbRight = new StringBuilder();

        new Right<>("cookie").forEach(sbLeft::append, sbRight::append);
        Assert.assertEquals("The Right function should have been applied",
                "cookie",
                sbRight.toString());

    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Test
    public void forEachBiLeft() {
        final StringBuilder sbLeft = new StringBuilder();
        final StringBuilder sbRight = new StringBuilder();

        new Right<>("cookie").forEach(sbLeft::append, sbRight::append);
        Assert.assertEquals("The Left function should not have been applied",
                "",
                sbLeft.toString());
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("The fallback should not be returned",
                "pancake",
                new Right<String, String>("pancake").getOrElse("cookie"));
    }

    @Test
    public void flattenInnerLeft() {
        Assert.assertEquals("The inner Left value should be returned",
                new Left<>("cookie"),
                Either.flatten(new Right<>(new Left<>("cookie"))));
    }

    @Test
    public void flattenInnerRight() {
        Assert.assertEquals("The inner Right value should be returned",
                new Right<>(7),
                Either.flatten(new Right<>(new Right<>(7))));
    }

    @Test
    public void map() {
        Assert.assertEquals("The function should be applied",
                new Right<>("flower"),
                new Right<>(12).map(x -> "flower"));
    }

    @Test
    public void orElse() {
        Assert.assertEquals("Should return itself",
                new Right<>(1),
                new Right<>(1).orElse(new Left<>(2)));
    }

    @Test
    public void stream() {
        Assert.assertEquals("The stream should contain the Right value",
                "pancake",
                new Right<>("pancake").stream().collect(Collectors.joining()));
    }

    @Test
    public void swap() {
        Assert.assertEquals("A Left with the same value should be returned",
                new Right<>(12),
                new Left<>(12).swap());
    }

    @Test
    public void toOptional() {
        Assert.assertEquals("An Optional with the same value should be returned",
                Optional.of(12),
                new Right<>(12).toOptional());
    }

    @Test
    public void joinLeft() {
        Assert.assertEquals("The type and value should be retained",
                new Right<>("daisy"),
                Either.joinLeft(new Right<>("daisy")));
    }

    @Test
    public void joinRight() {
        Assert.assertEquals("The inner value should be returned",
                new Right<>(12),
                Either.joinRight(new Right<>(new Right<>(12))));
    }

    @SuppressWarnings({
            "AssertBetweenInconvertibleTypes",
            "PMD.UnitTestContainsTooManyAsserts",
    })
    @Test
    public void consistentEquality() {
        Assert.assertEquals("Equal values and types should be equal",
                new Right<>(12),
                new Right<>(10).map(x -> x + 2));
        Assert.assertEquals("Equal values and types should have the same hashCode",
                new Right<>(12).hashCode(),
                new Right<>(10).map(x -> x + 2).hashCode());
        Assert.assertNotEquals("A Right should not be equal to a Left with the same value",
                new Left<Integer, Integer>(12),
                new Right<Integer, Integer>(12));
        Assert.assertNotEquals(
                "A Right should not have the same hashCode of a Left with the same value",
                new Left<>(12).hashCode(),
                new Right<>(12).hashCode());

        Assert.assertNotEquals("A Right should not be equals to its value",
                12,
                new Right<>(12));
    }

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("The string representation should match the documentation",
                "Right(12)",
                new Right<>(12).toString());
    }
}
