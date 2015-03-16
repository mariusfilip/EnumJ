/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

/**
 * Enumerator sharing the elements of a common {@link ShareableEnumerator}.
 * <p>
 * Sharing enumerators get spawned by {@link ShareableEnumerator} instances with
 * the purpose of sharing independently an enumerable sequence without
 * triggering the original enumeration multiple times.
 * </p>
 * <p>
 * A {@link ShareableEnumerator} enters its <em>shared mode</em> once it starts
 * spawning sharing enumerators. When spawned {@link SharingEnumerator}
 * instances start enumerating, the common {@link ShareableEnumerator} enters
 * its <em>shared enumerating mode</em> and further sharing is not possible.
 * </p>
 * <p>
 * {@link SharingEnumerator} instances can enumerate the shared elements
 * independently because the {@link ShareableEnumerator} behind them buffers
 * the elements being shared. Allowing too much divergence between the sharing
 * enumerators may lead to buffer overflows.
 * </p>
 * @param <E> type of shared elements
 * @see Enumerator
 * @see ShareableEnumerator
 */
public final class SharingEnumerator<E> extends AbstractEnumerator<E> {

    private ShareableEnumerator<E> source;
    private ShareableElement<E> consumedPtr;

    SharingEnumerator(ShareableEnumerator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    protected boolean internalHasNext() {
        return source.hasNext(consumedPtr);
    }

    @Override
    protected E internalNext() {
        consumedPtr = source.next(this, consumedPtr);

        assert consumedPtr != null;
        assert !consumedPtr.isFree();

        return consumedPtr.getValue();
    }

    @Override
    protected void cleanup() {
        source = null;
        if (consumedPtr != null) {
            assert !consumedPtr.isFree();
            consumedPtr.release(this);
            consumedPtr = null;
        }
    }
}
