/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Marius Filip
 */
public final class ShareableEnumerator<E> extends AbstractEnumerator<E> {

    private Iterator<E> source;
    private boolean isEnumerating;
    private boolean isSharing;
    private boolean isSharedEnumerating;
    private LinkedList<ShareableElement<E>> buffer;

    public ShareableEnumerator(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
        this.buffer = new LinkedList<>();
    }

    @Override
    public ShareableEnumerator<E> asShareable() {
        return this;
    }

    public SharingEnumerator<E> share() {
        return share(1)[0];
    }

    public SharingEnumerator<E>[] share(int count) {
        if (isEnumerating || isSharedEnumerating) {
            throw new IllegalStateException(Messages.IllegalIteratorState);
        }
        Utils.ensureNonNegative(count,
                                Messages.NegativeEnumeratorExpectedCount);
        isSharing = true;
        final SharingEnumerator<E>[] enumerators = new SharingEnumerator[count];
        for(int i=0; i<count; ++i) {
            enumerators[i] = new SharingEnumerator(this);
        }
        return enumerators;
    }

    @Override
    protected boolean mayContinue() {
        if (isSharing || isSharedEnumerating) {
            throw new IllegalStateException(Messages.IllegalIteratorState);
        }
        isEnumerating = true;
        if (source == null || buffer == null) {
            return false;
        }
        return source.hasNext();
    }

    @Override
    protected E nextValue() {
        if (isSharing || isSharedEnumerating) {
            throw new IllegalStateException(Messages.IllegalIteratorState);
        }
        isEnumerating = true;
        return source.next();
    }

    @Override
    protected void cleanup() {
        source = null;
        buffer.clear();
        buffer = null;
    }

    boolean hasNext(ShareableElement<E> consumedPtr) {
        if (isEnumerating) {
            throw new IllegalStateException(Messages.IllegalIteratorState);
        }
        isSharedEnumerating = true;
        return consumedPtr != null && consumedPtr.next != null
               || source.hasNext();
    }

    public ShareableElement<E> next(SharingEnumerator<E> enumerator,
                                    ShareableElement<E> consumedPtr) {
        if (isEnumerating) {
            throw new IllegalStateException(Messages.IllegalIteratorState);
        }
        isSharedEnumerating = true;
        boolean ok = false;

        ShareableElement<E> newPtr = next(consumedPtr);
        assert consumedPtr == null || consumedPtr.next == newPtr;

        try {
            newPtr.addRef(enumerator);
            if (consumedPtr != null) {
                consumedPtr.release(enumerator);
            }
            ok = true;
        }
        catch (Exception ex) {
            newPtr.release(enumerator);

            assert !buffer.isEmpty();
            if (newPtr == buffer.getLast()) {
                final ShareableElement<E> last = buffer.removeLast();
                assert last == newPtr;

                if (!buffer.isEmpty()) {
                    buffer.getLast().next = null;
                }
            }

            throw ex;
        }
        finally {
            if (!buffer.isEmpty() && buffer.getFirst().isFree()) {
                final ShareableElement<E> head = buffer.removeFirst();

                assert consumedPtr != null;
                assert consumedPtr == head;
                assert !ok || head.next != null;
                assert !ok || !buffer.isEmpty();
                assert !ok || buffer.getFirst() == newPtr;

                head.next = null;
            }
        }
        return newPtr;
    }

    private ShareableElement<E> next(ShareableElement<E> consumedPtr) {
        if (consumedPtr == null) {
            if (buffer.isEmpty()) {
                getNextElement();
            }
            return buffer.getFirst();
        }
        if (consumedPtr.next == null) {
            assert !buffer.isEmpty();
            assert consumedPtr == buffer.getLast();
            getNextElement();
        }
        return consumedPtr.next;
    }

    private void getNextElement() {
        final ShareableElement<E> nextPtr = new ShareableElement(source.next());
        final ShareableElement<E> tail = buffer.isEmpty()
                                         ? null
                                         : buffer.getLast();
        buffer.addLast(nextPtr);
        if (tail != null) {
            tail.next = nextPtr;
        }
    }
}
