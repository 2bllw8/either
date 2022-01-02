/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A failure that occurred during a computation
 * wrapped in a {@link Try} type.
 * Holds a {@link Throwable} object that represents
 * the failure point.
 *
 * @author 2bllw8
 * @since 3.0.0
 */
public final class Failure<T> extends Try<T> {
    private transient final Throwable throwable;

    public Failure(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T get() {
        throw new UnsupportedOperationException("Failure.get");
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        // Do nothing
    }

    @Override
    public <U> Try<U> flatMap(Function<T, Try<U>> function) {
        return new Failure<>(throwable);
    }

    @Override
    public <U> Try<U> map(Function<T, U> function) {
        return new Failure<>(throwable);
    }

    @Override
    public Try<T> filter(Function<T, Boolean> predicate) {
        return this;
    }

    @Override
    public Try<T> recoverWith(Function<Throwable, Try<T>> function) {
        return function.apply(throwable);
    }

    @Override
    public Try<T> recover(Function<Throwable, T> function) {
        return Try.from(() -> function.apply(throwable));
    }

    @Override
    public Optional<T> tOptional() {
        return Optional.empty();
    }

    @Override
    public Either<Throwable, T> toEither() {
        return new Left<>(throwable);
    }

    @Override
    public Try<Throwable> failed() {
        return new Success<>(throwable);
    }

    @Override
    public <U> Try<U> transform(Function<T, Try<U>> successFunction, Function<Throwable, Try<U>> failureFunction) {
        return failureFunction.apply(throwable);
    }

    @Override
    public <U> U fold(Function<Throwable, U> failureFunction, Function<T, U> successFunction) {
        return failureFunction.apply(throwable);
    }

    @Override
    public T getOrElse(T fallback) {
        return fallback;
    }

    @Override
    public Try<T> orElse(Try<T> fallback) {
        return fallback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Failure)) {
            return false;
        }
        final Failure<?> that = (Failure<?>) o;
        return Objects.equals(throwable, that.throwable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Failure.class, throwable);
    }

    @Override
    public String toString() {
        return "Failure(" + throwable + ')';
    }
}
