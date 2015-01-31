/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.mutable.Mutable;

/**
 *
 * @author Marius Filip
 */
abstract class RetrievingEnumerator<E> extends BasicEnumerator<E> {

    private Optional<Mutable<E>> value;
    private ValueRetriever<E> retriever;

    public RetrievingEnumerator(Iterator<E> source,
                                Function<Iterator<E>, ValueRetriever<E>> retriever) {
        super(source);
        this.value = null;
        this.retriever = retriever.apply(source);
    }

    @Override
    public boolean hasNext() {
        if (value == null) {
            value = retriever.get();
        }
        return value.isPresent();
    }

    @Override
    protected E nextValue() {
        E existingValue = value.get().getValue();
        value = retriever.get();
        return existingValue;
    }
}
