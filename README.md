# Either

[![Either CI](https://github.com/2bllw8/either/actions/workflows/main.yml/badge.svg)](https://github.com/2bllw8/either/actions/workflows/main.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.2bllw8/either)](https://search.maven.org/artifact/io.github.2bllw8/either)

Implementation of the `Either` type for Java 8+ based on the
[Scala `Either` type](https://scala-lang.org/api/3.x/scala/util/Either.html).

`Either` is a type that represents a value of 1 of 2 possible types (disjoint union). An instance of `Either` is an
instance of _either_ `Left` or `Right`.

Conventionally, `Either` is used as a replacement for `Optional`: instead of having an empty
`Optional`, a `Left` that contains useful information is used, while `Right` contains the successful result.

While `Either` is right–biased, it is possible to operate on the left values by using a
`LeftProjection`, obtained by invoking the `Either#left()` method.

## Releases

The latest release is available on [Maven Central](https://search.maven.org/artifact/io.github.2bllw8/either/2.0.0/jar).

```groovy
implementation 'io.github.2bllw8:either:2.2.0'
```

## Usage

```java
import exe.bbllw8.either.Either;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        MyMagicCode magic = new MyMagicCode();
        for (String arg : args) {
            final Path p = new File(arg).toPath();
            Either.tryCatch(() -> {
                try {
                    return Files.size(path);
                } catch (IOException e) {
                    throw new CheckedException(e);
                }
            }, IOException.class)
                    // Map the left value to a MyError instance
                    .left().map(MyError::new)
                    // Map the right value to a MyObject instance
                    .map(magic::doSomeMagic)
                    .forEach(leftVal -> System.err.println(
                                    "E: " + arg + ": " + leftVal),
                            rightVal -> System.out.println(
                                    arg + " -> " + rightVal));
        }
    }
}
```

## Documentation

Javadoc is available at [2bllw8.github.io/either](https://2bllw8.github.io/either)