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
class ForkedEnumerator<E> extends AbstractEnumerator<E> {

    private final boolean isLeft;
    private ForkingPseudoEnumerator<E> source;

    ForkedEnumerator(ForkingPseudoEnumerator<E> source, boolean isLeft) {
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
