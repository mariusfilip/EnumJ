/*
 * The MIT License
 *
 * Copyright 2016 Marius Filip.
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

import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;

final class ValueFunction<T,R> implements Function<In<T>,Out<R>> {

    private final Out<R> out = new InOut<>();

    private int                 type;
    private Function<T,R>       function;
    private IntUnaryOperator    intFunction;
    private LongUnaryOperator   longFunction;
    private DoubleUnaryOperator doubleFunction;
    
    private final Consumer<In<T>> consumer = this::consume;
    private final Consumer<In<T>> intConsumer = this::consumeInt;
    private final Consumer<In<T>> longConsumer = this::consumeLong;
    private final Consumer<In<T>> doubleConsumer = this::consumeDouble;
    private final Consumer[]      consumers = {
        consumer,
        intConsumer,
        longConsumer,
        doubleConsumer
    };

    public ValueFunction(Function<T,R> function) {
        this.type = Value.GENERIC;
        this.function = function;
    }
    public ValueFunction(IntUnaryOperator intFunction) {
        this.type = Value.INT;
        this.intFunction = intFunction;
    }
    public ValueFunction(LongUnaryOperator longFunction) {
        this.type = Value.LONG;
        this.longFunction = longFunction;
    }
    public ValueFunction(DoubleUnaryOperator doubleFunction) {
        this.type = Value.DOUBLE;
        this.doubleFunction = doubleFunction;
    }

    @Override
    public Out<R> apply(In<T> value) {
        consumers[type].accept(value);
        return out;
    }
    
    private void consume(In<T> value) {
        out.set(function.apply(value.get()));
    }
    private void consumeInt(In<T> value) {
        out.setInt(intFunction.applyAsInt(value.getInt()));
    }
    private void consumeLong(In<T> value) {
        out.setLong(longFunction.applyAsLong(value.getLong()));
    }
    private void consumeDouble(In<T> value) {
        out.setDouble(doubleFunction.applyAsDouble(value.getDouble()));
    }
}
