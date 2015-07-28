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
final class SharingEnumerator<E> extends AbstractEnumerator<E> {
    
    private ShareableEnumerator<E> sharedSource;
    private Enumerator<E>          cachedSource;
    private Nullable<E>            value;
    
    SharingEnumerator(ShareableEnumerator<E> sharedSource,
                      Enumerator<E>          cachedSource) {
        Utils.ensureNotNull(sharedSource, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNotNull(cachedSource, Messages.NULL_ENUMERATOR_SOURCE);
        this.sharedSource = sharedSource;
        this.cachedSource = cachedSource;
        this.value = cachedSource.hasNext()
                     ? Nullable.of(cachedSource.next())
                     : Nullable.empty();
    }

    @Override
    protected boolean internalHasNext() {
        sharedSource.startSharedEnumeration();
        return value.isPresent();
    }
    @Override
    protected E internalNext() {
        final E result = value.get();
        if (cachedSource.hasNext()) {
            value.set(cachedSource.next());
        } else {
            value.clear();
        }
        return result;
    }
    @Override
    protected void cleanup() {
        sharedSource = null;
        cachedSource = null;
        value.clear();
        value = null;
    }
}
