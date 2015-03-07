/*
 * The MIT License
 *
 * Copyright 2015 Marius Filip.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package enumj;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.WeakHashMap;

/**
 * Type of {@link Enumerator} with support for element sharing.
 * <p>
 * Shareable enumerators encapsulate an {@link Iterator} and spawn
 * {@link SharingEnumerator} instances that can share the elements without the
 * original iterator being traversed more than once.
 * </p>
 * <p>
 * Shareable enumerators work in one of the following three modes:
 * </p>
 * <ul>
 *   <li><em>shared mode</em> in which the enumerator spawns
 * {@link SharingEnumerator} instances</li>
 *   <li><em>shared enumerating mode</em> in which the
 * {@link SharingEnumerator} instances enumerate and share the elements
 * independently of each other</li>
 *   <li><em>enumerating mode</em> in which the current enumerator yields
 * the elements directly, like any other enumerator</li>
 * </ul>
 * <p>
 * The second mode must come after the first mode and both are mutually
 * exclusive with the third.
 * </p>
 * <p>
 * The <em>shared mode</em> it triggered by calls to {@link #share()} or
 * {@link #share(int)}. The <em>shared enumerating mode</em> begins when
 * one of the spawned {@link SharingEnumerator} instances starts enumerating.
 * The <em>enumerating mode</em> commences with calling {@link #hasNext()} or
 * {@link #next()}.
 * </p>
 * <p>
 * The spawned {@link SharingEnumerator} instances can traverse independently the
 * sequence of elements because {@link ShareableEnumerator} keeps a transient
 * internal buffer of elements, from the first in use to the last in use. While
 * the buffer allows for independent traversal of the same sequence, the
 * {@link SharingEnumerator} instances may not diverge too much without the danger
 * of buffer overflow.
 * </p>
 * @param <E> type of shared elements
 * @see Enumerator
 * @see SharingEnumerator
 */
public final class ShareableEnumerator<E> extends AbstractEnumerator<E> {

    private Iterator<E> source;
    private boolean isEnumerating;
    private boolean isSharing;
    private boolean isSharedEnumerating;
    private LinkedList<ShareableElement<E>> buffer;
    private WeakHashMap<SharingEnumerator<E>, ShareableElement<E>> waiting;

    /**
     * Constructs an shareable enumerator out of a provided {@link Iterator}.
     * <p>
     * The elements of the provided {@link Iterator} will be shared without
     * the original iterator to be traversed more than once.
     * </p>
     * @param source {@link Iterator} sharing the elements
     * @exception IllegalArgumentException <code>source</code> is
     * <code>null</code>
     */
    public ShareableEnumerator(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
        this.buffer = new LinkedList<>();
        this.waiting = new WeakHashMap<>();
    }

    @Override
    public ShareableEnumerator<E> asShareable() {
        return this;
    }

    /**
     * Creates a {@link SharingEnumerator} instance that will share the
     * elements of the current enumerator.
     *
     * @return the new {@link SharingEnumerator} instance
     */
    public SharingEnumerator<E> share() {
        return share(1)[0];
    }

    /**
     * Creates an array of {@link SharingEnumerator} instances that will share
     * the elements of the current enumerator.
     *
     * @param count the number of {@link SharingEnumerator} to create
     * @return array of new {@link SharingEnumerator} instances
     * @exception IllegalArgumentException <code>count</code> is negative
     * @exception IllegalStateException enumeration has started
     */
    public SharingEnumerator<E>[] share(int count) {
        Utils.ensureNonNegative(count,
                                Messages.NegativeEnumeratorExpectedCount);
        if (isEnumerating || isSharedEnumerating) {
            throw new IllegalStateException(Messages.IllegalIteratorState);
        }
        Utils.ensureNonNegative(count,
                                Messages.NegativeEnumeratorExpectedCount);
        isSharing = true;
        final SharingEnumerator<E>[] enumerators = new SharingEnumerator[count];
        for(int i=0; i<count; ++i) {
            enumerators[i] = new SharingEnumerator(this);
            waiting.put(enumerators[i], null);
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
               || consumedPtr == null && !buffer.isEmpty()
               || source.hasNext();
    }

    ShareableElement<E> next(SharingEnumerator<E> enumerator,
                             ShareableElement<E> consumedPtr) {
        if (isEnumerating) {
            throw new IllegalStateException(Messages.IllegalIteratorState);
        }
        isSharedEnumerating = true;
        boolean ok = false;
        assert consumedPtr != null || waiting.containsKey(enumerator);

        ShareableElement<E> newPtr = next(consumedPtr);
        assert consumedPtr == null || consumedPtr.next == newPtr;

        try {
            newPtr.addRef(enumerator);
            if (consumedPtr != null) {
                consumedPtr.release(enumerator);
            }
            else {
                final ShareableElement<E> ptr = waiting.remove(enumerator);
                assert ptr == null;
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
        final ShareableElement<E> nextPtr = new ShareableElement(source.next(),
                                                                 waiting);
        final ShareableElement<E> tail = buffer.isEmpty()
                                         ? null
                                         : buffer.getLast();
        buffer.addLast(nextPtr);
        if (tail != null) {
            tail.next = nextPtr;
        }
    }
}
