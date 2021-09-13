/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents a value of 1 of 2 possible types (disjoint union).
 *
 * <code>null</code> values are not accepted and will throw exceptions
 * if used in an Either instance.
 * <p>
 * Construct an instance using one of:
 * <ul>
 *     <li>{@link Left}: instance with a left value</li>
 *     <li>{@link Right}: instance with a right value</li>
 *     <li>{@link Either#fromTry(Supplier, Class)}: instance from the result
 *         of a function that may throw an exception</li>
 * </ul>
 *
 * @param <A> Type of the left value
 * @param <B> Type of the right value
 * @author 2bllw8
 * @since 1.0
 */
public abstract class Either<A, B> {

    /**
     * Default package-private constructor.
     *
     * <p>To instantiate this class use one of:
     * <ul>
     *     <li>{@link Left}</li>
     *     <li>{@link Right}</li>
     *     <li>{@link Either#fromTry(Supplier, Class)}</li>
     * </ul>
     *
     * @hidden
     * @see Left
     * @see Right
     * @since 1.1
     */
    /* package */ Either() {
    }

    /**
     * @return Returns true if this is a {@link Left}, false otherwise.
     * @since 1.0
     */
    public abstract boolean isLeft();

    /**
     * @return Returns true if this is a {@link Right}, false otherwise.
     * @since 1.0
     */
    public abstract boolean isRight();

    /**
     * @return The contained left value
     * @throws NoSuchElementException if this is a {@link Right}
     * @hidden
     * @since 2.0
     */
    protected abstract A leftValue();

    /**
     * @return The contained right value
     * @throws NoSuchElementException if this is a {@link Left}
     * @hidden
     * @since 2.0
     */
    protected abstract B rightValue();

    /**
     * Returns true if this is a {@link Right} and its value is
     * equal to elem (as determined by {@link Object#equals(Object)}),
     * returns false otherwise.
     *
     * @param elem The element to test.
     * @return <code>true</code> if this is a {@link Right} value equal to <code>elem</code>
     * @since 2.0
     */
    public final boolean contains(B elem) {
        return isRight() && Objects.requireNonNull(elem).equals(rightValue());
    }

    /**
     * @return Returns false if {@link Left} or returns the result of the application
     * of the given predicate to the {@link Right} value.
     * @since 2.0
     */
    public final boolean exists(Function<? super B, Boolean> predicate) {
        return isRight() && Objects.requireNonNull(predicate).apply(rightValue());
    }

    /**
     * @return Returns {@link Right} with the existing value of {@link Right} if this is a {@link Right}
     * and the given predicate p holds for the right value, or an instance of
     * {@link Left} with fallback as argument if this is a {@link Right}
     * and the given predicate does not hold for the right value, or an instance of
     * {@link Left} with the existing value of {@link Left} if this is a {@link Left}.
     * @since 2.0
     */
    public final Either<A, B> filterOrElse(Function<? super B, Boolean> predicate, A fallback) {
        if (isRight()) {
            final B value = rightValue();
            return Objects.requireNonNull(predicate).apply(value)
                    ? new Right<>(value)
                    : new Left<>(fallback);
        } else {
            return new Left<>(leftValue());
        }
    }

    /**
     * Binds the given function across {@link Right}.
     *
     * @param function The function to bind across {@link Right}.
     * @since 2.0
     */
    public final <B1> Either<A, B1> flatMap(Function<? super B, Either<A, B1>> function) {
        return isRight()
                ? Objects.requireNonNull(function).apply(rightValue())
                : new Left<>(leftValue());
    }

    /**
     * Applies functionLeft if this is a {@link Left} or
     * functionRight if this is a {@link Right}.
     *
     * @return Returns the results of applying the function.
     * @since 2.0
     */
    public final <C> C fold(Function<? super A, ? extends C> functionLeft, Function<B, C> functionRight) {
        return isLeft()
                ? Objects.requireNonNull(functionLeft).apply(leftValue())
                : Objects.requireNonNull(functionRight).apply(rightValue());
    }

    /**
     * @return Returns true if {@link Left} or returns the result of the application
     * of the given predicate to the {@link Right} value.
     * @since 2.0
     */
    public final boolean forAll(Function<? super B, Boolean> predicate) {
        return isLeft() || Objects.requireNonNull(predicate).apply(rightValue());
    }

    /**
     * Executes the given side-effecting function if this is a {@link Right}.
     *
     * @since 2.0
     */
    public final void forEach(Consumer<? super B> consumer) {
        if (isRight()) {
            Objects.requireNonNull(consumer).accept(rightValue());
        }
    }

    /**
     * @return Returns the value from this {@link Right} or the given
     * fallback if this is a {@link Left}.
     * @since 2.0
     */
    public final B getOrElse(B fallback) {
        return isRight()
                ? rightValue()
                : fallback;
    }

    /**
     * The given function is applied if this is a {@link Right}.
     *
     * @since 2.0
     */
    public final <C> Either<A, C> map(Function<? super B, ? extends C> function) {
        return isRight()
                ? new Right<>(Objects.requireNonNull(function).apply(rightValue()))
                : new Left<>(leftValue());
    }

    /**
     * @return Returns this {@link Right} or the given argument if this
     * is a {@link Left}.
     * @since 2.0
     */
    public final Either<A, B> orElse(Either<A, B> alternative) {
        return isRight()
                ? new Right<>(rightValue())
                : Objects.requireNonNull(alternative);
    }

    /**
     * Allows for-comprehensions over the left side of Either instances,
     * reversing the usual right-bias of the Either class.
     *
     * @return Projects this Either as a {@link Left}.
     * @since 2.0
     */
    public final LeftProjection<A, B> left() {
        return new LeftProjection<>(this);
    }

    /**
     * @return Returns a stream containing the right value if this is a {@link Right},
     * otherwise, {@link Stream#empty()}.
     * @since 2.0
     */
    public final Stream<B> stream() {
        return isRight()
                ? Stream.of(rightValue())
                : Stream.empty();
    }

    /**
     * @return If this is a {@link Left}, then returns the left value in
     * {@link Right} or vice versa.
     * @since 1.0
     */
    public final Either<B, A> swap() {
        return isLeft()
                ? new Right<>(leftValue())
                : new Left<>(rightValue());
    }

    /**
     * @return Returns a {@link Optional} with the right value if this is a {@link Right},
     * otherwise, {@link Optional#empty()}.
     * @since 2.0
     */
    public Optional<B> toOptional() {
        return isRight()
                ? Optional.of(rightValue())
                : Optional.empty();
    }

    /**
     * @return Returns the right value if the given argument is {@link Right} or
     * its value if it is {@link Left}.
     * @since 2.0
     */
    public static <A, B> Either<A, B> flatten(Either<A, Either<A, B>> either) {
        return either.isRight()
                ? either.rightValue()
                : new Left<>(either.leftValue());
    }

    /**
     * Joins an Either through {@link Left}.
     *
     * <p>This method requires that the left side of this Either is itself an Either type.
     * That is, this must be some type like:
     * <code>Either&lt;Either&lt;C, B&gt;, B&gt;</code></p>
     *
     * <p>If this instance is a {@link Left}&lt;Either&lt;C, B&gt;&gt; then the contained
     * Either&lt;C, B&gt; will be returned, otherwise this value will be returned unmodified.</p>
     *
     * @since 2.0
     */
    public static <B, C> Either<C, B> joinLeft(Either<Either<C, B>, B> either) {
        return Objects.requireNonNull(either).isLeft()
                ? either.leftValue()
                : new Right<>(either.rightValue());
    }

    /**
     * Joins an Either through {@link Right}.
     *
     * <p>This method requires that the left side of this Either is itself an Either type.
     * That is, this must be some type like:
     * <code>Either&lt;A, Either&lt;A, C&gt;&gt;</code></p>
     *
     * <p>If this instance is a {@link Right}&lt;Either&lt;A, C&gt;&gt; then the contained
     * Either&lt;A, C&gt; will be returned, otherwise this value will be returned unmodified.</p>
     *
     * @since 2.0
     */
    public static <A, C> Either<A, C> joinRight(Either<A, Either<A, C>> either) {
        return Objects.requireNonNull(either).isRight()
                ? either.rightValue()
                : new Left<>(either.leftValue());
    }

    /**
     * @return An Either that is {@link Right} with the return value of the given function
     * or {@link Left} with an exception thrown during the execution of the function.
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    public static <A extends Exception, B> Either<A, B> fromTry(Supplier<B> supplier, Class<A> leftClass) {
        try {
            return new Right<>(Objects.requireNonNull(supplier).get());
        } catch (Exception e) {
            if (Objects.requireNonNull(leftClass).isAssignableFrom(e.getClass())) {
                return new Left<>((A) e);
            } else {
                // Unexpected exception, throw it up the stack
                throw e;
            }
        }
    }


    @Override
    public int hashCode() {
        return isRight()
                ? Objects.hash(true, rightValue())
                : Objects.hash(false, leftValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Either<?, ?>)) {
            return false;
        } else {
            final Either<?, ?> that = (Either<?, ?>) obj;
            if (isRight()) {
                return that.isRight() && that.rightValue().equals(rightValue());
            } else {
                return that.isLeft() && that.leftValue().equals(leftValue());
            }
        }
    }
}
