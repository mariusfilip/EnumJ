/*
 * The MIT License
 *
 * Copyright 2015 Marius Filip.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package enumj;

import java.util.LinkedList;
import java.util.function.Function;

/**
 * Pipe processor that transforms inputs elements by mapping them to output
 * elements.
 *
 * @param <In> input value to map.
 * @param <Out> mapped output value.
 * @see FlatMapPipeProcessor
 * @see ZipPipeProcessor
 */
final class MapPipeProcessor<In,Out> extends AbstractPipeProcessor<In,Out> {

    private Function<In,Out>             mapper;
    private LinkedList<Function<In,Out>> mappers;
    private Out                          value;

    /**
     * Constructs a {@code MapPipeProcessor} instance.
     * <p>
     * The new {@link MapPipeProcessor} instance stores its {@code functor}
     * internally.
     * </p>
     *
     * @param functor {@link Function} mapping input elements to
     * output elements.
     */
    public MapPipeProcessor(Function<In,Out> functor) {
        super(false, false);
        mapper = functor;
    }

    // ---------------------------------------------------------------------- //

    @Override
    public <R> boolean pushFrontMap(Function<Out,R> mapper) {
        ensureMappers();
        this.mappers.addFirst((Function<In,Out>)mapper);
        return true;
    }
    @Override
    public <R> boolean enqueueMap(Function<Out,R> mapper) {
        ensureMappers();
        this.mappers.addLast((Function<In,Out>)mapper);
        return true;
    }
    private void ensureMappers() {
        if (mappers == null) {
            mappers = new LinkedList<>();
            mappers.add(mapper);
            mapper = null;
        }
    }

    // ---------------------------------------------------------------------- //

    @Override
    public void processInputValue(In value) {
        if (mapper != null) {
            value = (In)mapper.apply(value);
        }
        else {
            for(Function fun : mappers) {
                value = (In)fun.apply(value);
            }
        }
        this.value = (Out)value;
    }
    @Override
    public boolean hasOutputValue() {
        return true;
    }
    @Override
    protected Out retrieveOutputValue() {
        return value;
    }
    @Override
    protected void clearOutputValue() {
        value = null;
    }
    @Override
    public boolean isInactive() {
        return false;
    }
}
