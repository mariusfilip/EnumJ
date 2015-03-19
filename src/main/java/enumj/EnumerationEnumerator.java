/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Enumeration;

/**
 *
 * @author Marius Filip
 */
final class EnumerationEnumerator<E> extends AbstractEnumerator<E> {

    private Enumeration<E> source;

    EnumerationEnumerator(Enumeration<E> source) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
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
