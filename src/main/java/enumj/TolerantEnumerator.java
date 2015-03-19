/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 *
 * @author Marius Filip
 */
final class TolerantEnumerator<E> extends AbstractEnumerator<E> {

    private Iterator<E> source;
    private Consumer<? super Exception> handler;
    private int retries;
    private Nullable<E> element;

    public TolerantEnumerator(Iterator<E> source,
                              Consumer<? super Exception> handler,
                              int retries) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
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
        element = null;
    }
}
