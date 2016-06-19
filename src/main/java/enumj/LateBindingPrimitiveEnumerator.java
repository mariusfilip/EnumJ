/*
 * The MIT License
 *
 * Copyright 2016 Marius Filip.
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
import java.util.PrimitiveIterator;

public final class LateBindingPrimitiveEnumerator {
    public final static class OfInt extends AbstractPrimitiveEnumerator.OfInt {

        private PrimitiveIterator.OfInt source;

        @Override protected boolean internalHasNext() {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            return source != null && source.hasNext();
        }
        @Override protected int internalNextInt() {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            return source.nextInt();
        }
        @Override protected void internalRecovery(Throwable error) {
            source = null;
        }
        @Override protected void cleanup() {
            source = null;
        }

        // ------------------------------------------------------------------ //

        public void bind(PrimitiveIterator.OfInt source) {
            Checks.ensureNonEnumerating(this);
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            this.source = source;
        }
        public Optional<PrimitiveIterator.OfInt> binding() {
            return Optional.ofNullable(source);
        }
    }
}
