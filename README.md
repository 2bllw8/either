# Either

[![Either CI](https://github.com/2bllw8/either/actions/workflows/main.yml/badge.svg)](https://github.com/2bllw8/either/actions/workflows/main.yml)
[![](https://jitpack.io/v/2bllw8/either.svg)](https://jitpack.io/#2bllw8/either)


Implementation of the `Either` type for Java 8+

Either is a type that represents a value of 1 of 2 possible types (disjoint union).

Usage:

```groovy
// Add jitpack repo
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

// Include the lib
dependencies {
    implementation 'com.github.2bllw8:either:1.0.0'
}
```

Example usage

```java
import exe.bbllw8.Either;
import java.util.stream.Collectors;

public class Main {

    private List<Either<Error, ADataType>> performMyOperations(String[] args) {
        // ...
    }

    private MyDataType convertToMyDataType(ADataType obj) {
        // ...
    }

    public static void main(String[] args) {
        // The "operations" returns either an error or a data result
        List<Either<Error, MyDataType>> result = performMyOperations(args).stream()
                .mapRight(data -> convertToMyDataType(data));
        List<Error> errors = result.leftValues();
        if (errors.isEmpty()) {
            // All operation completed successfully
            System.out.println(result.rightValues()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ")));
        } else {
            System.err.println(errors.map(error -> "An error occurred: " + error)
                    .collect(Collectors.joining("\n")));
        }
    }
}
```

### Javadoc

Javadoc is available at [2bllw8.github.io/either](https://2bllw8.github.io/either)