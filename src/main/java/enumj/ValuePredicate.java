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
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

final class ValuePredicate<E> implements Predicate<In<E>> {

    private int             type;    
    private Predicate<E>    predicate;
    private IntPredicate    intPredicate;
    private LongPredicate   longPredicate;
    private DoublePredicate doublePredicate;
    private boolean         checked;
    
    private final Consumer<In<E>> checker = this::check;
    private final Consumer<In<E>> intChecker = this::checkInt;
    private final Consumer<In<E>> longChecker = this::checkLong;
    private final Consumer<In<E>> doubleChecker = this::checkDouble;
    private final Consumer[] checkers = {
        checker,
        intChecker,
        longChecker,
        doubleChecker
    };
    
    public ValuePredicate(Predicate<E> predicate) {
        this.type = Value.GENERIC;
        this.predicate = predicate;
    }
    public ValuePredicate(IntPredicate intPredicate) {
        this.type = Value.INT;
        this.intPredicate = intPredicate;
    }
    public ValuePredicate(LongPredicate longPredicate) {
        this.type = Value.LONG;
        this.longPredicate = longPredicate;
    }
    public ValuePredicate(DoublePredicate doublePredicate) {
        this.type = Value.DOUBLE;
        this.doublePredicate = doublePredicate;
    }
    
    @Override public boolean test(In<E> value) {
        checkers[type].accept(value);
        return checked;
    }
    
    public boolean cleared() { return this.type == Value.NONE; }
    public void    clear() {
        this.predicate = null;
        this.intPredicate = null;
        this.longPredicate = null;
        this.doublePredicate = null;
        this.type = Value.NONE;
    }
    
    private void check(In<E> value) {
        checked = predicate.test(value.get());
    }
    private void checkInt(In<E> value) {
        checked = intPredicate.test(value.getInt());
    }
    private void checkLong(In<E> value) {
        checked = longPredicate.test(value.getLong());
    }
    private void checkDouble(In<E> value) {
        checked = doublePredicate.test(value.getDouble());
    }
}
