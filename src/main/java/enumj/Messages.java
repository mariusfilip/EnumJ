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

public final class Messages {
    
    private Messages() {}

    public static final String NULL_ENUMERATOR_SOURCE =
            "Null enumerator source";
    public static final String NULL_ENUMERATOR_PREDICATE =
            "Null enumerator predicate";
    public static final String NULL_ENUMERATOR_HANDLER =
            "Null enumerator handler";
    public static final String NULL_ENUMERATOR_MAPPER =
            "Null enumerator mapper";
    public static final String NULL_ENUMERATOR_GENERATOR =
            "Null enumerator generator";
    public static final String NULL_ENUMERATOR_CONSUMER =
            "Null enumerator consumer";
    public static final String NULL_ENUMERATOR_COMPARATOR =
            "Null enumerator comparator";
    public static final String NULL_ENUMERATOR_CLASS =
            "Null enumerator class";
    public static final String NULL_ENUMERATOR_ACCUMULATOR =
            "Null enumerator accumulator";
    public static final String NULL_ITERATOR =
            "Null iterator";
    public static final String NULL_PIPE_PROCESSOR_REFERENCE =
            "Null pipe processor reference";

    public static final String NEGATIVE_ENUMERATOR_SIZE =
            "Negative enumerator size";
    public static final String NEGATIVE_ENUMERATOR_INDEX =
            "Negative enumerator index";
    public static final String NEGATIVE_ENUMERATOR_EXPECTED_COUNT =
            "Negative enumerator expected count";
    public static final String NEGATIVE_RETRIES =
            "Negative enumerator retries";

    public static final String ILLEGAL_ENUMERATOR_STATE =
            "Illegal enumerator state";
    public static final String ILLEGAL_MULTIPLE_ENUMERATIONS =
            "Illegal multiple enumerations";

    public static final String NO_SINGLE_ENUMERATOR_ELEMENT =
            "No single enumerator element";
    public static final String OVERFLOWED_ENUMERATOR_INDEX =
            "Overflowed enumerator index";
}
