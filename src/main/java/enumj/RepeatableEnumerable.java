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
import java.util.function.Supplier;

class RepeatableEnumerable<E> extends AbstractEnumerable<E> {

    protected Supplier<Optional<Iterator<E>>> repeatableSource;
    protected Optional<Enumerator<E>> enumerator;

    protected RepeatableEnumerable(
            Supplier<Optional<Iterator<E>>> repeatableSource) {
        this.repeatableSource = repeatableSource;
        this.enumerator = repeatableSource.get().map(Enumerator::of);
    }

    @Override
    protected Enumerator<E> internalEnumerator() {
        Enumerator<E> result = enumerator.get();
        enumerator = repeatableSource.get().map(Enumerator::of);
        return result;
    }
    @Override
    protected boolean internalConsumed() {
        return !enumerator.isPresent();
    }
    @Override
    protected void cleanup() {
        repeatableSource = null;
        enumerator = null;
    }

    public static <E> RepeatableEnumerable<E> of(
        Supplier<Optional<Iterator<E>>> repeatableSource) {
        Utils.ensureNotNull(repeatableSource, Messages.NULL_ENUMERATOR_SOURCE);
        return new RepeatableEnumerable(repeatableSource);
    }
}