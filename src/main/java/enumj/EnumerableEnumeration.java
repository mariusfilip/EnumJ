/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Enumeration;
import java.util.Iterator;

/**
 *
 * @author Marius Filip
 */
final class EnumerableEnumeration<E> implements Enumeration<E> {
    
    private Iterator<E> source;

    public EnumerableEnumeration(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    public boolean hasMoreElements() {
        return source.hasNext();
    }

    @Override
    public E nextElement() {
        return source.next();
    }
}
