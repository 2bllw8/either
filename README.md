# Either

[![Either CI](https://github.com/2bllw8/either/actions/workflows/main.yml/badge.svg)](https://github.com/2bllw8/either/actions/workflows/main.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.2bllw8/either)](https://search.maven.org/artifact/io.github.2bllw8/either)

Implementation of the `Either` and `Try` types for Java 8+ based on the
Scala [`Either`](https://scala-lang.org/api/3.x/scala/util/Either.html)
and [`Try`](https://scala-lang.org/api/3.x/scala/util/Try.html) types.

`Either` is a type that represents a value of 1 of 2 possible types (disjoint union).
An instance of `Either` is an  instance of _either_ `Left` or `Right`.

Conventionally, `Either` is used as a replacement for `Optional`: instead of having an empty
`Optional`, a `Left` that contains useful information is used, while `Right` contains
the successful result.

While `Either` is right–biased, it is possible to operate on the left values by using a
`LeftProjection`, obtained by invoking the `Either#left()` method.

The `Try` type represents a computation that may either result in an * exception, or
return a successfully computed value. 
It's similar to, but semantically different from the `Either` type.

Instances of `Try`, are either an instance of _either_ `Success` or `Failure`.

## Releases

The latest release is available on [Maven Central](https://search.maven.org/artifact/io.github.2bllw8/either/2.0.0/jar).

```groovy
implementation 'io.github.2bllw8:either:2.2.0'
```

## Usage

```java
import exe.bbllw8.either.CheckedException;
import exe.bbllw8.either.Either;
import exe.bbllw8.either.Try;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Arrays.stream(args)
                .flatMap(arg -> Try.from(() -> {
                    try {
                        //noinspection Since15
                        return Files.readString(Path.of(arg));
                    } catch (IOException e) {
                        throw new CheckedException(e);
                    }
                }).map(fileContent -> fileContent.substring(2)))
                .map(tryContent -> tryContent.map(Integer::parseInt))
                .map(tryNumber -> tryNumber.toEither())
                .map(either -> either.left().map(Throwable::getMessage))
                .map(either -> either.fold(
                        error -> "Error: " + error,
                        value -> "The result is " + value))
                .forEach(System.out::println);
    }
}
```

## Documentation

Javadoc is available at [2bllw8.github.io/either](https://2bllw8.github.io/either)
