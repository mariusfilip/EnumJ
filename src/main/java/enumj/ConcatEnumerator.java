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

    private Iterator<E> first;
    private Iterator<E> second;
    private boolean firstDone;
    private boolean secondDone;

    public ConcatEnumerator(Iterator<E> first, Iterator<E> second) {
        Utils.ensureNotNull(first, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(second, Messages.NullEnumeratorSource);
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean hasNext() {
        if (!firstDone) {
            firstDone = !first.hasNext();
            if (firstDone) {
                first = null;
            }
        }
        if (!secondDone) {
            secondDone = !second.hasNext();
            if (secondDone) {
                second = null;
            }
        }
        return !(firstDone && secondDone);
    }

    @Override
    protected E nextValue() {
        return firstDone ? second.next() : first.next();
    }
}
