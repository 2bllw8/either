/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import org.junit.Assert;
import org.junit.Test;

public final class EitherTest {

    @Test
    public void from() {
        Assert.assertTrue("Should return a Right if the conditional supplies a true value",
                Either.from(true, () -> 0, () -> 0) instanceof Right);
        Assert.assertTrue("Should return a Left if the conditional supplies a false value",
                Either.from(false, () -> 0, () -> 0) instanceof Left);
        Assert.assertEquals("Should return the value supplied by the ifTrue parameter",
                new Right<>(1),
                Either.from(true, () -> 1, () -> 0));
        Assert.assertEquals("Should return the value supplied by the ifFalse parameter",
                new Left<>(0),
                Either.from(false, () -> 1, () -> 0));
    }
}
