/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

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
    public boolean contains(B elem) {
        return elem.equals(value);
    }

    @Override
    public boolean exists(Function<? super B, Boolean> predicate) {
        return predicate.apply(value);
    }

    @Override
    public Either<A, B> filterOrElse(Function<? super B, Boolean> predicate, A fallback) {
        return predicate.apply(value)
                ? this
                : new Left<>(fallback);
    }

    @Override
    public <B1> Either<A, B1> flatMap(Function<? super B, Either<A, B1>> function) {
        return function.apply(value);
    }

    @Override
    public <C> C fold(Function<? super A, ? extends C> functionLeft, Function<B, C> functionRight) {
        return functionRight.apply(value);
    }

    @Override
    public boolean forAll(Function<? super B, Boolean> predicate) {
        return predicate.apply(value);
    }

    @Override
    public void forEach(Consumer<? super B> consumer) {
        consumer.accept(value);
    }

    @Override
    public void forEach(Consumer<? super A> consumerLeft, Consumer<? super B> consumerRight) {
        consumerRight.accept(value);
    }

    @Override
    public B getOrElse(B fallback) {
        return value;
    }

    @Override
    public <C> Either<A, C> map(Function<? super B, ? extends C> function) {
        return new Right<>(function.apply(value));
    }

    @Override
    public Either<A, B> orElse(Either<A, B> alternative) {
        return this;
    }

    @Override
    public LeftProjection<A, B> left() {
        return new RightToLeftProjection();
    }

    @Override
    public Stream<B> stream() {
        return Stream.of(value);
    }

    @Override
    public Either<B, A> swap() {
        return new Left<>(value);
    }

    @Override
    public Optional<B> toOptional() {
        return Optional.of(value);
    }

    /**
     * Up-casts this {@link Right} with another left side type.
     */
    public <A1> Either<A1, B> withLeft() {
        return new Right<>(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Right)) {
            return false;
        }
        final Right<?, ?> that = (Right<?, ?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(true, value);
    }

    @Override
    public String toString() {
        return "Right(" + value + ")";
    }

    public static <A, B> Either<A, B> flatten(Right<A, Either<A, B>> either) {
        return either.value;
    }

    public static <B, C> Either<C, B> joinLeft(Right<Either<C, B>, B> either) {
        return either.withLeft();
    }

    public static <A, C> Either<A, C> joinRight(Right<A, Either<A, C>> either) {
        return either.value;
    }

    private final class RightToLeftProjection extends LeftProjection<A, B> {
        @Override
        public boolean exists(Function<? super A, Boolean> predicate) {
            return false;
        }

        @Override
        public Optional<Either<A, B>> filterToOptional(Function<? super A, Boolean> predicate) {
            return Optional.empty();
        }

        @Override
        public <A1> Either<A1, B> flatMap(Function<? super A, Either<A1, B>> function) {
            return withLeft();
        }

        @Override
        public boolean forAll(Function<? super A, Boolean> function) {
            return true;
        }

        @Override
        public void forEach(Consumer<? super A> consumer) {
            // Do nothing
        }

        @Override
        public A getOrElse(A fallback) {
            return fallback;
        }

        @Override
        public <A1> Either<A1, B> map(Function<? super A, ? extends A1> function) {
            return withLeft();
        }

        @Override
        public Stream<A> stream() {
            return Stream.empty();
        }

        @Override
        public Optional<A> toOptional() {
            return Optional.empty();
        }
    }
}
