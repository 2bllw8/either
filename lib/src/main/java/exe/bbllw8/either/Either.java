/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Represents a value of 1 of 2 possible types (disjoint union).
 *
 * <code>null</code> values are not accepted and will throw exceptions
 * if used in an Either instance.
 * <p>
 * Construct an instance using one of:
 * <ul>
 *     <li>{@link #left(Object)}: instance with a left value</li>
 *     <li>{@link #left(Object)}: instance with a left value</li>
 *     <li>{@link #iff(boolean, Supplier, Supplier)}: instance with a left or
 *         value depending on the value of a condition</li>
 * </ul>
 *
 * @param <L> Type of the left value
 * @param <R> Type of the right value
 * @author 2bllw8
 * @since 1.0
 */
public abstract class Either<L, R> {

    /**
     * Default package-private constructor.
     *
     * <p>To instantiate this class use one of:
     * <ul>
     *     <li>{@link #left(Object)}</li>
     *     <li>{@link #right(Object)}</li>
     *     <li>{@link #iff(boolean, Supplier, Supplier)}</li>
     * </ul>
     *
     * @hidden
     * @see #left(Object)
     * @see #right(Object)
     * @see #iff(boolean, Supplier, Supplier)
     * @since 1.1
     */
    /* package */ Either() {
    }

    /**
     * Whether this either has a left value.
     * <p>
     * If this is <code>true</code>, {@link #isRight()}
     * must be <code>false</code> and {@link #getLeft()}
     * is guaranteed to not throw any exception.
     *
     * @return whether this either has a left value
     * @see #isRight()
     * @see #getLeft()
     * @since 1.0
     */
    public abstract boolean isLeft();

    /**
     * Whether this either has a right value.
     * <p>
     * If this is <code>true</code>, {@link #isLeft()}
     * must be <code>false</code> and {@link #getRight()}
     * is guaranteed to not throw any exception.
     *
     * @return whether this either has a right value
     * @see #isLeft()
     * @see #getRight()
     * @since 1.0
     */
    public abstract boolean isRight();

    /**
     * The left value contained.
     * An exception is thrown if there's no left value
     * ({@link #isLeft()} is false).
     *
     * @return The left value
     * @see #isLeft()
     * @since 1.0
     * @throws NoSuchElementException if there's no left value
     */
    public abstract L getLeft();

    /**
     * The right value contained.
     * An exception is thrown if there's no right value
     * ({@link #isRight()} is false).
     *
     * @return The right value
     * @see #isRight()
     * @since 1.0
     * @throws NoSuchElementException if there's no right value
     */
    public abstract R getRight();

    /**
     * Map either the left or the right value to a specific type
     * and return it.
     *
     * @param <T> The expected return type to which the value is mapped
     * @param mapLeft Function that maps the left value (if present) to
     *                the desired type
     * @param mapRight Function that maps the right value (if present) to
     *                 the desired type
     * @return The value contained in the Either instanced mapped to another type
     * @since 1.0
     */
    public abstract <T> T either(Function<? super L, ? extends T> mapLeft,
                                 Function<? super R, ? extends T> mapRight);

    /**
     * Map the left value (if present).
     *
     * @param <T> New type for the left value
     * @param mapFunction The function that maps the left value to
     *                    the desired type
     * @return An instance of Either with the left value mapped or
     *         the right value unchanged
     * @see #mapRight(Function)
     * @see #biMap(Function, Function)
     * @since 1.0
     */
    public abstract <T> Either<T, R> mapLeft(Function<? super L, ? extends T> mapFunction);

    /**
     * Map the right value (if present).
     *
     * @param <T> New type for the right value
     * @param mapFunction The function that maps the right value to
     *                    the desired type
     * @return An instance of Either with the right value mapped or
     *         the left value unchanged
     * @see #mapLeft(Function)
     * @see #biMap(Function, Function)
     * @since 1.0
     */
    public abstract <T> Either<L, T> mapRight(Function<? super R, ? extends T> mapFunction);

    /**
     * Transform either the left or right value but both the types
     * of this Either instance.
     *
     * @param mapLeft The function that maps the left value to
     *                the desired type
     * @param mapRight The function that maps the right value to
     *                 the desired type
     * @param <A> New type for the left value
     * @param <B> New type for the right value
     * @return An instance of Either with either the left or right
     *         value mapped, but both types changed
     * @see #mapLeft(Function)
     * @see #mapRight(Function)
     * @since 1.0
     */
    public abstract <A, B> Either<A, B> biMap(Function<? super L, ? extends A> mapLeft,
                                              Function<? super R, ? extends B> mapRight);

    /**
     * Swap the left and right values.
     *
     * @return An instance of Either with the left and right values
     *         and types swapped
     * @since 1.0
     */
    public final Either<R, L> swap() {
        return isLeft()
                ? right(getLeft())
                : left(getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isLeft() ? getLeft() : getRight(), isLeft());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Either<?, ?>) {
            final Either<?, ?> that = (Either<?, ?>) obj;
            return isLeft()
                    ? that.isLeft() && getLeft().equals(that.getLeft())
                    : that.isRight() && getRight().equals(that.getRight());
        } else {
            return false;
        }
    }

    /**
     * Create an Either instance that contains a value on the left.
     *
     * @param <L> Type of the left value
     * @param <R> Type of the (nil) right value
     * @param value The left value
     * @return An instance of Either that has a left value
     * @see #right(Object)
     * @since 1.0
     */
    public static <L, R> Either<L, R> left(L value) {
        return new LeftEither<>(Optional.of(value));
    }

    /**
     * Create an Either instance that contains a value on the right.
     *
     * @param <L> Type of the (nil) left value
     * @param <R> Type of the right value
     * @param value The right value
     * @return An instance of Either that has a right value
     * @see #left(Object)
     * @since 1.0
     */
    public static <L, R> Either<L, R> right(R value) {
        return new RightEither<>(Optional.of(value));
    }

    /**
     * Create an Either instance that contains a left or right
     * value depending on a given condition.
     *
     * @param <L> Type of the left value
     * @param <R> Type of the right value
     * @param condition If this condition is <code>true</code>, then the
     *                  Either instance will have a left value, otherwise
     *                  it will have a right value
     * @param leftSupplier Supplier of the left value, invoked only if necessary
     * @param rightSupplier Supplier of the right value, invoked only if necessary
     * @return An instance of Either that has a value depending on a
     *         given condition
     * @see #left(Object)
     * @see #right(Object)
     * @since 1.0
     */
    public static <L, R> Either<L, R> iff(boolean condition,
                                          Supplier<? extends L> leftSupplier,
                                          Supplier<? extends R> rightSupplier) {
        return condition
                ? left(leftSupplier.get())
                : right(rightSupplier.get());
    }

    /**
     * Return all the left values in a list of Either instances.
     *
     * @param <L> Type of the left value
     * @param <R> Type of the right value
     * @param list A list of instances of Either
     * @return All the left values from the given list of instances of Either
     * @since 1.0
     */
    public static <L, R> Collection<L> leftValues(List<Either<L, R>> list) {
        return Objects.requireNonNull(list).stream()
                .filter(Either::isLeft)
                .map(Either::getLeft)
                .collect(Collectors.toList());
    }

    /**
     * Return all the right values in a list of Either instances.
     *
     * @param <L> Type of the left value
     * @param <R> Type of the right value
     * @param list A list of instances of Either
     * @return All the right values from the given list of instances of Either
     * @since 1.0
     */
    public static <L, R> Collection<R> rightValues(List<Either<L, R>> list) {
        return Objects.requireNonNull(list).stream()
                .filter(Either::isRight)
                .map(Either::getRight)
                .collect(Collectors.toList());
    }

    /**
     * Reduce an Either instance that has left and right of the
     * same type.
     *
     * @param <T> Type of the value that will be reduced from the
     *           instance of Either
     * @param either An instance of Either that has both the left and
     *               right values of the same type
     * @return The value contained within the given instance of Either.
     * @since 1.0
     */
    public static <T> T reduce(Either<T, T> either) {
        return Objects.requireNonNull(either).either(Function.identity(), Function.identity());
    }

    /**
     * @return An exception for that's thrown when the requested
     *         left value is not present
     * @hidden
     */
    protected static NoSuchElementException noLeftValueException() {
        return new NoSuchElementException("No left value present");
    }

    /**
     * @return An exception for that's thrown when the requested
     *         right value is not present
     * @hidden
     */
    protected static NoSuchElementException noRightValueException() {
        return new NoSuchElementException("No right value present");
    }
}
