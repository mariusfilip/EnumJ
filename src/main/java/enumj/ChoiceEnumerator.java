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
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Marius Filip
 */
class ChoiceEnumerator<E> extends AbstractEnumerator<E> {

    protected IntSupplier indexSupplier;
    protected IntUnaryOperator nextIndexSupplier;
    protected List<Iterator<E>> sources;
    protected E value;
    protected boolean isValue;

    public ChoiceEnumerator(IntSupplier indexSupplier,
                            IntUnaryOperator nextIndexSupplier,
                            Iterator<E> first,
                            Iterator<E> second,
                            Iterator<E>... rest) {
        Utils.ensureNotNull(indexSupplier,
                            Messages.NullEnumeratorGenerator);
        Utils.ensureNotNull(nextIndexSupplier,
                            Messages.NullEnumeratorGenerator);
        Utils.ensureNotNull(first, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(second, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(rest, Messages.NullEnumeratorSource);
        for(Iterator<E> source : rest) {
            Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        }
        this.indexSupplier = indexSupplier;
        this.nextIndexSupplier = nextIndexSupplier;
        this.sources = new ArrayList<>();
        this.sources.add(first);
        this.sources.add(second);
        this.sources.addAll(Arrays.asList(rest));
    }

    @Override
    protected boolean internalHasNext() {
        if (isValue) {
            return true;
        }
        if (sources.size() > 0) {
            int index = indexSupplier.getAsInt();
            int count = sources.size()-1;
            while (count >= 0) {
                if (sources.get(index) != null) {
                    if (sources.get(index).hasNext()) {
                        value = sources.get(index).next();
                        isValue = true;
                        return true;
                    }
                    sources.set(index, null);
                }
                index = nextIndexSupplier.applyAsInt(index);
                --count;
            }
        }
        return false;
    }
    @Override
    protected E internalNext() {
        isValue = false;
        return value;
    }
    @Override
    protected void cleanup() {
        indexSupplier = null;
        nextIndexSupplier = null;
        sources = null;
        value = null;
    }
}
