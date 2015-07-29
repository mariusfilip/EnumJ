/*
 * The MIT License
 *
 * Copyright 2015 Marius Filip.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package enumj;

/**
 * Utility class containing check methods.
 */
final class Checks {

    private Checks() {}

    /**
     * Checks whether {@code something} is not null.
     *
     * @param <T> type of {@code something}.
     * @param something object to check for null.
     * @param message error message.
     * @throws IllegalArgumentException {@code something} is null.
     */
    public static <T> void ensureNotNull(T something, String message) {
        if (something == null) {
            throw new IllegalArgumentException(message);
        }
    }
    /**
     * Checks whether {@code something} is not negative.
     *
     * @param something {@code long} value to check.
     * @param message error message.
     * @throws IllegalArgumentException {@code something} is negative.
     */
    public static void ensureNonNegative(long something, String message) {
        if (something < 0) {
            throw new IllegalArgumentException(message);
        }
    }
    /**
     * Checks whether {@code something} is less than a {@code limit}.
     *
     * @param something {@code long} value to check.
     * @param limit {@code long} limit to check against.
     * @param message error message.
     * @throws IllegalArgumentException {@code something} is not less than
     * the given {@code limit}.
     */
    public static void ensureLessThan(long something,
                                      long limit,
                                      String message) {
        if (something >= limit) {
            throw new IllegalArgumentException(
                    message + ": " +
                    something + " is not less than " + limit);
        }
    }
    /**
     * Checks that an {@code enumerator} is not enumerating.
     *
     * @param enumerator {@link Enumerator} to check.
     * @throws IllegalStateException {@code enumerator} is enumerating.
     * @see Enumerator#enumerating()
     */
    public static void ensureNonEnumerating(Enumerator<?> enumerator) {
        if (enumerator.enumerating()) {
            throw new IllegalStateException(
                    Messages.ILLEGAL_MULTIPLE_ENUMERATIONS);
        }
    }
    /**
     * Checks that an {@code enumerable} is not enumerating.
     * @param enumerable {@link Enumerable} to check.
     * @throws IllegalStateException {@code enumerable} is enumerating.
     * @see Enumerable#enumerating()
     */
    public static void ensureNonEnumerating(Enumerable<?> enumerable) {
        if (enumerable.enumerating()) {
            throw new IllegalStateException(
                    Messages.ILLEGAL_MULTIPLE_ENUMERATIONS);
        }
    }
}
