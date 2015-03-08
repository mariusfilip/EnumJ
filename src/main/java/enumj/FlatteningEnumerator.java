/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Marius Filip
 */
class FlatteningEnumerator<E> extends AbstractEnumerator<E> {

    protected LinkedList<Enumerator<E>> sources;

    public FlatteningEnumerator(Iterator<Iterator<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.sources = new LinkedList<>();
        while(source.hasNext()) {
            this.sources.addLast(Enumerator.of(source.next()));
        }
    }

    @Override
    protected boolean mayContinue() {
        while (sources.size() > 0) {
            if (sources.element().hasNext()) {
                return true;
            }
            sources.remove();
        }
        return false;
    }
    @Override
    protected E nextValue() {
        return sources.element().next();
    }
    @Override
    protected void cleanup() {
        sources = null;
    }

    // ---------------------------------------------------------------------- //

    public Enumerator<E> prepend(Iterator<? extends E> elements) {
        sources.addFirst((Enumerator<E>)Enumerator.of(elements));
        return this;
    }

    public Enumerator<E> concat(Iterator<? extends E> elements) {
        sources.addLast((Enumerator<E>)Enumerator.of(elements));
        return this;
    }

    // ---------------------------------------------------------------------- //

    private <R> Enumerator<R> replace(Function<Enumerator<E>,
                                               Enumerator<R>> transform) {
        final Enumerator<E> last = sources.getLast();
        final Enumerator<R> result = transform.apply(last);
        if (result != last) {
            boolean removed = false;
            try {
                sources.removeLast(); removed = true;
                sources.addLast((Enumerator<E>)result);
            } catch(Exception ex) {
                if (removed) {
                    sources.addLast(last);
                }
                throw ex;
            }
        }
        return (Enumerator<R>)this;
    }

    @Override
    public Enumerator<E> filter(Predicate<? super E> predicate) {
        return replace(e -> e.filter(predicate));
    }
    @Override
    public <R> Enumerator<R> map(
            Function<? super E, ? extends R> mapper) {
        return replace(e -> e.map(mapper));
    }
    @Override
    public Enumerator<E> skipWhile(Predicate<? super E> predicate) {
        return replace(e -> e.skipWhile(predicate));
    }
    @Override
    public Enumerator<E> takeWhile(Predicate<? super E> predicate) {
        return replace(e -> e.takeWhile(predicate));
    }
}
