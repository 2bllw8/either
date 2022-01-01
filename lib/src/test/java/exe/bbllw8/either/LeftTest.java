/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import org.junit.Assert;
import org.junit.Test;

public class LeftTest {

    @Test
    public void stringRepresentation() {
        Assert.assertEquals("Left(12)",
                new Left<>(12).toString());
    }
}
