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
package com.github.enumj;

import java.util.function.Supplier;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

/**
 * {@code LazyInitializer} that gets a {@code Supplier} to initialise
 * with.
 * @param <T> type of lazily enumerated entity.
 * @see LazyInitializer
 * @see Supplier
 */
public final class Lazy<T> extends LazyInitializer<T> {

    private Supplier<T> supplier;
    private volatile boolean initialized;

    /**
     * Constructs a {@code Lazy} instance.
     *
     * @param supplier {@link Supplier} instance providing the lazily
     * initialised non-null element.
     * @see Lazy
     */
    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets whether this lazy instance has been initialised.
     *
     * @return true if the value has been initialised, false
     * otherwise.
     */
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public T get() {
        try {
            final T result = super.get();
            initialized = true;
            return result;
        } catch(ConcurrentException ex) {
            throw new UnsupportedOperationException(ex.getCause());
        }
    }
    @Override
    protected T initialize() throws ConcurrentException {
        try {
            final T result = supplier.get();
            supplier = null;
            return result;
        } catch(LazyException ex) {
            throw new ConcurrentException(ex.getCause());
        }
    }
}
