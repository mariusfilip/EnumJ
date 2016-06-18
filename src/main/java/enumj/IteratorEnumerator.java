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

import java.util.Iterator;

/**
 * {@code Enumerator} encapsulating an {@code Iterator}.
 * <p>
 * <strong>Important:</strong> this class may not be {@code final} because
 * {@code PipeSource} extends it.
 * </p>
 *
 * @param <E> type of enumerated elements.
 * @see Enumerator
 * @see AbstractEnumerator
 * @see Iterator
 * @see PipeSource
 */
class IteratorEnumerator<E> extends AbstractEnumerator<E> {

    private Iterator<E> source;

    /**
     * Creates an {@code IteratorEnumerator} instance.
     * <p>
     * The new {@link IteratorEnumerator} stores its {@code source} internally.
     * </p>
     *
     * @param source {@link Iterator} to encapsulate.
     */
    protected IteratorEnumerator(Iterator<E> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        if (source instanceof Enumerator) {
            Checks.ensureNonEnumerating((Enumerator<E>)source);
        }
        this.source = source;
    }

    @Override
    protected final boolean internalHasNext() {
        return source.hasNext();
    }
    @Override
    protected final void internalNext(Out<E> value) {
        value.set(source.next());
    }
    @Override
    protected final void cleanup() {
        source = null;
    }

    // ---------------------------------------------------------------------- //

    /**
     * Constructs an {@code Enumerator} from an {@code Iterator}. If the
     * iterator is already an enumerator, it returns is unchanged, otherwise
     * it returns an {@code IteratorEnumerator} encapsulating it.
     * 
     * @param <T> type of enumerated elements.
     * @param source {@link Iterator} to get elements from.
     * @return {@link Enumerator} instance.
     */
    static <T> Enumerator<T> of(Iterator<T> source) {
        Enumerator<T> result = (source != null && source instanceof Enumerator)
                               ? (Enumerator<T>)source
                               : new IteratorEnumerator<>(source);
        if (result == source) {
            Checks.ensureNonEnumerating(result);
        }
        return result;
    }
}
