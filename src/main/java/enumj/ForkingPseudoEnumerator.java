/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.LinkedList;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Marius Filip
 */
final class ForkingPseudoEnumerator<E> {

    private final Iterator<E> source;
    private final LinkedList<ForkedElement<E>> buffer;

    private ForkedEnumerator<E> left;
    private ForkedEnumerator<E> right;
    private ForkedElement<E> leftPtr;
    private ForkedElement<E> rightPtr;

    public ForkingPseudoEnumerator(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
        this.left = new ForkedEnumerator(this, true);
        this.right = new ForkedEnumerator(this, false);
        this.buffer = new LinkedList<>();
    }

    public boolean hasNext(boolean left) {
        if (left) {
            if (this.left == null) {
                return false;
            }
            if (this.leftPtr != null
                && !this.leftPtr.leftHas()) {
                return true;
            }
            if (source.hasNext()) {
                return true;
            }
            this.left = null;
        } else {
            if (this.right == null) {
                return false;
            }
            if (this.rightPtr != null
                && !this.rightPtr.rightHas()) {
                return true;
            }
            if (source.hasNext()) {
                return true;
            }
            this.right = null;
        }
        return false;
    }

    public E next(boolean left) {
        if (buffer.isEmpty()) {
            newElement();
        }

        ForkedElement<E> ptr = null;
        if (left) {
            if (leftPtr == null) {
                leftPtr = buffer.getFirst();
            }
            if (leftPtr.leftHas()) {
                if (leftPtr.getNext() == null) {
                    assert leftPtr == buffer.getLast();
                    newElement();
                    assert leftPtr.getNext() != null;
                }
                leftPtr = leftPtr.getNext();
            }
            ptr = leftPtr;
        }
        else {
            if (rightPtr == null) {
                rightPtr = buffer.getFirst();
            }
            if (rightPtr.rightHas()) {
                if (rightPtr.getNext() == null) {
                    assert rightPtr == buffer.getLast();
                    newElement();
                    assert rightPtr.getNext() != null;
                }
                rightPtr = rightPtr.getNext();
            }
            ptr = rightPtr;
        }

        final E result = ptr.getValue(left);
        final ForkedElement<E> head = buffer.getFirst();
        if (leftPtr != head && rightPtr != head) {
            assert head.leftHas() && head.rightHas();
            buffer.remove();
        }

        assert leftPtr == buffer.getFirst() && rightPtr == buffer.getLast()
               || leftPtr == buffer.getLast() && rightPtr == buffer.getFirst();
        return result;
    }

    private void newElement() {
        final ForkedElement<E> elem = new ForkedElement<>(source.next());
        if (!buffer.isEmpty()) {
            buffer.getLast().setNext(elem);
        }
        buffer.addLast(elem);
    }

    public Pair<Enumerator<E>,Enumerator<E>> dup() {
        return Pair.of(left, right);
    }
}
 