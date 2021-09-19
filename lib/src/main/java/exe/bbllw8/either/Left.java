/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The left side of the disjoint union, as opposed to the {@link Right} side.
 *
 * @author 2bllw8
 * @see Either
 * @see Right
 * @since 2.0
 */
public final class Left<A, B> extends Either<A, B> {

    private transient final A value;

    public Left(A value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    protected A leftValue() {
        return value;
    }

    @Override
    protected B rightValue() {
        throw new NoSuchElementException("This is not a Right");
    }

    /**
     * Up-casts this {@link Left} with another right side type.
     */
    public <B1> Either<A, B1> withRight() {
        return new Left<>(value);
    }

    @Override
    public String toString() {
        return "Left(" + value + ")";
    }
}
