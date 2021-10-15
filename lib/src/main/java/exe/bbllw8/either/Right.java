/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The right side of the disjoint union, as opposed to the {@link Left} side.
 *
 * @author 2bllw8
 * @see Either
 * @see Left
 * @since 2.0
 */
public final class Right<A, B> extends Either<A, B> {

    private transient final B value;

    public Right(B value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    A leftValue() {
        throw new NoSuchElementException("This is a Right");
    }

    @Override
    B rightValue() {
        return value;
    }

    /**
     * Up-casts this {@link Right} with another left side type.
     */
    public <A1> Either<A1, B> withLeft() {
        return new Right<>(value);
    }

    @Override
    public String toString() {
        return "Right(" + value + ")";
    }
}
