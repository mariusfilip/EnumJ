/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;

/**
 *
 * @author Marius Filip
 */
class ConcatEnumerator<E> extends AbstractEnumerator<E> {

    private final Iterator<E> first;
    private final Iterator<E> second;

    public ConcatEnumerator(Iterator<E> first, Iterator<E> second) {
        Utils.ensureNotNull(first, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(second, Messages.NullEnumeratorSource);
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean hasNext() {
        return first.hasNext() || second.hasNext();
    }

    @Override
    protected E nextValue() {
        return first.hasNext() ? first.next() : second.next();
    }
}
