/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The {@link Try} type represents a computation that may either result in an
 * exception, or return a successfully computed value.
 * It's similar to, but semantically different from the {@link Either} type.
 * <p>
 * Instances of {@link Try}, are either an instance of {@link Success} or
 * {@link Failure}.
 * <p>
 * For example, {@link Try} can be used to perform division on a user-defined
 * input, without the need to do explicit exception-handling in all the places
 * that where an exception might occur.
 * <p>
 * An important property of {@link Try} is its ability to <i>pipeline</i>,
 * or chain, operations, catching exceptions along the way. For example, the
 * {@link Try#flatMap(Function)} and {@link Try#map(Function)} combinators
 * pass off either their successfully completed value, wrapped in the
 * {@link Success} type for it to be further operated upon by the next
 * combinator in the chain, or the exception wrapped in the
 * {@link Failure} type usually to be simply passed on down the chain.
 * <p>
 * Combinators such as {@link Try#recover(Function)} and
 * {@link Try#recoverWith(Function)} are designed to provide some type of
 * default behavior in the case of failure.
 *
 * @author 2bllw8
 * @since 3.0.0
 */
public abstract class Try<T> {

    /**
     * Default package-private constructor.
     *
     * <p>To instantiate this class use one of:
     * <ul>
     *     <li>{@link Success}</li>
     *     <li>{@link Failure}</li>
     *     <li>{@link Try#from(Supplier)}</li>
     * </ul>
     *
     * @hidden
     * @see Success
     * @see Failure
     * @since 3.0.0
     */
    /* package */ Try() {
    }

    /**
     * @return Returns true if the {@link Try} is a {@link Failure},
     * false otherwise.
     * @since 3.0.0
     */
    public abstract boolean isFailure();

    /**
     * @return Returns true if the {@link Try} is a {@link Success},
     * false otherwise.
     * @since 3.0.0
     */
    public abstract boolean isSuccess();

    /**
     * @return Returns the value from this {@link Success} or throws
     * the exception if this is a {@link Failure}.
     * @since 3.0.0
     */
    public abstract T get();

    /**
     * Applies the given function iff this is a {@link Success}.
     *
     * @since 3.0.0
     */
    public abstract void forEach(Consumer<T> consumer);

    /**
     * @return Returns the given function applied to the value from this
     * {@link Success} or returns this if this is a {@link} Failure.
     * @since 3.0.0
     */
    public abstract <U> Try<U> flatMap(Function<T, Try<U>> function);

    /**
     * Maps the given function to the value from this {@link Success}
     * or returns this if this is a {@link Failure}.
     *
     * @since 3.0.0
     */
    public abstract <U> Try<U> map(Function<T, U> function);

    /**
     * Converts this to a {@link Failure} if the predicate is not satisfied.
     *
     * @since 3.0.0
     */
    public abstract Try<T> filter(Function<T, Boolean> predicate);

    /**
     * Applies the given function if this is a {@link Failure},
     * otherwise returns this if this is a {@link Success}.
     * This is like {@link Try#flatMap} for the exception.
     *
     * @since 3.0.0
     */
    public abstract Try<T> recoverWith(Function<Throwable, Try<T>> function);

    /**
     * Applies the given function if this is a {@link Failure},
     * otherwise returns this if this is a {@link Success}.
     * This is like {@link Try#map} for the exception.
     *
     * @since 3.0.0
     */
    public abstract Try<T> recover(Function<Throwable, T> function);

    /**
     * @return Returns {@link Optional#empty()} if this is a
     * {@link Failure} or returns an optional containing the value
     * if this is a {@link Success}.
     * @since 3.0.0
     */
    public abstract Optional<T> tOptional();

    /**
     * @return Returns {@link Left} with {@link Throwable} if this
     * is a {@link Failure}, otherwise returns {@link Right} with
     * {@link Success} value.
     * @since 3.0.0
     */
    public abstract Either<Throwable, T> toEither();

    /**
     * Inverts this {@link Try}. If this is a {@link Failure}, returns
     * its exception wrapped in a {@link Success}.
     * If this is a {@link Success}, returns a {@link Failure}
     * containing an {@link UnsupportedOperationException}.
     *
     * @since 3.0.0
     */
    public abstract Try<Throwable> failed();

    /**
     * Completes this {@link Try} by applying the
     * function failureFunction to this if this is of type {@link Failure},
     * or conversely, by applying successFunction if this is a {@link Success}.
     *
     * @param successFunction the function to apply if this is a {@link Failure}
     * @param failureFunction the function to apply if this is a {@link Success}
     * @since 3.0.0
     */
    public abstract <U> Try<U> transform(Function<T, Try<U>> successFunction,
                                         Function<Throwable, Try<U>> failureFunction);

    /**
     * Applies successFunction if this is a {@link Failure}
     * or failureFunction if this is a {@link Success}.
     * If successFunction is initially applied and throws an exception,
     * then failureFunction is applied with this exception.
     *
     * @param successFunction the function to apply if this is a {@link Failure}
     * @param failureFunction the function to apply if this is a {@link Success}
     * @return Returns the results of applying the function
     * @since 3.0.0
     */
    public abstract <U> U fold(Function<Throwable, U> failureFunction,
                               Function<T, U> successFunction);

    /**
     * Applies the given function to the value from this
     * {@link Success} or returns this if this is a failure.
     *
     * @since 3.0.0
     */
    public abstract T getOrElse(T fallback);

    /**
     * @return Returns this {@link Try} if it's a `Success` or the given `default` argument if this is a `Failure`.
     * @since 3.0.0
     */
    public abstract Try<T> orElse(Try<T> fallback);

    /**
     * Transforms a nested {@link Try}, ie, a {@link Try} of type
     * <code>Try&lt;Try&lt;T&gt;&gt;</code>,
     * into an un-nested {@link Try}, ie, a {@link Try} of type
     * <code>Try&lt;T&gt;</code>.
     *
     * @since 3.0.0
     */
    public static <T> Try<T> flatten(Try<Try<T>> tryTry) {
        return tryTry.isSuccess()
                ? tryTry.get()
                : (Failure<T>) tryTry;
    }

    /**
     * Constructs a {@link Try} from the execution of a given supplier.
     * This method will ensure any non-fatal exception is caught and a
     * {@link Failure} object is returned.
     *
     * @since 3.0.0
     */
    @SuppressWarnings({"PMD.AvoidCatchingThrowable"})
    public static <T> Try<T> from(Supplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (CheckedException e) {
            // Wrapped checked exception
            final Throwable t = e.getCause();
            if (isFatal(t)) {
                // Fatal
                throw e;
            } else {
                return new Failure<>(t);
            }
        } catch (Throwable t) {
            if (isFatal(t)) {
                // Fatal
                throw t;
            } else {
                return new Failure<>(t);
            }
        }
    }

    /**
     * @return Returns whether the given throwable is fatal.
     */
    private static boolean isFatal(Throwable t) {
        return t instanceof VirtualMachineError
                || t instanceof ThreadDeath
                || t instanceof InterruptedException
                || t instanceof LinkageError;
    }
}
