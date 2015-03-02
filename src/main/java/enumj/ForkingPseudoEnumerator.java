/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Marius Filip
 */
final class ForkingPseudoEnumerator<E> {

    private final Iterator<E> source;
    private final Predicate<E> leftChooser;

    private ForkedEnumerator<E> left;
    private ForkedEnumerator<E> right;

    private LinkedList<E> leftBuf;
    private LinkedList<E> rightBuf;

    public ForkingPseudoEnumerator(Iterator<E> source,
                                   Predicate<E> leftChooser) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(leftChooser, Messages.NullEnumeratorPredicate);
        this.source = source;
        this.leftChooser = leftChooser;

        this.left = new ForkedEnumerator(this, true);
        this.right = new ForkedEnumerator(this, false);

        this.leftBuf = new LinkedList<>();
        this.rightBuf = new LinkedList<>();
    }

    public boolean hasNext(boolean left) {
        if (left && this.left == null
            || !left && this.right == null) {
            return false;
        }
        while(true) {
            if (left && !this.leftBuf.isEmpty()
                || !left && !this.rightBuf.isEmpty()) {
                return true;
            }
            if (!source.hasNext()) {
                break;
            }
            final E value = source.next();
            if (this.leftChooser.test(value)) {
                this.leftBuf.addLast(value);
            }
            else {
                this.rightBuf.addLast(value);
            }
        }
        if (left) {
            this.left = null;
            this.leftBuf = null;
        }
        else {
            this.right = null;
            this.rightBuf = null;
        }
        return false;
    }

    public E next(boolean left) {
        if (left) {
            assert !this.leftBuf.isEmpty();
            return this.leftBuf.removeFirst();
        }
        else {
            assert !this.rightBuf.isEmpty();
            return this.rightBuf.removeFirst();
        }
    }
    
    public Pair<Enumerator<E>, Enumerator<E>> fork() {
        return Pair.of(left, right);
    }
}
