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

abstract class AbstractNumericEnumerator<T extends Number>
         extends AbstractEnumerator<T> {
    
    public static final byte INT_TYPE    = 0;
    public static final byte LONG_TYPE   = 1;
    public static final byte DOUBLE_TYPE = 2;
    
    private   int     type;    
    protected boolean boxIt = true;
    
    protected AbstractNumericEnumerator(byte type) {
        switch (type) {
            case INT_TYPE:
            case LONG_TYPE:
            case DOUBLE_TYPE:
                this.type = type;
            default:
                throw new IllegalArgumentException(
                        "Non-enumerable numeric type id: " + type);
        }
    }

    @Override
    protected final T internalNext() {
        switch(this.type) {
            case INT_TYPE:
                final int intResult = internalNextInt();
                if (boxIt) {
                    return (T)Integer.valueOf(intResult);
                }
                break;
            case LONG_TYPE:
                final long longResult = internalNextLong();
                if (boxIt) {
                    return (T)Long.valueOf(longResult);
                }
                break;
            case DOUBLE_TYPE:
                final double doubleResult = internalNextDouble();
                if (boxIt) {
                    return (T)Double.valueOf(doubleResult);
                }
                break;
            default:
                assert false;
        }
        return null;
    }
    
    protected int     internalNextInt() {
        throw new UnsupportedOperationException();
    }
    protected long    internalNextLong() {
        throw new UnsupportedOperationException();
    }
    protected double  internalNextDouble() {
        throw new UnsupportedOperationException();
    }
}

