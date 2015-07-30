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

import java.util.Random;

public class LongEnumerableComparator extends EnumerableStreamComparator<Long> {

    public LongEnumerableComparator(Random rnd,
                                    int    maxDepth,
                                    int    maxLength) {
        super(rnd, maxDepth, maxLength);

        newStreamOfSeed = s -> newEnumerableOfSeed.apply(s)
                                                  .asStream();
        predicateOfSeed = s -> {
            return x -> x <= limitOfSeed.apply(s);
        };
        limitOfSeed = s -> {
            final Random r = new Random(s);
            return Math.max(0, maxLength + 0L - r.nextInt(5));
        };
        mapperOfSeed = s -> {
            final Random r = new Random(s);
            final boolean add = r.nextBoolean();
            final boolean neg = r.nextBoolean();
            final long amount = r.nextInt(Math.max(1, maxLength/2));
            return x -> x*(neg ? -1 : 1) + amount * (add ? 1 : -1);
        };
        peekConsumerOfSeed = s -> {
            return x -> {
                if (13 == s % 100_003) {
                    System.out.println("Consuming " + x + " ...");
                }
            };
        };
        skipLimitOfSeed = s -> {
            final Random r = new Random(s);
            return (long)r.nextInt(5);
        };
        newEnumerableOfSeed = s -> {
            final Random r = new Random(s);
            return Enumerable.rangeLong(0, maxLength)
                             .map(i -> (long)r.nextInt(maxLength+1));
        };

        final int tempSeed = rnd.nextInt();
        lhs = newStreamOfSeed.apply(tempSeed);
        rhs = newEnumerableOfSeed.apply(tempSeed);
    }

    @Override
    protected StreamComparator<Long,Enumerable<Long>>
              subComparator(Random rnd,
                            int    maxDepth,
                            int    maxLength) {
                  return new LongEnumerableComparator(
                          rnd,
                          maxDepth,
                          maxLength);
              }
}
