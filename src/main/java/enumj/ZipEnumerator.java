/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Marius Filip
 */
class ZipEnumerator<U, V, W extends Pair> extends AbstractEnumerator<W> {

    private Iterator<U> left;
    private Iterator<V> right;
    private BinaryCombiner<? super U, ? super V, ? extends W> combiner;

    private final boolean doesLeft;
    private final boolean doesRight;

    public ZipEnumerator(Iterator<U> left,
                         Iterator<V> right,
                         boolean doesLeft,
                         boolean doesRight,
                         BinaryCombiner<? super U,
                                        ? super V,
                                        ? extends W> combiner) {
        Utils.ensureNotNull(left, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(right, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(combiner, Messages.NullEnumeratorGenerator);
        this.left = left;
        this.right = right;
        this.doesLeft = doesLeft;
        this.doesRight = doesRight;
        this.combiner = combiner;
    }

    @Override
    protected boolean mayContinue() {
        if (doesLeft && doesRight) {
            return left.hasNext() && right.hasNext();
        }
        if (doesLeft) {
            return left.hasNext();
        }
        if (doesRight) {
            return right.hasNext();
        }
        return left.hasNext() || right.hasNext();
    }
    @Override
    protected W nextValue() {
        U u = left.hasNext() ? left.next() : null;
        V v = right.hasNext() ? right.next() : null;
        return combiner.combine(u, v);
    }
}
