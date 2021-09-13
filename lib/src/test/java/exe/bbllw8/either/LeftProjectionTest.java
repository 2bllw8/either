/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class LeftProjectionTest {

    @Test
    public void exists() {
        Assert.assertTrue(new Left<>(12).left().exists(x -> x > 10));
        Assert.assertFalse(new Left<>(7).left().exists(x -> x > 10));
        Assert.assertFalse(new Right<Integer, Integer>(12).left().exists(x -> x > 10));
    }

    @Test
    public void filterToOptional() {
        Assert.assertEquals(Optional.of(new Left<>(12)),
                new Left<>(12).left().filterToOptional(x -> x > 10));
        Assert.assertEquals(Optional.empty(),
                new Left<>(7).left().filterToOptional(x -> x > 10));
        Assert.assertEquals(Optional.empty(),
                new Right<Integer, Integer>(12).left().filterToOptional(x -> x > 10));
    }

    @Test
    public void flatMap() {
        Assert.assertEquals(new Left<>("flower"),
                new Left<Integer, Integer>(12).left().flatMap(x -> new Left<>("flower")));
        Assert.assertEquals(new Right<>(12),
                new Right<>(12).left().flatMap(x -> new Left<>("flower")));
    }

    @Test
    public void forAll() {
        Assert.assertTrue(new Left<>(12).<Integer>withRight().left().forAll(x -> x > 10));
        Assert.assertFalse(new Left<>(7).<Integer>withRight().left().forAll(x -> x > 10));
    }

    @Test
    public void forEach() {
        final StringBuilder sb = new StringBuilder();

        new Left<>("Hello").left().forEach(sb::append);
        Assert.assertEquals("Hello", sb.toString());

        new Right<>(" world").left().forEach(sb::append);
        Assert.assertEquals("Hello", sb.toString());
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("apple",
                new Left<>("apple").left().getOrElse("orange"));
        Assert.assertEquals("orange",
                new Right<>("apple").left().getOrElse("orange"));
    }

    @Test
    public void map() {
        Assert.assertEquals(new Left<>(14),
                new Left<>(12).left().map(x -> x + 2));
        Assert.assertEquals(new Right<>(12),
                new Right<>(12).<Integer>withLeft().left().map(x -> x + 2));
    }

    @Test
    public void stream() {
        Assert.assertEquals("pancake",
                new Left<>("pancake").left().stream().collect(Collectors.joining()));
        Assert.assertEquals("",
                new Right<String, String>("cookie").left().stream().collect(Collectors.joining()));
    }

    @Test
    public void toOptional() {
        Assert.assertEquals(Optional.of(12),
                new Left<>(12).left().toOptional());
        Assert.assertEquals(Optional.empty(),
                new Right<>(12).left().toOptional());
    }
}
