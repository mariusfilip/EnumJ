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
package com.github.enumj;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;


public abstract class EnumerableStreamComparator<T>
                extends StreamComparator<T,Enumerable<T>> {

    protected EnumerableStreamComparator(Random rnd,
                                         int    maxDepth,
                                         int    maxLength) {
        super(rnd, maxDepth, maxLength);
    }

    public  IntFunction<Stream<T>>     newStreamOfSeed;
    public  IntFunction<Enumerable<T>> newEnumerableOfSeed;
    private Enumerator<T>              rhsEnumerator;

    @Override
    protected boolean rhsHasNext() {
        if (rhsEnumerator == null) {
            rhsEnumerator = rhs.enumerator();
        }
        return rhsEnumerator.hasNext();
    }
    @Override
    protected T rhsNext() {
        return rhsEnumerator.next();
    }
    @Override
    protected Enumerable<T> concat(Enumerable<T> first, Enumerable<T> second) {
        return first.concat(second);
    }
    @Override
    protected Enumerable<T> distinct(Enumerable<T> en) {
        return en.distinct();
    }
    @Override
    protected Enumerable<T> filter(Enumerable<T> en, Predicate<T> filter) {
        return en.filter(filter);
    }
    @Override
    protected Enumerable<T> flatMap(Enumerable<T> en,
                                    Function<T,Enumerable<T>> mapper) {
        return en.flatMap(mapper);
    }
    @Override
    protected Enumerable<T> limit(Enumerable<T> en, long limit) {
        return en.limit(limit);
    }
    @Override
    protected Enumerable<T> map(Enumerable<T> en, Function<T,T> mapper) {
        return en.map(mapper);
    }
    @Override
    protected Enumerable<T> peek(Enumerable<T> en, Consumer<T> peekConsumer) {
        return en.peek(peekConsumer);
    }
    @Override
    protected Enumerable<T> skip(Enumerable<T> en, long n) {
        return en.skip(n);
    }
    @Override
    protected Enumerable<T> sorted(Enumerable<T> en) {
        return en.sorted();
    }
}
