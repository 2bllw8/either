/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

/**
 * Represents a supplier of results that may throw a {@link Throwable}.
 *
 * @param <T> The type of result supplied by this supplier
 * @author 2bllw8
 * @since 3.2.0
 */
@FunctionalInterface
public interface CheckedSupplier<T> {

    T get() throws Throwable;
}
