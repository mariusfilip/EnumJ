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

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Encapsulates output method parameters.
 *
 * @param <T> Type of output parameter.
 */
public final class Out<T> {

    private T       value;
    private boolean hasValue;

    private Out() {}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Out) {
            final Out<?> other = (Out<?>)obj;
            return hasValue == other.hasValue
                   && Objects.equals(value, other.value);
        }
        return super.equals(obj);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(hasValue) + Objects.hashCode(value);
    }
    @Override
    public String toString() {
        return hasValue ? value.toString() : "<none>";
    }

    /**
     * Gets the value of the output parameter.
     *
     * @return Value of the output parameter, if any.
     * @throws NoSuchElementException An output value has not been set.
     */
    public T get() {
        if (!this.hasValue) {
            throw new NoSuchElementException();
        }
        return this.value;
    }

    /**
     * Sets the value of the output parameter.
     *
     * @param value Value of the output parameter.
     */
    public void set(T value) {
        this.value = value;
        this.hasValue = true;
    }

    /**
     * Gets whether an output parameter has been set.
     *
     * @return true if this {@link Out} has been set, false otherwise.
     */
    public boolean hasValue() {
        return this.hasValue;
    }

    /**
     * Empties the output parameter.
     */
    public void clear() {
        this.value = null;
        this.hasValue = false;
    }

    /**
     * Creates a new, empty output parameter.
     *
     * @param <T> Type of output parameter.
     * @return new {@link Out} instance.
     */
    public static <T> Out<T> empty() {
        return new Out<T>();
    }
}
