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
implementation 'io.github.2bllw8:either:2.0.0'
```

## Usage

```java
import exe.bbllw8.either.Either;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        MyMagicCode magic = new MyMagicCode();

        List<Either<MyError, MyObject>> results = Arrays.stream(args)
                // If the function throws a NumberFormatException,
                // it returns a Left(NumberFormatException),
                // otherwise, a Right(Integer)
                .map(arg -> Either.fromTry(() -> Integer.parseInt(arg), 
                                NumberFormatException.class)
                        // Map the left value to a MyError instance
                        .left().map(ex -> new MyError(ex.getMessage()))
                        // Map the right value to a MyObject instance
                        .map(magic::doSomeMagic))
                .collect(Collectors.toList());
        
        for (int i = 0; i < results.size(); i++) {
            final String arg = args[i];
            final Either<MyError, MyObject> either = results.get(i);
            if (either.isRight()) {
                either.forEach(x -> System.out.println(arg + " -> " + x));
            } else {
                either.left().forEach(x -> System.err.println("Error occurred for '" 
                        + arg + "': " + x));
            }
        }
    }
}
```

## Documentation

Javadoc is available at [2bllw8.github.io/either](https://2bllw8.github.io/either)