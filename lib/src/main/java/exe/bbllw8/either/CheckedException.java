package exe.bbllw8.either;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An CheckedException instance is used to <i>catch</i>
 * checked Exceptions for usage in {@link Either#tryCatch(Supplier, Class)}
 * and {@link Either#tryCatch(Supplier, Function)}.
 *
 * <pre>
 * public class Example {
 *     public static Either&#60;IOException, Long&#62; getSize(Path p) {
 *         return Either.tryCatch(() -&#62; {
 *             try {
 *                 return Files.size(p);
 *             } catch(IOException e) {
 *                 throw new CheckedException(e);
 *             }
 *         }, IOException.class);
 *     }
 * }
 * </pre>
 * @author 2bllw8
 * @see Either#tryCatch(Supplier, Class)
 * @see Either#tryCatch(Supplier, Function)
 * @since 2.2
 */
public final class CheckedException extends RuntimeException {
    private static final long serialVersionUID = 2896775411630760282L;

    public CheckedException(Throwable cause) {
        super(cause);
    }
}
