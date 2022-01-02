/*
 * Copyright (c) 2021-2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

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
    public boolean contains(B elem) {
        return false;
    }

    @Override
    public boolean exists(Function<? super B, Boolean> predicate) {
        return false;
    }

    @Override
    public Either<A, B> filterOrElse(Function<? super B, Boolean> predicate, A fallback) {
        return this;
    }

    @Override
    public <B1> Either<A, B1> flatMap(Function<? super B, Either<A, B1>> function) {
        return withRight();
    }

    @Override
    public <C> C fold(Function<? super A, ? extends C> functionLeft, Function<B, C> functionRight) {
        return functionLeft.apply(value);
    }

    @Override
    public boolean forAll(Function<? super B, Boolean> predicate) {
        return true;
    }

    @Override
    public void forEach(Consumer<? super B> consumer) {
        // Do nothing
    }

    @Override
    public void forEach(Consumer<? super A> consumerLeft, Consumer<? super B> consumerRight) {
        consumerLeft.accept(value);
    }

    @Override
    public B getOrElse(B fallback) {
        return fallback;
    }

    @Override
    public <C> Either<A, C> map(Function<? super B, ? extends C> function) {
        return withRight();
    }

    @Override
    public Either<A, B> orElse(Either<A, B> alternative) {
        return alternative;
    }

    @Override
    public LeftProjection<A, B> left() {
        return new LeftToLeftProjection();
    }

    @Override
    public Stream<B> stream() {
        return Stream.empty();
    }

    @Override
    public Either<B, A> swap() {
        return new Right<>(value);
    }

    @Override
    public Optional<B> toOptional() {
        return Optional.empty();
    }

    /**
     * Up-casts this {@link Left} with another right side type.
     */
    public <B1> Either<A, B1> withRight() {
        return new Left<>(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Left)) {
            return false;
        }
        final Left<?, ?> that = (Left<?, ?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Left.class, value);
    }

    @Override
    public String toString() {
        return "Left(" + value + ")";
    }

    public static <A, B> Either<A, B> flatten(Left<A, Either<A, B>> either) {
        return either.withRight();
    }

    public static <B, C> Either<C, B> joinLeft(Left<Either<C, B>, B> either) {
        return either.value;
    }

    public static <A, C> Either<A, C> joinRight(Left<A, Either<A, C>> either) {
        return either.withRight();
    }

    private final class LeftToLeftProjection extends LeftProjection<A, B> {
        @Override
        public boolean exists(Function<? super A, Boolean> predicate) {
            return predicate.apply(value);
        }

        @Override
        public Optional<Either<A, B>> filterToOptional(Function<? super A, Boolean> predicate) {
            return predicate.apply(value)
                    ? Optional.of(Left.this)
                    : Optional.empty();
        }

        @Override
        public <A1> Either<A1, B> flatMap(Function<? super A, Either<A1, B>> function) {
            return function.apply(value);
        }

        @Override
        public boolean forAll(Function<? super A, Boolean> function) {
            return function.apply(value);
        }

        @Override
        public void forEach(Consumer<? super A> consumer) {
            consumer.accept(value);
        }

        @Override
        public A getOrElse(A fallback) {
            return value;
        }

        @Override
        public <A1> Either<A1, B> map(Function<? super A, ? extends A1> function) {
            return new Left<>(function.apply(value));
        }

        @Override
        public Stream<A> stream() {
            return Stream.of(value);
        }

        @Override
        public Optional<A> toOptional() {
            return Optional.of(value);
        }
    }
}
