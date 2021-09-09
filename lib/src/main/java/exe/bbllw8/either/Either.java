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
 */
public abstract class Either<L, R> {

    /**
     * Whether this either has a left value.
     * <p>
     * If this is <code>true</code>, {@link #getRight()}
     * must be <code>false</code>.
     */
    public abstract boolean isLeft();

    /**
     * Whether this either has a right value.
     * <p>
     * If this is <code>true</code>, {@link #getLeft()}
     * must be <code>false</code>.
     */
    public abstract boolean isRight();

    /**
     * The left value contained.
     * An exception is thrown if there's no left value.
     *
     * @see #isLeft()
     */
    public abstract L getLeft();

    /**
     * The right value contained.
     * An exception is thrown if there's no right value.
     *
     * @see #isRight()
     */
    public abstract R getRight();

    /**
     * Map either the left or the right value to a specific type
     * and return it.
     */
    public abstract <T> T either(Function<? super L, ? extends T> mapLeft,
                                 Function<? super R, ? extends T> mapRight);

    /**
     * Map the left value (if present).
     */
    public abstract <T> Either<T, R> mapLeft(Function<? super L, ? extends T> mapFunction);

    /**
     * Map the right value (if present).
     */
    public abstract <T> Either<L, T> mapRight(Function<? super R, ? extends T> mapFunction);

    public abstract <A, B> Either<A, B> biMap(Function<? super L, ? extends A> mapLeft,
                                              Function<? super R, ? extends B> mapRight);

    /**
     * Swap the left and right values.
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
     */
    public static <L, R> Either<L, R> left(L value) {
        return new LeftEither<>(Optional.of(value));
    }

    /**
     * Create an Either instance that contains a value on the right.
     */
    public static <L, R> Either<L, R> right(R value) {
        return new RightEither<>(Optional.of(value));
    }

    /**
     * Create an Either instance that contains a left or right
     * value depending on a given condition.
     *
     * @param condition If this condition is <code>true</code>, then the
     *                  Either instance will have a left value, otherwise
     *                  it will have a right value
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
     */
    public static <L, R> Collection<L> leftValues(List<Either<L, R>> list) {
        return Objects.requireNonNull(list).stream()
                .filter(Either::isLeft)
                .map(Either::getLeft)
                .collect(Collectors.toList());
    }

    /**
     * Return all the right values in a list of Either instances.
     */
    public static <L, R> Collection<R> rightValues(List<Either<L, R>> list) {
        return Objects.requireNonNull(list).stream()
                .filter(Either::isRight)
                .map(Either::getRight)
                .collect(Collectors.toList());
    }

    /**
     * Reduce an Either instance that has left and right of the same type.
     */
    public static <T> T reduce(Either<T, T> either) {
        return Objects.requireNonNull(either).either(Function.identity(), Function.identity());
    }

    protected static NoSuchElementException noLeftValueException() {
        return new NoSuchElementException("No left value present");
    }

    protected static NoSuchElementException noRightValueException() {
        return new NoSuchElementException("No right value present");
    }
}
