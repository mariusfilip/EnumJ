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
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Facultative<T> {

    private T value;
    private boolean isPresent;

    private Facultative() {}
    private Facultative(T value) {
        this.value = value;
        this.isPresent = true;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public T get() {
        if (!isPresent) {
            throw new NoSuchElementException();
        }
        return value;
    }

    public void set(T elem) {
        this.value = elem;
        this.isPresent = true;
    }

    public void clear() {
        value = null;
        isPresent = false;
    }

    public T orElse(T other) {
        return isPresent ? value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return isPresent ? value : other.get();
    }

    public <X extends Throwable> T orElseThrow(
            Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent) {
            return value;
        }
        throw exceptionSupplier.get();
    }
    
    // ---------------------------------------------------------------------- //
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Facultative)) {
            return false;
        }
        if (isPresent != ((Facultative)other).isPresent) {
            return false;
        }
        return Objects.equals(value, ((Facultative)other).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isPresent);
    }

    @Override
    public String toString() {
        return isPresent 
                ? (value != null
                   ? String.format("Facultative[%s]", value)
                   : "Facultative[null]")
                : "Facultative.empty";
    }

    // ---------------------------------------------------------------------- //

    public static Facultative empty() {
        return new Facultative();
    }

    public static <E> Facultative<E> of(E elem) {
        return new Facultative(elem);
    }

    public static <E> Facultative<E> of(Optional<E> elem) {
        return elem.isPresent() ? of(elem.get()) : empty();
    }

    // ---------------------------------------------------------------------- //

    public Facultative<T> filter(Predicate<? super T> predicate) {
        return !isPresent || predicate.test(value) ? this : empty();
    }

    public <U> Facultative<U> flatMap(
            Function<? super T, Facultative<U>> mapper) {
        return isPresent ? mapper.apply(value) : empty();
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (isPresent) {
            consumer.accept(value);
        }
    }

    public <U> Facultative<U> map(Function<? super T, ? extends U> mapper) {
        return isPresent ? of(mapper.apply(value)) : empty();
    }
}
