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
import java.util.Optional;

/**
 * Enumerator capable of late binding.
 *
 * @param <E> type of enumerated elements
 */
public final class LateBindingEnumerator<E> extends AbstractEnumerator<E> {

    private Iterator<E> source;

    LateBindingEnumerator() {}

    @Override
    protected boolean internalHasNext() {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return source.hasNext();
    }
    @Override
    protected E internalNext() {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return source.next();
    }
    @Override
    protected void cleanup() {
        source = null;
    }

    // ---------------------------------------------------------------------- //

    /**
     * Binds the current enumerator to the given source iterator.
     *
     * @param source iterator to bind to
     * @exception IllegalStateException the current enumerator is enumerating
     * @exception IllegalArgumentException <code>source</code> is
     * <code>null</code>.
     */
    public void bind(Iterator<? extends E> source) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        this.source = (Iterator<E>)source;
    }

    /**
     * Gets the binding of the current enumerator, if any.
     *
     * @return {@link Optional} containing the iterator that the current
     * enumerator is bound to, if any.
     */
    public Optional<Iterator<E>> binding() {
        return Optional.ofNullable(source);
    }
}