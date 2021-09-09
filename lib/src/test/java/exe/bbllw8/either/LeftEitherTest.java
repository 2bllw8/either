/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Test;

public class LeftEitherTest {
    private static final Either<String, List<Integer>> TEST_VAL = Either.left("Left value");

    @Test
    public void isLeft() {
        Assert.assertTrue(TEST_VAL.isLeft());
    }

    @Test
    public void isNotRight() {
        Assert.assertFalse(TEST_VAL.isRight());
    }

    @Test
    public void hasLeftValue() {
        Assert.assertEquals("Left value", TEST_VAL.getLeft());
    }

    @Test(expected = NoSuchElementException.class)
    public void doesntHaveRightValue() {
        TEST_VAL.getRight();
    }

    @Test
    public void either() {
        Assert.assertEquals(10, (int) TEST_VAL.either(String::length, List::size));
    }

    @Test
    public void mapLeft() {
        Assert.assertEquals(Either.left(10), TEST_VAL.mapLeft(String::length));
    }

    @Test
    public void mapRightDoesNothing() {
        //noinspection AssertBetweenInconvertibleTypes
        Assert.assertEquals(TEST_VAL, TEST_VAL.mapRight(List::size));
    }

    @Test
    public void biMap() {
        Assert.assertEquals(Either.left(10), TEST_VAL.biMap(String::length, List::size));
    }

    @Test
    public void swap() {
        Assert.assertEquals(Either.right("Left value"), TEST_VAL.swap());
    }
}
