/*
 * Copyright (c) 2021-2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents a value of 1 of 2 possible types (disjoint union).
 *
 * <p><code>null</code> values are not accepted and will throw exceptions
 * if used in an Either instance.
 * <p>
 * Construct an instance using one of:
 * <ul>
 *     <li>{@link Left}: instance with a left value</li>
 *     <li>{@link Right}: instance with a right value</li>
 *     <li>{@link Either#from(boolean, Supplier, Supplier)}: instance from the evaluation
 *         of a supplied boolean value</li>
 * </ul>
 * <p>
 * This class is not serializable.
 *
 * @param <A> Type of the left value
 * @param <B> Type of the right value
 * @author 2bllw8
 * @since 1.0.0
 */
public sealed interface Either<A, B> permits Left, Right {

    /**
     * @return Returns true if this is a {@link Left}, false otherwise.
     * @since 1.0.0
     */
    boolean isLeft();

    /**
     * @return Returns true if this is a {@link Right}, false otherwise.
     * @since 1.0.0
     */
    boolean isRight();

    /**
     * Returns true if this is a {@link Right} and its value is equal to elem (as determined by
     * {@link Object#equals(Object)}), returns false otherwise.
     *
     * @param elem The element to test.
     * @return <code>true</code> if this is a {@link Right} value equal to <code>elem</code>
     * @since 2.0.0
     */
    boolean contains(B elem);

    /**
     * @return Returns false if {@link Left} or returns the result of the application of the given
     * predicate to the {@link Right} value.
     * @since 2.0.0
     */
    boolean exists(Function<B, Boolean> predicate);

    /**
     * @return Returns {@link Right} with the existing value of {@link Right} if this is a
     * {@link Right} and the given predicate p holds for the right value, or an instance of
     * {@link Left} with fallback as argument if this is a {@link Right} and the given predicate
     * does not hold for the right value, or an instance of {@link Left} with the existing value of
     * {@link Left} if this is a {@link Left}.
     * @since 2.0.0
     */
    Either<A, B> filterOrElse(Function<B, Boolean> predicate, A fallback);

    /**
     * Binds the given function across {@link Right}.
     *
     * @param function The function to bind across {@link Right}.
     * @since 2.0.0
     */
    <B1> Either<A, B1> flatMap(Function<B, Either<A, B1>> function);

    /**
     * Applies functionLeft if this is a {@link Left} or functionRight if this is a {@link Right}.
     *
     * @return Returns the results of applying the function.
     * @since 2.0.0
     */
    <C> C fold(Function<A, C> functionLeft, Function<B, C> functionRight);

    /**
     * @return Returns true if {@link Left} or returns the result of the application of the given
     * predicate to the {@link Right} value.
     * @since 2.0.0
     */
    boolean forAll(Function<B, Boolean> predicate);

    /**
     * Executes the given side-effecting function if this is a {@link Right}.
     *
     * @since 2.0.0
     */
    void forEach(Consumer<B> consumer);

    /**
     * Executes a given side-effecting function depending on whether this is a {@link Left} or
     * {@link Right}.
     *
     * @since 2.1.0
     */
    void forEach(Consumer<A> consumerLeft, Consumer<B> consumerRight);

    /**
     * @return Returns the value from this {@link Right} or the given fallback if this is a
     * {@link Left}.
     * @since 2.0.0
     */
    B getOrElse(B fallback);

    /**
     * The given function is applied if this is a {@link Right}.
     *
     * @since 2.0.0
     */
    <C> Either<A, C> map(Function<B, C> function);

    /**
     * @return Returns this {@link Right} or the given argument if this is a {@link Left}.
     * @since 2.0.0
     */
    Either<A, B> orElse(Either<A, B> alternative);

    /**
     * Allows for-comprehensions over the left side of Either instances, reversing the usual
     * right-bias of the Either class.
     *
     * @return Projects this Either as a {@link Left}.
     * @since 2.0.0
     */
    LeftProjection<A, B> left();

    /**
     * @return Returns a stream containing the right value if this is a {@link Right}, otherwise,
     * {@link Stream#empty()}.
     * @since 2.0.0
     */
    Stream<B> stream();

    /**
     * @return If this is a {@link Left}, then returns the left value in {@link Right} or vice
     * versa.
     * @since 1.0.0
     */
    Either<B, A> swap();

    /**
     * @return Returns a {@link Optional} with the right value if this is a {@link Right},
     * otherwise, {@link Optional#empty()}.
     * @since 2.0.0
     */
    Optional<B> toOptional();

    /**
     * @return If the <code>conditional</code> is <code>true</code> returns a {@link Right} holding
     * the value supplied by <code>ifTrue</code>, otherwise a {@link Left} holding the value
     * supplied by the <code>ifFalse</code>
     * @since 3.4.0
     */
    static <A, B> Either<A, B> from(boolean conditional,
                                    Supplier<B> ifTrue,
                                    Supplier<A> ifFalse) {
        return conditional
                ? new Right<>(ifTrue.get())
                : new Left<>(ifFalse.get());
    }

    /**
     * @return Returns the right value if the given argument is {@link Right} or its value if it is
     * {@link Left}.
     * @since 2.0.0
     */
    static <A, B> Either<A, B> flatten(Either<A, Either<A, B>> either) {
        return switch (either) {
            case Left<A, Either<A, B>> l -> Left.flatten(l);
            case Right<A, Either<A, B>> r -> Right.flatten(r);
        };
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
    static <B, C> Either<C, B> joinLeft(Either<Either<C, B>, B> either) {
        return switch (either) {
            case Left<Either<C, B>, B> l -> Left.joinLeft(l);
            case Right<Either<C, B>, B> r -> Right.joinLeft(r);
        };
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
    static <A, C> Either<A, C> joinRight(Either<A, Either<A, C>> either) {
        return switch (either) {
            case Left<A, Either<A, C>> l -> Left.joinRight(l);
            case Right<A, Either<A, C>> r -> Right.joinRight(r);
        };
    }
}
