/*
 * Copyright (c) 2022 2bllw8
 * SPDX-License-Identifier: Apache-2.0
 */
package exe.bbllw8.either;

/**
 * A CheckedException instance is used to <i>catch</i> checked Exceptions for usage in {@link
 * Try#from(CheckedSupplier)}.
 *
 * <pre>
 * public class Example {
 *     public static Try&#60;Long&#62; getSize(Path p) {
 *         return Try.from(() -&#62; {
 *             try {
 *                 return Files.size(p);
 *             } catch(IOException e) {
 *                 throw new CheckedException(e);
 *             }
 *         });
 *     }
 * }
 * </pre>
 *
 * @author 2bllw8
 * @see Try#from(CheckedSupplier)
 * @since 2.2.0
 * @deprecated Use {@link CheckedSupplier} instead of thowing wrapped exceptions.
 */
@Deprecated
public final class CheckedException extends RuntimeException {

    private static final long serialVersionUID = 2896775411630760282L;

    public CheckedException(Throwable cause) {
        super(cause);
    }
}
