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
import java.util.function.Consumer;

final class TolerantEnumerator<E> extends AbstractEnumerator<E> {

    private Iterator<E>                 source;
    private Consumer<? super Exception> handler;

    private final int         retries;
    private final Nullable<E> element;

    public TolerantEnumerator(Enumerator<E> source,
                              Consumer<? super Exception> handler,
                              int retries) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNonEnumerating(source);
        Utils.ensureNotNull(handler, Messages.NULL_ENUMERATOR_HANDLER);
        Utils.ensureNonNegative(retries, Messages.NEGATIVE_RETRIES);

        this.source = source;
        this.handler = handler;
        this.retries = retries;
        this.element = Nullable.empty();
    }

    @Override
    protected boolean internalHasNext() {
        if (element.isPresent()) {
            return true;
        }

        while(safeHasNext()) {
            try {
                element.set(source.next());
                return true;
            } catch(Exception ex) {
                try {
                    handler.accept(ex);
                } catch(Exception ex1) {
                    // do nothing
                }
            }
        }

        return false;
    }

    private boolean safeHasNext() {
        try {
            return source.hasNext();
        } catch(Exception ex) {
            try {
                handler.accept(ex);
            } catch(Exception ex1) {
                return false;
            }
        }
        for(int i=0; i<retries; ++i) {
            try {
                return source.hasNext();
            } catch(Exception ex) {
                try {
                    handler.accept(ex);
                } catch(Exception ex1) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    protected E internalNext() {
        E result = element.get();
        element.clear();
        return result;
    }

    @Override
    protected void cleanup() {
        source = null;
        handler = null;
        
        element.clear();
    }
}
