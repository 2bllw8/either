/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Test;

public class RightEitherTest {
    private static final Either<String, List<Integer>> TEST_VAL = Either.right(List.of(0, 1, 2));

    @Test
    public void isNotLeft() {
        Assert.assertFalse(TEST_VAL.isLeft());
    }

    @Test
    public void isRight() {
        Assert.assertTrue(TEST_VAL.isRight());
    }

    @Test(expected = NoSuchElementException.class)
    public void doesntHaveLeftValue() {
        TEST_VAL.getLeft();
    }

    @Test
    public void hasRightValue() {
        Assert.assertEquals(List.of(0, 1, 2), TEST_VAL.getRight());
    }

    @Test
    public void either() {
        Assert.assertEquals(3, (int) TEST_VAL.either(String::length, List::size));
    }

    @Test
    public void mapLeftDoesNothing() {
        //noinspection AssertBetweenInconvertibleTypes
        Assert.assertEquals(TEST_VAL, TEST_VAL.mapLeft(String::length));
    }

    @Test
    public void mapRight() {
        Assert.assertEquals(Either.right(3), TEST_VAL.mapRight(List::size));
    }

    @Test
    public void biMap() {
        Assert.assertEquals(Either.right(3), TEST_VAL.biMap(String::length, List::size));
    }

    @Test
    public void swap() {
        Assert.assertEquals(Either.left(List.of(0, 1, 2)), TEST_VAL.swap());
    }
}
