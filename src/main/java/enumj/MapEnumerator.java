/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.function.Function;

/**
 *
 * @author Marius Filip
 */
public class MapEnumerator<E, R> extends AbstractEnumerator<R> {

    private final Iterator<E> source;
    private final Function<? super E, ? extends R> mapper;

    public MapEnumerator(Iterator<E> source,
                         Function<? super E, ? extends R> mapper) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(mapper, Messages.NullEnumeratorMapper);
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    protected R nextValue() {
        return mapper.apply(source.next());
    }
}
