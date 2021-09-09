/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Implementation of {@link Either} that only has a left value.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
final class LeftEither<L, R> extends Either<L, R> {

    private final Optional<L> left;

    public LeftEither(Optional<L> left) {
        super();
        this.left = Objects.requireNonNull(left);
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
    public L getLeft() {
        return left.orElseThrow();
    }

    @Override
    public R getRight() {
        throw new NoSuchElementException("There's no right value");
    }

    @Override
    public <T> T either(Function<? super L, ? extends T> mapLeft,
                        Function<? super R, ? extends T> mapRight) {
        return left.map(mapLeft).orElseThrow();
    }

    @Override
    public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> mapFunction) {
        return new LeftEither<>(left.map(mapFunction));
    }

    @Override
    public <T> Either<L, T> mapRight(Function<? super R, ? extends T> mapFunction) {
        return new LeftEither<>(left);
    }

    @Override
    public <A, B> Either<A, B> biMap(Function<? super L, ? extends A> mapLeft,
                                     Function<? super R, ? extends B> mapRight) {
        return new LeftEither<>(left.map(mapLeft));
    }
}
