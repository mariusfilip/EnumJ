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

import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;

public class NanoTimer {

    public static <R> long nanos(Supplier<R> action, Supplier<String> msg) {
        System.out.println(msg.get());
        final long t0 = System.nanoTime();
        final R result = action.get();
        return System.nanoTime() - t0;
    }

    public static <U,R> long buildNanos(Supplier<U> construction,
                                        Function<U,R> action,
                                        Supplier<String> msg) {
        System.out.println("Construction: " + msg.get());
        final U input = construction.get();
        System.out.println("Action: " + msg.get());
        final long t0 = System.nanoTime();
        final R result = action.apply(input);
        return System.nanoTime() - t0;
    }

    public static <T,R> long[] nanos(Function<T,R> action,
                                     Function<T,String> msg,
                                     T... args) {
        final long[] results = new long[args.length];
        for(int i=0; i<results.length; ++i) {
            final T arg = args[i];
            results[i] = nanos(() -> action.apply(arg),
                               () -> msg.apply(arg));
        }
        return results;
    }

    public static <T,U,R> long[] buildNanos(Function<T,U> construction,
                                            Function<U,R> action,
                                            Function<T,String> msg,
                                            T... args) {
        final long[] results = new long[args.length];
        final Function<U,R> runner = in -> action.apply(in);
        for(int i=0; i<results.length; ++i) {
            final T arg = args[i];
            final Supplier<U> builder = () -> construction.apply(arg);
            results[i] = buildNanos(builder,
                                    runner,
                                    () -> msg.apply(arg));
        }
        return results;
    }

    public static <T,R1,R2> Pair<Long,Long>[] compareNanos(
            Function<T,R1> firstAction,
            Function<T,R2> secondAction,
            T... args) {
        final long[] firstResults = nanos(firstAction,
                                          arg -> "First: " + arg,
                                          args);
        final long[] secondResults = nanos(secondAction,
                                           arg -> "Second: " + arg,
                                           args);
        final Pair[] results = new Pair[args.length];
        for(int i=0; i<results.length; ++i) {
            results[i] = Pair.of(firstResults[i], secondResults[i]);
        }
        return results;
    }

    public static <T,U1,U2,R1,R2> Pair<Long,Long>[] compareBuildNanos(
            Function<T,U1> firstConstruction,
            Function<U1,R1> firstAction,
            Function<T,U2> secondConstruction,
            Function<U2,R2> secondAction,
            T... args) {
        final long[] firstResults = buildNanos(
                firstConstruction,
                firstAction,
                arg -> "First: " + arg,
                args);
        final long[] secondResults = buildNanos(
                secondConstruction,
                secondAction,
                arg -> "Second: " + arg,
                args);
        final Pair[] results = new Pair[args.length];
        for(int i=0; i<results.length; ++i) {
            results[i] = Pair.of(firstResults[i], secondResults[i]);
        }
        return results;
    }
}
