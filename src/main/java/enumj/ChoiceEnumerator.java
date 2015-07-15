/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

/**
 * {@link Enumerator} implementation that enumerates by choosing among the
 * elements of given source {@link Iterator} instances.
 * @param <E> Type of enumerated elements.
 */
final class ChoiceEnumerator<E> extends AbstractEnumerator<E> {

    private IntSupplier             indexSupplier;
    private IntUnaryOperator        nextIndexSupplier;
    private final List<Iterator<E>> sources;
    private final Nullable<E>       value;

    /**
     * Constructs a {@link ChoiceEnumerator} instance.
     * @param indexSupplier {@link IntSupplier} instance that yields the index
     * of the source {@link Iterator} instance to get the next element from.
     * @param nextIndexSupplier {@link IntSupplier} instance that yields the
     * subsequent indexes of the source {@link Iterator} instance to get the
     * next element from, if the source indicated by {@code indexSupplier} runs
     * out of elements.
     * @param first first {@link Iterator} source to choose elements from.
     * @param second second {@link Iterator} source to choose elements from.
     * @param rest rest of {@link Iterator} sources to choose elements from.
     */
    public ChoiceEnumerator(IntSupplier indexSupplier,
                            IntUnaryOperator nextIndexSupplier,
                            Iterator<E> first,
                            Iterator<E> second,
                            List<Iterator<E>> rest) {
        Utils.ensureNotNull(indexSupplier,
                            Messages.NULL_ENUMERATOR_GENERATOR);
        Utils.ensureNotNull(nextIndexSupplier,
                            Messages.NULL_ENUMERATOR_GENERATOR);
        Utils.ensureNotNull(first, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNotNull(second, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNotNull(rest, Messages.NULL_ENUMERATOR_SOURCE);
        for(Iterator<E> source : rest) {
            Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        }
        this.indexSupplier = indexSupplier;
        this.nextIndexSupplier = nextIndexSupplier;

        this.sources = new ArrayList<>();
        this.sources.add(first);
        this.sources.add(second);
        this.sources.addAll(rest);

        this.value = Nullable.empty();
    }

    @Override
    protected boolean internalHasNext() {
        if (value.isPresent()) {
            return true;
        }
        if (sources.size() > 0) {
            int index = indexSupplier.getAsInt();
            int count = sources.size()-1;
            while (count >= 0) {
                if (sources.get(index) != null) {
                    if (sources.get(index).hasNext()) {
                        value.set(sources.get(index).next());
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
        final E result = value.get();
        value.clear();
        return result;
    }
    @Override
    protected void cleanup() {
        indexSupplier = null;
        nextIndexSupplier = null;
        sources.clear();
        value.clear();
    }
}
