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
class ZipBothEnumerator<U, V>
      extends ZipEnumerator<U, V, Pair<U, V>> {
    public ZipBothEnumerator(Iterator<U> left, Iterator<V> right) {
        super(left, right, true, true, ZipBothEnumerator::combine);
    }

    private static <U,V> Pair<U, V> combine(U left, V right) {
        return Pair.of(left, right);
    }
}
