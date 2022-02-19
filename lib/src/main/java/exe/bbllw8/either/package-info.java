/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * This package provides the Either and Try types.
 * <p>
 * Either is a type that represents a value of one of two possible types (disjoint union). An
 * instance of {@link exe.bbllw8.either.Either} is an instance of <i>either</i> {@link
 * exe.bbllw8.either.Left} or {@link exe.bbllw8.either.Right}.
 * </p>
 *
 * <p>
 * The {@link exe.bbllw8.either.Try} type represents a computation that may either result in an
 * exception, or return a successfully computed value. It's similar to, but semantically different
 * from the {@link exe.bbllw8.either.Either} type. Instances of {@link exe.bbllw8.either.Try}, are
 * either an instance of {@link exe.bbllw8.either.Success} or {@link exe.bbllw8.either.Failure}.
 * </p>
 *
 * @author 2bllw8
 * @since 1.0.0
 */
package exe.bbllw8.either;
