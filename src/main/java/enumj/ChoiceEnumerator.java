/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntSupplier;

/**
 *
 * @author Marius Filip
 */
public class ChoiceEnumerator<E> extends AbstractEnumerator<E> {

    private IntSupplier indexSupplier;
    private List<Iterator<E>> sources;
    private boolean hasNoNext;
    private boolean definitelyHasNext;

    public ChoiceEnumerator(IntSupplier indexSupplier,
                            Iterator<E> first,
                            Iterator<E> second,
                            Iterator<E>... rest) {
        Utils.ensureNotNull(indexSupplier, Messages.NullEnumeratorGenerator);
        Utils.ensureNotNull(first, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(second, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(rest, Messages.NullEnumeratorSource);
        for(Iterator<E> source : rest) {
            Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        }
        this.indexSupplier = indexSupplier;
        this.sources = new ArrayList<>();
        this.sources.add(first);
        this.sources.add(second);
        this.sources.addAll(Arrays.asList(rest));
        this.hasNoNext = false;
        this.definitelyHasNext = false;
    }

    @Override
    public boolean hasNext() {
        if (hasNoNext) {
            indexSupplier = null;
            sources = null;
            return !hasNoNext;
        }
        if (definitelyHasNext) {
            return definitelyHasNext;
        }
        for(Iterator<E> source : sources) {
            if (source.hasNext()) {
                return true;
            }
        }
        hasNoNext = true;
        definitelyHasNext = false;
        return false;
    }

    @Override
    protected E nextValue() {
        int index = indexSupplier.getAsInt();
        Utils.ensureNonNegative(index, Messages.NegativeEnumeratorIndex);
        Utils.ensureLessThan(index, sources.size(), Messages.OverflowedEnumeratorIndex);

        while (!sources.get(index).hasNext()) {
            index = (index + 1) % sources.size();
        }
        E result = sources.get(index).next();
        definitelyHasNext = sources.get(index).hasNext();
        return result;
    }
}
