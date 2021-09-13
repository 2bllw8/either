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
 * Projects an Either into a {@link Left}.
 *
 * @author 2bllw8
 * @see Either#left()
 * @see Left
 * @since 2.0
 */
public final class LeftProjection<A, B> {

    private transient final Either<A, B> either;

    /* package */ LeftProjection(Either<A, B> either) {
        this.either = Objects.requireNonNull(either);
    }

    /**
     * @return Returns false if {@link Right} or returns the result of the
     * application of the given function to the {@link Left} value.
     * @since 2.0
     */
    public boolean exists(Function<? super A, Boolean> predicate) {
        return either.isLeft() && Objects.requireNonNull(predicate).apply(either.leftValue());
    }

    /**
     * @return Returns {@link Optional#empty()} if this is a {@link Right}
     * or if the given predicate p does not hold for the left value,
     * otherwise, returns a {@link Left}.
     * @since 2.0
     */
    public Optional<Either<A, B>> filterToOptional(Function<? super A, Boolean> predicate) {
        return either.isLeft() && Objects.requireNonNull(predicate).apply(either.leftValue())
                ? Optional.of(new Left<>(either.leftValue()))
                : Optional.empty();
    }

    /**
     * @return Binds the given function across {@link Left}.
     * @since 2.0
     */
    public <A1> Either<A1, B> flatMap(Function<? super A, Either<A1, B>> function) {
        return either.isLeft()
                ? Objects.requireNonNull(function).apply(either.leftValue())
                : new Right<>(either.rightValue());
    }

    /**
     * @return Returns true if {@link Right} or returns the result of
     * the application of the given function to the Right value.
     * @since 2.0
     */
    public boolean forAll(Function<? super A, Boolean> function) {
        return either.isRight() || Objects.requireNonNull(function).apply(either.leftValue());
    }

    /**
     * Executes the given side-effecting function if this is a {@link Left}.
     *
     * @since 2.0
     */
    public void forEach(Consumer<? super A> consumer) {
        if (either.isLeft()) {
            Objects.requireNonNull(consumer).accept(either.leftValue());
        }
    }

    /**
     * @return Returns the value from this {@link Left} or the given
     * argument if this is a {@link Right}.
     * @since 2.0
     */
    public A getOrElse(A fallback) {
        return either.isLeft()
                ? either.leftValue()
                : Objects.requireNonNull(fallback);
    }

    /**
     * The given function is applied if this is a {@link Left}.
     *
     * @since 2.0
     */
    public <A1> Either<A1, B> map(Function<? super A, ? extends A1> function) {
        return either.isLeft()
                ? new Left<>(Objects.requireNonNull(function).apply(either.leftValue()))
                : new Right<>(either.rightValue());
    }

    /**
     * @return Returns a stream containing the {@link Left} value if
     * it exists or {@link Stream#empty()} if this is a {@link Right}.
     * @since 2.0
     */
    public Stream<A> stream() {
        return either.isLeft()
                ? Stream.of(either.leftValue())
                : Stream.empty();
    }

    /**
     * @return Returns an {@link Optional} containing the {@link Left} value if
     * it exists or {@link Optional#empty()} if this is a {@link Right}.
     * @since 2.0
     */
    public Optional<A> toOptional() {
        return either.isLeft()
                ? Optional.of(either.leftValue())
                : Optional.empty();
    }
}
