/*
 * Copyright (c) 2021-2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
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
 * </ul>
 * <p>
 * This class is not serializable.
 *
 * @param <A> Type of the left value
 * @param <B> Type of the right value
 * @author 2bllw8
 * @since 1.0.0
 */
public abstract class Either<A, B> {

    /**
     * Default package-private constructor.
     *
     * <p>To instantiate this class use one of:
     * <ul>
     *     <li>{@link Left}</li>
     *     <li>{@link Right}</li>
     * </ul>
     *
     * @hidden
     * @see Left
     * @see Right
     * @since 1.1.0
     */
    /* package */ Either() {
    }

    /**
     * @return Returns true if this is a {@link Left}, false otherwise.
     * @since 1.0.0
     */
    public abstract boolean isLeft();

    /**
     * @return Returns true if this is a {@link Right}, false otherwise.
     * @since 1.0.0
     */
    public abstract boolean isRight();

    /**
     * Returns true if this is a {@link Right} and its value is equal to elem (as determined by
     * {@link Object#equals(Object)}), returns false otherwise.
     *
     * @param elem The element to test.
     * @return <code>true</code> if this is a {@link Right} value equal to <code>elem</code>
     * @since 2.0.0
     */
    public abstract boolean contains(B elem);

    /**
     * @return Returns false if {@link Left} or returns the result of the application of the given
     * predicate to the {@link Right} value.
     * @since 2.0.0
     */
    public abstract boolean exists(Function<B, Boolean> predicate);

    /**
     * @return Returns {@link Right} with the existing value of {@link Right} if this is a
     * {@link Right} and the given predicate p holds for the right value, or an instance of
     * {@link Left} with fallback as argument if this is a {@link Right} and the given predicate
     * does not hold for the right value, or an instance of {@link Left} with the existing value of
     * {@link Left} if this is a {@link Left}.
     * @since 2.0.0
     */
    public abstract Either<A, B> filterOrElse(Function<B, Boolean> predicate, A fallback);

    /**
     * Binds the given function across {@link Right}.
     *
     * @param function The function to bind across {@link Right}.
     * @since 2.0.0
     */
    public abstract <B1> Either<A, B1> flatMap(Function<B, Either<A, B1>> function);

    /**
     * Applies functionLeft if this is a {@link Left} or functionRight if this is a {@link Right}.
     *
     * @return Returns the results of applying the function.
     * @since 2.0.0
     */
    public abstract <C> C fold(Function<A, C> functionLeft, Function<B, C> functionRight);

    /**
     * @return Returns true if {@link Left} or returns the result of the application of the given
     * predicate to the {@link Right} value.
     * @since 2.0.0
     */
    public abstract boolean forAll(Function<B, Boolean> predicate);

    /**
     * Executes the given side-effecting function if this is a {@link Right}.
     *
     * @since 2.0.0
     */
    public abstract void forEach(Consumer<B> consumer);

    /**
     * Executes a given side-effecting function depending on whether this is a {@link Left} or
     * {@link Right}.
     *
     * @since 2.1.0
     */
    public abstract void forEach(Consumer<A> consumerLeft, Consumer<B> consumerRight);

    /**
     * @return Returns the value from this {@link Right} or the given fallback if this is a
     * {@link Left}.
     * @since 2.0.0
     */
    public abstract B getOrElse(B fallback);

    /**
     * The given function is applied if this is a {@link Right}.
     *
     * @since 2.0.0
     */
    public abstract <C> Either<A, C> map(Function<B, C> function);

    /**
     * @return Returns this {@link Right} or the given argument if this is a {@link Left}.
     * @since 2.0.0
     */
    public abstract Either<A, B> orElse(Either<A, B> alternative);

    /**
     * Allows for-comprehensions over the left side of Either instances, reversing the usual
     * right-bias of the Either class.
     *
     * @return Projects this Either as a {@link Left}.
     * @since 2.0.0
     */
    public abstract LeftProjection<A, B> left();

    /**
     * @return Returns a stream containing the right value if this is a {@link Right}, otherwise,
     * {@link Stream#empty()}.
     * @since 2.0.0
     */
    public abstract Stream<B> stream();

    /**
     * @return If this is a {@link Left}, then returns the left value in {@link Right} or vice
     * versa.
     * @since 1.0.0
     */
    public abstract Either<B, A> swap();

    /**
     * @return Returns a {@link Optional} with the right value if this is a {@link Right},
     * otherwise, {@link Optional#empty()}.
     * @since 2.0.0
     */
    public abstract Optional<B> toOptional();

    /**
     * @return Returns the right value if the given argument is {@link Right} or its value if it is
     * {@link Left}.
     * @since 2.0.0
     */
    public static <A, B> Either<A, B> flatten(Either<A, Either<A, B>> either) {
        if (either instanceof Left<?, ?>) {
            return Left.flatten((Left<A, Either<A, B>>) either);
        } else if (either instanceof Right<?, ?>) {
            return Right.flatten((Right<A, Either<A, B>>) either);
        } else {
            // Should never happen
            throw new IllegalStateException();
        }
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
     * @since 2.0.0
     */
    public static <B, C> Either<C, B> joinLeft(Either<Either<C, B>, B> either) {
        if (either instanceof Left<?, ?>) {
            return Left.joinLeft((Left<Either<C, B>, B>) either);
        } else if (either instanceof Right<?, ?>) {
            return Right.joinLeft((Right<Either<C, B>, B>) either);
        } else {
            // Should never happen
            throw new IllegalStateException();
        }
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
     * @since 2.0.0
     */
    public static <A, C> Either<A, C> joinRight(Either<A, Either<A, C>> either) {
        if (either instanceof Left<?, ?>) {
            return Left.joinRight((Left<A, Either<A, C>>) either);
        } else if (either instanceof Right<?, ?>) {
            return Right.joinRight((Right<A, Either<A, C>>) either);
        } else {
            // Should never happen
            throw new IllegalStateException();
        }
    }
}
