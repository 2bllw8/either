/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class EitherTest {

    @Test
    public void leftAndRightNotEqual() {
        Assert.assertNotEquals(Either.<Integer, Boolean>left(2),
                Either.<Integer, Boolean>right(false));
        Assert.assertNotEquals(Either.<Integer, Boolean>left(2).hashCode(),
                Either.<Integer, Boolean>right(false).hashCode());
        Assert.assertNotEquals(Either.<Integer, Integer>left(2),
                Either.<Integer, Integer>right(2));
        Assert.assertNotEquals(Either.<Integer, Integer>left(2).hashCode(),
                Either.<Integer, Integer>right(2).hashCode());
    }

    @Test
    public void consistentEquals() {
        //noinspection AssertBetweenInconvertibleTypes
        Assert.assertNotEquals(Either.<Integer, Boolean>left(2), 2);

        Assert.assertEquals(Either.<Integer, Boolean>left(2),
                Either.<Integer, Boolean>left(2));
        Assert.assertEquals(Either.<Integer, Boolean>left(2).hashCode(),
                Either.<Integer, Boolean>left(2).hashCode());
    }

    @Test
    public void iff() {
        Assert.assertTrue(Either.iff(true, () -> 0, () -> 1).isLeft());
        Assert.assertTrue(Either.iff(false, () -> 0, () -> 1).isRight());
        Assert.assertFalse(Either.iff(true, () -> 0, () -> 1).isRight());
        Assert.assertFalse(Either.iff(false, () -> 0, () -> 1).isLeft());
    }

    @Test
    public void leftValues() {
        Assert.assertEquals(List.of(0, 2, 4),
                Either.leftValues(List.of(Either.left(0),
                        Either.right(1),
                        Either.left(2),
                        Either.right(3),
                        Either.left(4))));
    }

    @Test
    public void rightValues() {
        Assert.assertEquals(List.of("1", "2"),
                Either.rightValues(List.of(Either.left(1),
                        Either.right("1"),
                        Either.left(2),
                        Either.right("2"),
                        Either.left(3))));
    }

    @Test
    public void reduce() {
        Assert.assertEquals("0", Either.reduce(Either.left("0")));
        Assert.assertEquals("1", Either.reduce(Either.right("1")));
    }
}
