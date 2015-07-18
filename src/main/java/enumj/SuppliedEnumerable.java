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

import java.util.function.Supplier;

/**
 * {@link Enumerable} implementation that returns enumerators yielded by
 * a {@link Supplier}.
 * @param <E> type of enumerated elements.
 */
class SuppliedEnumerable<E> extends AbstractEnumerable<E> {

    Supplier<Enumerator<E>> supplier;

    /**
     * Constructs a {@link SuppliedEnumerable} instance.
     * @param supplier {@link Supplier} instance providing the
     * {@link Enumerator} instance to return.
     */
    public SuppliedEnumerable(Supplier<Enumerator<E>> supplier) {
        Utils.ensureNotNull(supplier, Messages.NULL_ENUMERATOR_GENERATOR);
        this.supplier = supplier;
    }

    @Override
    protected boolean internalOnceOnly() {
        return false;
    }
    @Override
    protected Enumerator<E> internalEnumerator() {
        return supplier.get();
    }
}
