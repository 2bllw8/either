/*
 * Copyright (c) 2021-2025 2bllw8
 * SPDX-License-Identifier: BSD-3-Clause
 */
package exe.bbllw8.either;

import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

public class LeftProjectionTest {

    @Test
    public void containsProjectionFromLeftValuesEqual() {
        Assert.assertTrue(
                "Contains should return true if the projection is form a Left and the values are equal",
                new Left<>(12).left().contains(12));
    }

    @Test
    public void containsProjectionFromLeftValuesNotEqual() {
        Assert.assertFalse(
                "Contains should return false if the projection is form a Left and the values are not equal",
                new Left<>(7).left().contains(10));
    }

    @Test
    public void containsProjectionFromRight() {
        Assert.assertFalse("Contains should return false if the projection is from a Right",
                new Right<Integer, Integer>(12).left().contains(12));
    }

    @Test
    public void existsProjectionFromLeftPredicateSatisfied() {
        Assert.assertTrue("Exists should return true if the predicate is satisfied",
                new Left<>(12).left().exists(x -> x > 10));
    }

    @Test
    public void existsProjectionFromLeftPredicateNotSatisfied() {
        Assert.assertFalse("Exists should return false if the predicate is not satisfied",
                new Left<>(7).left().exists(x -> x > 10));
    }

    @Test
    public void existsProjectionFromRight() {
        Assert.assertFalse("Exists should return false the projection is from a Right",
                new Right<Integer, Integer>(12).left().exists(x -> x > 10));
    }

    @Test
    public void filterToOptionalProjectionFromLeftPredicateSatisfied() {
        Assert.assertEquals(
                "The Optional should contain the value of the Left if the predicate is satisfied",
                Optional.of(new Left<>(12)),
                new Left<>(12).left().filterToOptional(x -> x > 10));
    }

    @Test
    public void filterToOptionalProjectionFromLeftPredicateNotSatisfied() {
        Assert.assertEquals("The Optional should be empty if the predicate is not satisfied",
                Optional.empty(),
                new Left<>(7).left().filterToOptional(x -> x > 10));
    }

    @Test
    public void filterToOptionalProjectionFromRightPredicateSatisfied() {
        Assert.assertEquals(
                "The Optional should be empty if the projection is from a Right if the predicate is satisfied",
                Optional.empty(),
                new Right<Integer, Integer>(12).left().filterToOptional(x -> x > 10));
    }

    @Test
    public void filterToOptionalProjectionFromRightPredicateNotSatisfied() {
        Assert.assertEquals(
                "The Optional should be empty if the projection is from a Right if the predicate is not satisfied",
                Optional.empty(),
                new Right<Integer, Integer>(2).left().filterToOptional(x -> x < 1));
    }

    @Test
    public void flatMapProjectionFromLeft() {
        Assert.assertEquals(
                "The flatMap function should be applied if the projection is form a Left",
                new Left<>("flower"),
                new Left<Integer, Integer>(12).left().flatMap(x -> new Left<>("flower")));
    }

    @Test
    public void flatMapProjectionFromRight() {
        Assert.assertEquals(
                "The flatMap function should not be applied if the projection is form a Right",
                new Right<>(12),
                new Right<>(12).left().flatMap(x -> new Left<>("flower")));
    }

    @Test
    public void forAllProjectionFromLeftPredicateSatisfied() {
        Assert.assertTrue("The forAll function should return true if the predicate is satisfied",
                new Left<>(12).<Integer>withRight().left().forAll(x -> x > 10));
    }

    @Test
    public void forAllProjectionFromLeftPredicateNotSatisfied() {
        Assert.assertFalse(
                "The forAll function should return false if the predicate is not satisfied",
                new Left<>(7).<Integer>withRight().left().forAll(x -> x > 10));
    }

    @Test
    public void forAllProjectionFromRightPredicateSatisfied() {
        Assert.assertTrue(
                "The forAll function should return true if the projection is from a Right and predicate satisfied",
                new Right<>(7).<Integer>withLeft().left().forAll(x -> x > 10));
    }

    @Test
    public void forAllProjectionFromRightPredicateNotSatisfied() {
        Assert.assertTrue(
                "The forAll function should return true if the projection is from a Right and predicate not satisfied",
                new Right<>(7).<Integer>withLeft().left().forAll(x -> x > 10));
    }

    @Test
    public void forEachProjectionFromLeft() {
        final StringBuilder sb = new StringBuilder();

        new Left<>("Hello").left().forEach(sb::append);
        Assert.assertEquals(
                "The forEach function should be executed if the projection is from a Left",
                "Hello",
                sb.toString());
    }

    @Test
    public void forEachProjectionFromRight() {
        final StringBuilder sb = new StringBuilder();
        new Right<>("Hi").left().forEach(sb::append);
        Assert.assertEquals(
                "The forEach function should not be executed if the projection is from a Right",
                "",
                sb.toString());
    }

    @Test
    public void getOrElseProjectoinFromLeft() {
        Assert.assertEquals("The fallback should not be returned if the projection is from a Left",
                "apple",
                new Left<>("apple").left().getOrElse("orange"));
    }

    @Test
    public void getOrElseProjectionFromRight() {
        Assert.assertEquals("The fallback should be returned if the projection is from a Right",
                "orange",
                new Right<>("apple").left().getOrElse("orange"));
    }

    @Test
    public void mapProjectionFromLeft() {
        Assert.assertEquals("The map function should be applied if the projection is from a Left",
                new Left<>(14),
                new Left<>(12).left().map(x -> x + 2));
    }

    @Test
    public void mapProjectionFromRight() {
        Assert.assertEquals(
                "The map function should not be applied if the projection is from a Right",
                new Right<>(12),
                new Right<>(12).<Integer>withLeft().left().map(x -> x + 2));
    }

    @Test
    public void streamProjectionFromLeft() {
        Assert.assertEquals(
                "The stream should contain the Left value if the projection is from a Left",
                "pancake",
                new Left<>("pancake").left().stream().collect(Collectors.joining()));
    }

    @Test
    public void streamProjectionFromRight() {
        Assert.assertEquals("The stream should be empty if the projection is from a Right",
                0L,
                new Right<String, String>("cookie").left().stream().count());
    }

    @Test
    public void toOptionalProjectionFromLeft() {
        Assert.assertEquals(
                "The projection should be mapped to a Optional with value if it's from a Left",
                Optional.of(12),
                new Left<>(12).left().toOptional());
    }

    @Test
    public void toOptionalProjectionFromRight() {
        Assert.assertEquals(
                "The projection should be mapped to an empty Optional if it's from a Right",
                Optional.empty(),
                new Right<>(12).left().toOptional());
    }
}
