/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Objects;

/**
 * Represents a function that accepts one argument and produces a result, but may also throw a
 * {@link Throwable}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author 2bllw8
 * @since 3.3.0
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {

    /**
     * Applies this function to the given argument. During the application an exception may be
     * thrown instead of returning the result.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t) throws Throwable;

    /**
     * Returns a composed function that first applies the {@code before} function to its input, and
     * then applies this function to the result. If evaluation of either function throws an
     * exception, it is relayed to the caller of the composed function.
     *
     * @param <V>    the type of input to the {@code before} function, and to the composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before} function and then applies
     * this function
     * @throws NullPointerException if before is null
     * @see #andThen(CheckedFunction)
     */
    default <V> CheckedFunction<V, R> compose(CheckedFunction<V, T> before) {
        Objects.requireNonNull(before);
        return v -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to its input, and then applies
     * the {@code after} function to the result. If evaluation of either function throws an
     * exception, it is relayed to the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the
     * {@code after} function
     * @throws NullPointerException if after is null
     * @see #compose(CheckedFunction)
     */
    default <V> CheckedFunction<T, V> andThen(CheckedFunction<R, V> after) {
        Objects.requireNonNull(after);
        return t -> after.apply(apply(t));
    }
}
