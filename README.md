# Either

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

// Include pencil lib
dependencies {
    implementation 'com.github.2bllw8:either:SNAPSHOT'
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