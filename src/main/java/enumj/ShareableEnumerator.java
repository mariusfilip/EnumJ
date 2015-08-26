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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@code Enumerator} with support for element sharing.
 * <p>
 * Shareable enumerators encapsulate an {@link Iterator} and spawn sharing
 * {@link Enumerator} instances that can share the elements without the
 * original iterator being traversed more than once.
 * </p>
 * <p>
 * Shareable enumerators work in one of the following three modes:
 * </p>
 * <ul>
 *   <li><em>shared mode</em> in which the enumerator spawns sharing
 * {@code Enumerator} instances</li>
 *   <li><em>shared enumerating mode</em> in which the sharing
 * {@code Enumerator} instances enumerate and share the elements
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
 * one of the spawned sharing {@code Enumerator} instances starts enumerating.
 * The <em>enumerating mode</em> commences with calling {@link #hasNext()} or
 * {@link #next()}.
 * </p>
 * <p>
 * The spawned {@code Enumerator} instances can traverse independently
 * the sequence of elements because {@link ShareableEnumerator} keeps a
 * transient internal buffer of elements, from the first in use to the last in
 * use. While the buffer allows for independent traversal of the same sequence,
 * the sharing {@code Enumerator} instances may not diverge too much without
 * danger of buffer overflow.
 * </p>
 * @param <E> type of shared elements
 * @see Enumerator
 * @see SharingEnumerator
 */
public class ShareableEnumerator<E> extends AbstractEnumerator<E> {

    private CachedEnumerable<E> source;
    private Enumerator<E>       direct;
    private boolean             isSharing;
    private AtomicBoolean       isEnumerating;
    private AtomicBoolean       isSharedEnumerating;

    /**
     * Creates a {@code ShareableEnumerator} instance that will share the
     * elements of the given {@code source}.
     *
     * @param source {@link Iterator} to share.
     * @see ShareableEnumerator
     */
    public ShareableEnumerator(Iterator<E> source) {
        this.source = new EnumeratorEnumerable(source).cached();
        this.isEnumerating = new AtomicBoolean(false);
        this.isSharedEnumerating = new AtomicBoolean(false);
    }

    @Override
    public ShareableEnumerator<E> asShareable() {
        return this;
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected boolean internalHasNext() {
        startEnumerating();
        return direct.hasNext();
    }
    @Override
    protected E internalNext() {
        return direct.next();
    }
    @Override
    protected void cleanup() {
        source = null;
        direct = null;
        isEnumerating = null;
        isSharedEnumerating = null;
    }

    // ---------------------------------------------------------------------- //

    /**
     * Creates an {@code Enumerator} instance that will share the
     * elements of the current enumerator.
     *
     * @return sharing {@link Enumerator} instance.
     * @see #share(int)
     */
    public Enumerator<E> share() {
        return share(1)[0];
    }

    /**
     * Creates an array of {@code Enumerator} instances that will share
     * the elements of the current enumerator.
     *
     * @param count the number of {@link Enumerator} instances to create.
     * @return array of new {@link Enumerator} instances.
     * @exception IllegalArgumentException <code>count</code> is negative
     * @exception IllegalStateException enumeration has started
     * @see #share()
     */
    public Enumerator<E>[] share(int count) {
        Checks.ensureNonNegative(count,
                                 Messages.NEGATIVE_ENUMERATOR_EXPECTED_COUNT);
        startSharing();

        final Enumerator<E>[] result = new Enumerator[count];
        for(int i=0; i<count; ++i) {
            result[i] = new SharingEnumerator(this, source.enumerator());
        }
        return result;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -//

    private void startSharing() {
        if (isEnumerating.get() || isSharedEnumerating.get()) {
            throw new IllegalStateException(Messages.ILLEGAL_ENUMERATOR_STATE);
        }
        isSharing = true;
    }
    private void startEnumerating() {
        if (isSharing || isSharedEnumerating.get()) {
            throw new IllegalStateException(Messages.ILLEGAL_ENUMERATOR_STATE);
        }
        if (isEnumerating.compareAndSet(false, true)) {
            source.disable();
            direct = source.enumerator();
            source = null;
        }
    }

    /**
     * Marks the <em>shared enumeration</em> state consisting in the
     * enumerators sharing this shareable enumerator starting their own
     * enumeration.
     *
     * @see SharingEnumerator#hasNext()
     */
    void startSharedEnumeration() {
        if (isSharedEnumerating.compareAndSet(false, true)) {
            source.disable();
            source = null;
        }
    }
}
