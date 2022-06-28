/*
 * Copyright (c) 2021-2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class LeftProjectionTest {

    @Test
    public void contains() {
        Assert.assertTrue("Contains should return true if the values are equal",
                new Left<>(12).left().contains(12));
        Assert.assertFalse("Contains should return false if the values are not equal",
                new Left<>(7).left().contains(10));
        Assert.assertFalse("Exists should return false if the projection is from a Right",
                new Right<Integer, Integer>(12).left().contains(12));
    }

    @Test
    public void exists() {
        Assert.assertTrue("Exists should return true if the predicate is satisfied",
                new Left<>(12).left().exists(x -> x > 10));
        Assert.assertFalse("Exists should return false if the predicate is not satisfied",
                new Left<>(7).left().exists(x -> x > 10));
        Assert.assertFalse("Exists should return false the projection is from a Right",
                new Right<Integer, Integer>(12).left().exists(x -> x > 10));
    }

    @Test
    public void filterToOptional() {
        Assert.assertEquals(
                "The Optional should contain the value of the Left if the predicate is satisfied",
                Optional.of(new Left<>(12)),
                new Left<>(12).left().filterToOptional(x -> x > 10));
        Assert.assertEquals("The Optional should be empty if the predicate is not satisfied",
                Optional.empty(),
                new Left<>(7).left().filterToOptional(x -> x > 10));
        Assert.assertEquals("The Optional should be empty if the projection is from a Right",
                Optional.empty(),
                new Right<Integer, Integer>(12).left().filterToOptional(x -> x > 10));
    }

    @Test
    public void flatMap() {
        Assert.assertEquals(
                "The flatMap function should be applied if the projection is form a Left",
                new Left<>("flower"),
                new Left<Integer, Integer>(12).left().flatMap(x -> new Left<>("flower")));
        Assert.assertEquals(
                "The flatMap function should not be applied if the projection is form a Right",
                new Right<>(12),
                new Right<>(12).left().flatMap(x -> new Left<>("flower")));
    }

    @Test
    public void forAll() {
        Assert.assertTrue("The forAll function should return true if the predicate is satisfied",
                new Left<>(12).<Integer>withRight().left().forAll(x -> x > 10));
        Assert.assertFalse(
                "The forAll function should return false if the predicate is not satisfied",
                new Left<>(7).<Integer>withRight().left().forAll(x -> x > 10));
        Assert.assertTrue("The forAll function should return true if the projection is from a Right",
                new Right<>(7).<Integer>withLeft().left().forAll(x -> x > 10));
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder();

        new Left<>("Hello").left().forEach(sb::append);
        Assert.assertEquals(
                "The forEach function should be executed if the projection is from a Left",
                "Hello",
                sb.toString());

        new Right<>(" world").left().forEach(sb::append);
        Assert.assertEquals(
                "The forEach function should not be executed if the projection is from a Right",
                "Hello",
                sb.toString());
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("The fallback should not be returned if the projection is from a Left",
                "apple",
                new Left<>("apple").left().getOrElse("orange"));
        Assert.assertEquals("The fallback should be returned if the projection is from a Right",
                "orange",
                new Right<>("apple").left().getOrElse("orange"));
    }

    @Test
    public void map() {
        Assert.assertEquals("The map function should be applied if the projection is from a Left",
                new Left<>(14),
                new Left<>(12).left().map(x -> x + 2));
        Assert.assertEquals(
                "The map function should not be applied if the projection is from a Right",
                new Right<>(12),
                new Right<>(12).<Integer>withLeft().left().map(x -> x + 2));
    }

    @Test
    public void stream() {
        Assert.assertEquals(
                "The stream should contain the Left value if the projection is from a Left",
                "pancake",
                new Left<>("pancake").left().stream().collect(Collectors.joining()));
        Assert.assertEquals("The stream should be empty if the projection is from a Right",
                0L,
                new Right<String, String>("cookie").left().stream().count());
    }

    @Test
    public void toOptional() {
        Assert.assertEquals(
                "The projection should be mapped to a Optional with value if it's from a Left",
                Optional.of(12),
                new Left<>(12).left().toOptional());
        Assert.assertEquals(
                "The projection should be mapped to an empty Optional if it's from a Right",
                Optional.empty(),
                new Right<>(12).left().toOptional());
    }
}
