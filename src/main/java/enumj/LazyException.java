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
 * {@code RuntimeException} for the use of {@code Lazy} instances.
 *
 * @see Exception
 * @see RuntimeException
 * @see Lazy
 */
public class LazyException extends RuntimeException {

    /**
     * Creates an empty {@code LazyException} instance.
     * 
     * @see LazyException
     */
    public LazyException() {
        super();
    }
    /**
     * Creates a {@code LazyException} instance with message.
     *
     * @param msg message to associate with {@link LazyException}.
     */
    public LazyException(String msg) {
        super(msg);
    }
    /**
     * Creates a {@link LazyException} instance with message and cause.
     *
     * @param msg message to associate with {@link LazyException}.
     * @param cause cause for exception.
     */
    public LazyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}