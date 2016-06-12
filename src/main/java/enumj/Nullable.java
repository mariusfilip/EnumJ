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

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Container object which may or may not contain a value. Unlike
 * {@code Optional} with which it shares many similarities, {@code Nullable}
 * may contain {@code null} values.
 * <p>
 * This is a <em>value-based</em> class.
 * </p>
 * @param <T> type of contained value.
 * @see Optional
 */
public final class Nullable<T> extends Value<T> {

    private Nullable() { super(); }
    private Nullable(T value) { super(value); }
    private Nullable(int value) { super(value); }
    private Nullable(long value) { super(value); }
    private Nullable(double value) { super(value); }
    private Nullable(Nullable<? extends T> value) { super(value); }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present.
     * @return the value, if present, otherwise {@code other}.
     */
    public T orElse(T other) {
        return super.isPresent() ? super.get() : other;
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return
     * the result of that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if no value is
     * present.
     * @return the value if present otherwise the result of {@code other.get()}.
     * @throws NullPointerException if value is not present and {@code other}
     * is null.
     */
    public T orElseGet(Supplier<? extends T> other) {
        return super.isPresent() ? super.get() : other.get();
    }

    /**
     * Return the contained value, if present, otherwise throw an exception to
     * be created by the provided supplier.
     *
     * @param <X> type of exception to be thrown.
     * @param exceptionSupplier the supplier which will return the exception to
     * be thrown.
     * @return the present value.
     * @throws X if there is no value present.
     */
    public <X extends Throwable> T orElseThrow(
            Supplier<? extends X> exceptionSupplier) throws X {
        if (super.isPresent()) { return super.get(); }
        throw exceptionSupplier.get();
    }

    // ---------------------------------------------------------------------- //
    @Override
    public String toString() {
        return super.isPresent()
                ? (super.get() != null
                   ? String.format("Facultative[%s]", super.get())
                   : "Facultative[null]")
                : "Facultative.empty";
    }

    // ---------------------------------------------------------------------- //

    /**
     * Returns an empty {@code Nullable} instance. No value is present for this
     * {@code Nullable}.
     *
     * @param <T> type of the non-existent value.
     * @return an empty {@link Nullable}.
     */
    public static <T> Nullable<T> empty() {
        return new Nullable();
    }

    /**
     * Returns a {@code Nullable} with the specified {@code value}.
     *
     * @param <T> the class of value.
     * @param value the value to be present.
     * @return a {@link Nullable} with the value present.
     */
    public static <T> Nullable<T> of(T value) {
        return new Nullable(value);
    }

    /**
     * Returns a {@code Nullable} with the value of the specified
     * {@code Optional} if any or an empty {@code Nullable} otherwise.
     * 
     * @param <T> the class of value.
     * @param value {@link Optional} value to be present.
     * @return a {@link Nullable} with the value present.
     */
    public static <T> Nullable<T> of(Optional<T> value) {
        return value.isPresent() ? of(value.get()) : empty();
    }

    // ---------------------------------------------------------------------- //

    /**
     * If a value is present and it matches the given {@code predicate},
     * return a {@code Nullable} containing the value, otherwise return an
     * empty {@code Nullable}.
     *
     * @param predicate a predicate to apply to the value, if present.
     * @return a {@link Nullable} describing the value of this {@code Nullable}
     * if present and it matches the given {@code predicate}, otherwise an
     * empty {@code Nullable}.
     */
    public Nullable<T> filter(Predicate<? super T> predicate) {
        return !super.isPresent() || predicate.test(super.get())
                ? this
                : empty();
    }

    /**
     * If a value is present, apply the {@code Nullable}-bearing function to it,
     * return that result, otherwise return an empty {@code Nullable}.
     *
     * @param <U> type parameter to the {@link Nullable} returned by
     * {@code mapper}.
     * @param mapper a mapping {@link Function} to apply to the value, if any.
     * @return the result of applying a {@code Nullable}-bearing mapping
     * function to the value of this {@code Nullable}, if a value is present,
     * otherwise an empty {@code Nullable}.
     */
    public <U> Nullable<U> flatMap(
            Function<? super T, Nullable<U>> mapper) {
        return super.isPresent() ? mapper.apply(super.get()) : empty();
    }

    /**
     * Return true if a value is present, otherwise false.
     *
     * @param consumer block to be executed if a value is present.
     */
    public void ifPresent(Consumer<? super T> consumer) {
        if (super.isPresent()) { consumer.accept(super.get()); }
    }

    /**
     * If a value is present, apply the provided mapping function to it and
     * return a {@code Nullable} describing the result. Otherwise return an
     * empty {@code Nullable}.
     *
     * @param <U> the type of the result of the mapping function.
     * @param mapper a mapping {@link Function} to apply to the value,
     * if present.
     * @return a {@link Nullable} describing the result of applying a mapping
     * function to the value of this {@code Nullable}, if a value is present,
     * otherwise an empty {@code Nullable}.
     */
    public <U> Nullable<U> map(Function<? super T, ? extends U> mapper) {
        return super.isPresent()
                ? of(mapper.apply(super.get()))
                : empty();
    }
}
