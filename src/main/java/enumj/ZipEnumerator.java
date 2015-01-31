/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Marius Filip
 */
public class ZipEnumerator<U, V, W extends Pair> extends AbstractEnumerator<W> {

    private final Iterator<U> left;
    private final Iterator<V> right;
    private final boolean doesLeft;
    private final boolean doesRight;
    private final BinaryCombiner<? super Mutable<U>, ? super Mutable<V>, ? extends W> combiner;

    public ZipEnumerator(Iterator<U> left,
                         Iterator<V> right,
                         boolean doesLeft,
                         boolean doesRight,
                         BinaryCombiner<? super Mutable<U>,
                                        ? super Mutable<V>,
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
    public boolean hasNext() {
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
        Mutable<U> u = left.hasNext() ? new MutableObject(left.next()) : null;
        Mutable<V> v = right.hasNext() ? new MutableObject(right.next()) : null;
        return combiner.combine(u, v);
    }
}
