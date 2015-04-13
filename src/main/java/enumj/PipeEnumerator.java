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

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;

class PipeEnumerator<E> extends AbstractEnumerator<E> {

    protected LinkedList<PipeProcessor> pipeline;
    protected LinkedList<PipeMultiProcessor> multiPipeline;
    protected LinkedList<PipeReference> sources;
    protected Nullable<E> value;

    PipeEnumerator(Iterator<E> source) {
        this();
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        PipeReference<?> ref = PipeReference.of(source);
        this.sources.add(ref);
    }
    PipeEnumerator() {
        this.pipeline = new LinkedList<>();
        this.multiPipeline = new LinkedList<>();
        this.sources = new LinkedList<>();
        this.value = Nullable.empty();
    }

    static <T> PipeEnumerator<T> of(Iterator<T> source) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return (source instanceof PipeEnumerator)
               ? (PipeEnumerator<T>)source
               : new PipeEnumerator(source);
    }

    // ---------------------------------------------------------------------- //

    protected <X> Enumerator<X> enqueue(
            PipeProcessor<? super E, ? extends X> processor) {
        PipeProcessor last = pipeline.peekLast();
        pipeline.addLast(processor);
        if (last != null) {
            last.next = processor;
        }
        if (!sources.isEmpty()) {
            sources.peekLast().setRefIfNull(processor);
        }
        return (Enumerator<X>)this;
    }
    protected <X> Enumerator<X> enqueue(
            PipeMultiProcessor<? super E, ? extends X> processor) {
        PipeProcessor last = pipeline.peekLast();
        boolean added = false;
        try {
            pipeline.addLast(processor);
            added = true;
            multiPipeline.addLast(processor);
        } catch(Exception ex) {
            if (added) {
                pipeline.removeLast();
            }
            throw ex;
        }
        if (last != null) {
            last.next = processor;
        }
        if (!sources.isEmpty()) {
            sources.pollLast().setRefIfNull(processor);
        }
        return (Enumerator<X>)this;
    }
    protected void dequeue() {
        if (!sources.isEmpty()) {
            if (sources.isEmpty()) {
                pipeline.clear();
                multiPipeline.clear();
            } else {
                while(!pipeline.isEmpty()
                      && pipeline.peekFirst() != sources.peekFirst().getRef()) {
                    PipeProcessor head = pipeline.remove();
                    if (!multiPipeline.isEmpty()
                        && head == multiPipeline.peekFirst()) {
                        multiPipeline.remove();
                    }
                }
            }
        }
    }
    protected boolean tryPipelineIn(Nullable<Object>        in,
                                    Nullable<PipeProcessor> processor) {
        in.clear();
        processor.clear();

        if (!multiPipeline.isEmpty()) {
            Iterator<PipeMultiProcessor> multiProcessorIterator =
                    multiPipeline.descendingIterator();
            PipeMultiProcessor multiProcessor;
            while(multiProcessorIterator.hasNext()) {
                multiProcessor = multiProcessorIterator.next();
                if (!multiProcessor.needsValue()) {
                    in.set(multiProcessor.getValue());
                    processor.set(multiProcessor.next);
                    return true;
                }
            }
        }
        while(!sources.isEmpty() && !sources.peekFirst().hasNext()) {
            dequeue();
        }
        if (sources.isEmpty()) {
            return false;
        }

        in.set(sources.peekFirst().next());
        processor.set(pipeline.peekFirst());
        return true;
    }
    protected boolean tryPipelineOut(Object            in,
                                     PipeProcessor     processor,
                                     Nullable<E>       out,
                                     Nullable<Boolean> nextOnNoValue) {
        out.clear();
        nextOnNoValue.clear();

        while(processor != null) {
            processor.process(in);
            if (processor.hasValue()) {
                in = processor.getValue();
            }
            else {
                nextOnNoValue.set(processor.nextOnNoValue());
                return false;
            }
            processor = processor.next;
        }

        out.set((E)in);
        return true;
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected boolean internalHasNext() {
        if (value.isPresent()) {
            return true;
        }
        Nullable<Object> in = Nullable.empty();
        Nullable<PipeProcessor> processor = Nullable.empty();
        Nullable<Boolean> nextOnNoValue = Nullable.empty();
        while(true) {
            if (!tryPipelineIn(in, processor)) {
                return false;
            }
            if (tryPipelineOut(in.get(),
                               processor.get(),
                               value,
                               nextOnNoValue)) {
                return true;
            }
            if (!nextOnNoValue.get()) {
                return false;
            }
        }
    }
    @Override
    protected E internalNext() {
        E result = value.get();
        value.clear();
        return result;
    }
    @Override
    protected void cleanup() {
        pipeline.clear();
        pipeline = null;
        multiPipeline.clear();
        multiPipeline = null;
        sources.clear();
        sources = null;
        value.clear();
        value = null;
    }

    // ---------------------------------------------------------------------- //

    @Override
    public <T> Enumerator<Pair<E,T>> cartesianProduct(
            Supplier<Iterator<T>> source) {
        return enqueue(new CartesianProductPipeProcessor(source));
    }
    @Override
    public Enumerator<E> concat(Iterator<? extends E> elements) {
        sources.addLast(PipeReference.of(elements));
        return this;
    }
    @Override
    public Enumerator<E> filter(Predicate<? super E> predicate) {
        return enqueue(new FilterPipeProcessor(predicate));
    }
    @Override
    public <R> Enumerator<R> flatMap(
        Function<? super E, ? extends Iterator<? extends R>> mapper) {
        return enqueue(new FlatMapPipeProcessor(mapper));
    }
    @Override
    public <X> Enumerator<X> map(
            Function<? super E, ? extends X> mapper) {
        return enqueue(new MapPipeProcessor(mapper));
    }
    @Override
    public Enumerator<E> skipWhile(Predicate<? super E> predicate) {
        return enqueue(new SkipPipeProcessor(predicate));
    }
    @Override
    public Enumerator<E> takeWhile(Predicate<? super E> predicate) {
        return enqueue(new WhilePipeProcessor(predicate));
    }
    @Override
    public Enumerator<Optional<E>[]> zipAll(Iterator<? extends E> first,
                                            Iterator<? extends E>... rest) {
        return zipAll(first, Arrays.asList(rest));
    }
    
    // ---------------------------------------------------------------------- //

    Enumerator<Optional<E>[]> zipAll(Iterator<? extends E> first,
                                     List<Iterator<? extends E>> rest) {
        PipeEnumerator<Optional<E>> optionalPipe =
                (PipeEnumerator<Optional<E>>)map(e -> Optional.of(e))
                .concat(Enumerator.of(() -> Optional.of(Optional.empty())));
        return optionalPipe.enqueue(new ZipPipeProcessor(first, rest));
    }    
}
