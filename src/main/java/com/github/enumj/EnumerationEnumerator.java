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

import java.util.Enumeration;

/**
 * {@code Enumerator} encapsulating an {@code Enumeration}.
 *
 * @param <E> type of enumerated elements.
 * @see EnumerableEnumeration
 * @see Enumerator
 * @see Enumeration
 */
final class EnumerationEnumerator<E> extends AbstractEnumerator<E> {

    private Enumeration<E> source;

    /**
     * Constructs an {@code EnumerationEnumerator} instance.
     * <p>
     * The new {@link EnumerationEnumerator} stores its {@code source}
     * internally.
     * </p>
     *
     * @param source {@link Enumeration} instance to get elements from.
     */
    public EnumerationEnumerator(Enumeration<E> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        this.source = source;
    }

    @Override
    protected boolean internalHasNext() {
        return source.hasMoreElements();
    }
    @Override
    protected E internalNext() {
        return source.nextElement();
    }
    @Override
    protected void cleanup() {
        source = null;
    }
}
