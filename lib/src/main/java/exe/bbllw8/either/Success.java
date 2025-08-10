/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A successful result of a computation wrapped in a {@link Try} type.
 *
 * @param <T> Type of the held value
 * @author 2bllw8
 * @since 3.0.0
 */
public final class Success<T> extends Try<T> {

    private transient final T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        consumer.accept(value);
    }

    @Override
    public void forEach(Consumer<T> successConsumer, Consumer<Throwable> failureConsumer) {
        successConsumer.accept(value);
    }

    @Override
    public <U> Try<U> flatMap(Function<T, Try<U>> function) {
        return function.apply(value);
    }

    @Override
    public <U> Try<U> map(CheckedFunction<T, U> function) {
        return Try.from(() -> function.apply(value));
    }

    @Override
    public Try<T> filter(Function<T, Boolean> predicate) {
        return predicate.apply(value)
                ? this
                : new Failure<>(new NoSuchElementException("Predicate does not hold for " + value));
    }

    @Override
    public Try<T> recoverWith(Function<Throwable, Try<T>> function) {
        return this;
    }

    @Override
    public Try<T> recover(Function<Throwable, T> function) {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote If {@link #value} is null, the returned optional is {@link Optional#empty()}.
     */
    @Override
    public Optional<T> tOptional() {
        return Optional.ofNullable(value);
    }

    @Override
    public Either<Throwable, T> toEither() {
        return new Right<>(value);
    }

    @Override
    public Try<Throwable> failed() {
        return new Failure<>(new UnsupportedOperationException("Success.failed"));
    }

    @Override
    public <U> Try<U> transform(Function<T, Try<U>> successFunction,
                                Function<Throwable, Try<U>> failureFunction) {
        return successFunction.apply(value);
    }

    @Override
    public <U> U fold(Function<Throwable, U> failureFunction, Function<T, U> successFunction) {
        return successFunction.apply(value);
    }

    @Override
    public T getOrElse(T fallback) {
        return value;
    }

    @Override
    public Try<T> orElse(Try<T> fallback) {
        return this;
    }

    @Override
    public Stream<T> stream() {
        return Stream.of(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Success)) {
            return false;
        }
        final Success<?> that = (Success<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Success.class, value);
    }

    @Override
    public String toString() {
        return "Success(" + value + ')';
    }
}
