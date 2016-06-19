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

import java.util.NoSuchElementException;

interface In<T> extends Value<T> {
    default <U> Out<U> castOut() { return (Out<U>)(Value)this; }
}
interface Out<T> extends Value<T> {
    default <U> In<U> castIn() { return (In<U>)(Value)this; }
}

final class InOut<T> implements In<T>, Out<T> {

    private final SingleValue<T> value;

    public InOut() {
        this.value = new SingleValue();
    }
    public InOut(T value) {
        this.value = new SingleValue(value);
    }
    public InOut(int value) {
        this.value = new SingleValue(value);
    }
    public InOut(long value) {
        this.value = new SingleValue(value);
    }
    public InOut(double value) {
        this.value = new SingleValue(value);
    }
    public InOut(InOut<? extends T> value) {
        this.value = new SingleValue(value.value);
    }

    @Override public boolean isPresent() {
        return value.isPresent();
    }
    @Override public boolean intIsPresent() {
        return value.intIsPresent();
    }
    @Override public boolean longIsPresent() {
        return value.longIsPresent();
    }
    @Override public boolean doubleIsPresent() {
        return value.doubleIsPresent();
    }

    @Override public T get() throws NoSuchElementException {
        return value.get();
    }
    @Override public int getInt() throws NoSuchElementException {
        return value.getInt();
    }
    @Override public long getLong() throws NoSuchElementException {
        return value.getLong();
    }
    @Override public double getDouble() throws NoSuchElementException {
        return value.getDouble();
    }

    @Override public void set(T value) {
        this.value.set(value);
    }
    @Override public void setInt(int value) {
        this.value.setInt(value);
    }
    @Override public void setLong(long value) {
        this.value.setLong(value);
    }
    @Override public void setDouble(double value) {
        this.value.setDouble(value);
    }
    @Override public void setValue(Value<? extends T> value) {
        this.value.setValue(value);
    }

    @Override public int getType() { return value.getType(); }
    @Override public void clear() { value.clear(); }
}
