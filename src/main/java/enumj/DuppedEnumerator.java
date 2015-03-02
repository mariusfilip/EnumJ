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
final class DuppedEnumerator<E> extends AbstractEnumerator<E> {

    private DuppingPseudoEnumerator<E> source;
    private final boolean isLeft;

    DuppedEnumerator(DuppingPseudoEnumerator<E> source, boolean isLeft) {
        assert source != null;
        this.source = source;
        this.isLeft = isLeft;
    }

    @Override
    protected boolean mayContinue() {
        return source.hasNext(isLeft);
    }
    @Override
    protected E nextValue() {
        return source.next(isLeft);
    }
    @Override
    protected void cleanup() {
        source = null;
    }
}
