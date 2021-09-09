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
 * Implementation of {@link Either} that only has a right value.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
final class RightEither<L, R> extends Either<L, R> {

    private final Optional<R> right;

    public RightEither(Optional<R> right) {
        super();
        this.right = Objects.requireNonNull(right);
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
    public L getLeft() {
        throw new NoSuchElementException("There is no left value");
    }

    @Override
    public R getRight() {
        return right.orElseThrow();
    }

    @Override
    public <T> T either(Function<? super L, ? extends T> mapLeft,
                        Function<? super R, ? extends T> mapRight) {
        return right.map(mapRight).orElseThrow();
    }

    @Override
    public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> mapFunction) {
        return new RightEither<>(right);
    }

    @Override
    public <T> Either<L, T> mapRight(Function<? super R, ? extends T> mapFunction) {
        return new RightEither<>(right.map(mapFunction));
    }

    @Override
    public <A, B> Either<A, B> biMap(Function<? super L, ? extends A> mapLeft,
                                     Function<? super R, ? extends B> mapRight) {
        return new RightEither<>(right.map(mapRight));
    }
}
