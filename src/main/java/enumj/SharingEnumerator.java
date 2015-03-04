/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

/**
 *
 * @author Marius Filip
 */
public final class SharingEnumerator<E> extends AbstractEnumerator<E> {

    private ShareableEnumerator<E> source;
    private ShareableElement<E> consumedPtr;

    SharingEnumerator(ShareableEnumerator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    protected boolean mayContinue() {
        return source.hasNext(consumedPtr);
    }

    @Override
    protected E nextValue() {
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
