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
import java.util.PrimitiveIterator;

/**
 * {@code Enumerator} and {@code PrimitiveIterator.OfLong} enumerating
 * over a range of long integers.
 * 
 * @see Enumerator
 * @see PrimitiveIterator
 */
public final class LongRangeEnumerator implements PrimitiveIterator.OfLong,
                                                  Enumerator<Long> {
    private long    start;
    private long    end;
    private long    iter;
    private boolean isInclusive;
    private boolean overflow;
    private boolean enumerating;

    /**
     * Constructs an {@link LongRangeEnumerator} instance.
     *
     * @param start lower limit of long integer range.
     * @param end upper limit of long integer range.
     * @param isInclusive true if {@code end} is an inclusive upper
     * limit, false otherwise.
     */
    public LongRangeEnumerator(long start,
                               long end,
                               boolean isInclusive) {
        this.start = start;
        this.end = end;
        this.isInclusive = isInclusive;

        this.iter = this.start;
        this.overflow = false;
        this.enumerating = false;
    }

    @Override
    public boolean enumerating() {
        return enumerating;
    }
    @Override
    public boolean hasNext() {
        enumerating = true;
        return !overflow && (iter < end || isInclusive && iter == end);
    }
    @Override
    public long nextLong() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final long result = iter;
        if (iter == Long.MAX_VALUE) {
            overflow = true;
        } else {
            ++iter;
        }
        return result;
    }
    @Override
    public Long next() {
        return nextLong();
    }
}
