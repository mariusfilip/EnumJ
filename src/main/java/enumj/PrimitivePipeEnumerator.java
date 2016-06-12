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

import java.util.PrimitiveIterator;

final class PrimitivePipeEnumerator {
    
    final static class OfInt extends    PipeEnumerator<Integer>
                             implements PrimitiveEnumerator.OfInt {
        
        public OfInt(PrimitiveEnumerator.OfInt source) {
            super(source);
        }
        public OfInt() { super(); }
        
        @Override
        public int nextInt() { return super.next(); }
        @Override
        public PrimitiveEnumerator.OfInt concat(
                PrimitiveIterator.OfInt elements) {
            super.addSourceLast(PrimitivePipeSource.of(elements));
            return this;
        }
    }
    
    public static PrimitivePipeEnumerator.OfInt of(
            PrimitiveIterator.OfInt source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return (source instanceof PrimitivePipeEnumerator.OfInt)
               ? (PrimitivePipeEnumerator.OfInt)source
               : new PrimitivePipeEnumerator.OfInt(
                         PrimitiveEnumerator.of(source));
    }
}
