/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Marius Filip
 */
class ZipRightEnumerator<U, V>
      extends ZipEnumerator<U, V, Pair<Optional<U>, V>> {
    public ZipRightEnumerator(Iterator<U> left, Iterator<V> right) {
        super(left, right, false, true, ZipRightEnumerator::combine);
    }

    private static <U,V> Pair<Optional<U>, V> combine(U left, V right) {
        return Pair.of(Optional.ofNullable(left), right);
    }
}
