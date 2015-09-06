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

import java.util.Comparator;
import java.util.Enumeration;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumeratorTest {

    public EnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEnumerating() {
        System.out.println("enumerating");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator())
                .forEach(en -> assertFalse(en.enumerating()));
    }

    @Test
    public void testOn() {
        System.out.println("on");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().onEnumerator(),
                                  p.getRight().onEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOf_Array() {
        System.out.println("of array");
        assertTrue(Enumerator.of(Enumerator.on(1, 2, 3)
                                           .toArray(Integer.class))
                             .elementsEqual(Enumerator.on(1, 2, 3)));
    }

    @Test
    public void testOf_Iterator() {
        System.out.println("of iterator");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofIteratorEnumerator(),
                                  p.getRight().ofIteratorEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofIteratorEnumerator(),
                                  p.getRight().onEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOf_Iterable() {
        System.out.println("of iterable");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofIterableEnumerator(),
                                  p.getRight().ofIterableEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofIterableEnumerator(),
                                  p.getRight().ofIteratorEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOf_Enumeration() {
        System.out.println("of enumeration");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofEnumerationEnumerator(),
                                  p.getRight().ofEnumerationEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofEnumerationEnumerator(),
                                  p.getRight().ofIterableEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOf_Stream() {
        System.out.println("of stream");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofStreamEnumerator(),
                                  p.getRight().ofStreamEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofStreamEnumerator(),
                                  p.getRight().ofEnumerationEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOf_Spliterator() {
        System.out.println("of spliterator");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofSpliteratorEnumerator(),
                                  p.getRight().ofSpliteratorEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofSpliteratorEnumerator(),
                                  p.getRight().ofStreamEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOf_Supplier() {
        System.out.println("of supplier");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofSupplierEnumerator(),
                                  p.getRight().ofSupplierEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofSupplierEnumerator(),
                                  p.getRight().ofSpliteratorEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOfLazyIterator() {
        System.out.println("ofLazyIterator");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazyIteratorEnumerator(),
                                  p.getRight().ofIteratorEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOfLazyIterable() {
        System.out.println("ofLazyIterable");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazyIterableEnumerator(),
                                  p.getRight().ofIterableEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOfLazyEnumeration() {
        System.out.println("ofLazyEnumeration");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazyEnumerationEnumerator(),
                                  p.getRight().ofEnumerationEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOfLazyStream() {
        System.out.println("ofLazyStream");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazyStreamEnumerator(),
                                  p.getRight().ofStreamEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOfLazySpliterator() {
        System.out.println("ofLazySpliterator");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazySpliteratorEnumerator(),
                                  p.getRight().ofSpliteratorEnumerator()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testOfLateBinding() {
        System.out.println("ofLateBinding");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLateBindingEnumerator(),
                                  p.getRight().enumerator()
                                              .asShareable()
                                              .share(2)))
                .map(p -> {
                    final LateBindingEnumerator<Double> en1 = p.getLeft();
                    final Enumerator<Double>[] sens = p.getRight();
                    
                    en1.bind(sens[0]);
                    return Pair.of(en1, sens[1]);
                })
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testAs() {
        System.out.println("as");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()
                                              .as(Integer.class)
                                              .as(Double.class)))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    @Test
    public void testAsFiltered() {
        System.out.println("as filtered");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator().asFiltered(Integer.class))
                .forEach(e -> e.elementsEqual(Enumerator.empty()));
    }

    @Test
    public void testAsIterable() {
        System.out.println("asIterable");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()
                                              .asEnumerable()))
                .forEach(p -> {
                    long c = p.getLeft().count();
                    for(double d : p.getRight()) {
                        --c;
                    }
                    assertEquals(0, c);
                });
    }

    @Test
    public void testAsEnumeration() {
        System.out.println("asEnumeration");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()
                                              .asEnumeration()))
                .forEach(p -> {
                    long c = p.getLeft().count();
                    final Enumeration<Double> e = p.getRight();
                    while (e.hasMoreElements()) {
                        e.nextElement();
                        --c;
                    }
                    assertEquals(0, c);
                });
    }

    @Test
    public void testAsOptional() {
        System.out.println("asOptional");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerator()
                                             .asOptional(),
                                  p.getRight().enumerator()))
                .map(p -> Pair.of(p.getLeft().takeWhile(e -> e.isPresent())
                                             .map(e -> e.get()),
                                  p.getRight()))
                .forEach(p -> p.getLeft()
                               .elementsEqual(p.getRight()));
    }

    @Test
    public void testAsSpliterator() {
        System.out.println("asSpliterator");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()
                                              .asSpliterator()))
                .forEach(p -> {
                    MutableLong c = new MutableLong(p.getLeft().count());
                    final Spliterator<Double> s = p.getRight();
                    s.forEachRemaining(d -> c.decrement());
                    assertEquals(0, c.longValue());
                });
    }

    @Test
    public void testAsStream() {
        System.out.println("asStream");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()
                                              .asStream()))
                .forEach(p -> {
                    assertEquals(p.getLeft().count(), p.getRight().count());
                });
    }

    @Test
    public void testAsSupplier() {
        System.out.println("asSupplier");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()
                                              .asSupplier()))
                .forEach(p -> {
                    long c = p.getLeft().count();
                    Supplier<Optional<Double>> supplier = p.getRight();
                    Optional<Double> d = supplier.get();
                    while (d.isPresent()) {
                        --c;
                        d = supplier.get();
                    }
                    assertEquals(0, c);
                });
    }

    @Test
    public void testAsShareable_SameElements() {
        System.out.println("asShareable_SameElements");
        EnumeratorGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()
                                              .asShareable()))
                .forEach(p -> {
                    assertTrue(p.getLeft().elementsEqual(p.getRight()));
                });
    }

    @Test
    public void testAsShareable_ConcatOfParts() {
        System.out.println("asShareable_ConcatOfParts");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator())
                .map(e -> e.asShareable())
                .map(e -> e.share(3))
                .forEach(es -> {
                    final long c1 = es[0].count();
                    final long c2 = es[1].filter(x -> x<0).count();
                    final long c3 = es[2].filter(x -> x>=0).count();
                    assertEquals(c1, c2+c3);
                });
    }

    @Test
    public void testAsShareable_SumOfOpposites() {
        System.out.println("asShareable_SumOfOpposites");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator())
                .map(e -> e.asShareable())
                .map(e -> e.share(2))
                .forEach(es -> {
                    final Enumerator<Double> plus = es[0];
                    final Enumerator<Double> minus = es[1].map(x -> -x);
                    final double sum = plus.zipAny(minus)
                            .map(p -> p.getLeft().get() + p.getRight().get())
                            .reduce(0.0, (x,y) -> x+y);
                    assertTrue(sum < 0.000001);
                    assertTrue(sum > -0.000001);
                });
    }

    @Test
    public void testAsShareable_TakeSkip() {
        System.out.println("asShareable_TakeSkip");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator())
                .map(e -> e.limit(10))
                .map(e -> e.asShareable())
                .map(e -> e.share(4))
                .forEach(es -> {
                    final long count = es[0].count();
                    final Enumerator<Double> first = es[1].take(count/2);
                    final Enumerator<Double> second = es[2].skip(count/2);
                    assertTrue(es[3].elementsEqual(first.concat(second)));
                });
    }

    @Test
    public void testAllMatch() {
        System.out.println("allMatch");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator())
                .map(e -> e.map(x -> x*x))
                .forEach(e -> { assertTrue(e.allMatch(x -> x>=0)); });
        final Random rnd = new Random(100);
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator())
                .map(e -> e.append(-1.0, -0.5, 0.0, 0.25, 0.75))
                .map(e -> e.map((Double d) -> Pair.of(d, rnd.nextDouble()))
                           .sorted((p1, p2) -> Double.compare(p1.getRight(),
                                                              p2.getRight()))
                           .map(p -> p.getLeft()))
                .map(e -> e.map(x -> x*x))
                .forEach(e -> { assertFalse(e.allMatch(x -> x>1)); });
    }

    @Test
    public void testAnyMatch() {
        System.out.println("anyMatch");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator().asShareable().share(3))
                .filter(es -> es[0].first().isPresent())
                .map(es -> es[1].skip(1)
                                .concat(Enumerator.on(Math.abs(es[2].first()
                                                                    .get()))))
                .forEach(e -> { assertTrue(e.anyMatch(x -> x >= 0)); });
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator())
                .map(e -> e.map(d -> -Math.abs(d)))
                .forEach(e -> { assertFalse(e.anyMatch(x -> x > 0)); });
    }

    @Test
    public void testAppend() {
        System.out.println("append");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator().asShareable().share(2))
                .forEach(es -> {
                    assertTrue(
                        Enumerator
                            .on(1.0, 2.0, 3.0)
                            .elementsEqual(
                                es[0].append(1.0, 2.0, 3.0)
                                     .skip(es[1].count())));
                });
    }

    @Test
    public void testCollect() {
        System.out.println("collect");
        EnumeratorGenerator
                .generators()
                .limit(100)
                .map(g -> g.enumerator().asShareable().share(2))
                .forEach(es -> {
                    assertTrue(
                        es[0].elementsEqual(
                            Enumerator.of(es[1].collect(Collectors.toList()))));
                });
    }

    @Test
    public void testConcat_Iterator() {
        System.out.println("concat iterator");
        assertTrue(Enumerator.on(1, 2, 3)
                             .concat(Enumerator.on(4, 5))
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testConcat_Iterable() {
        System.out.println("concat iterable");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asEnumerable())
                               .count(), 5);
    }

    @Test
    public void testConcat_Enumeration() {
        System.out.println("concat enumeration");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asEnumeration())
                               .count(), 5);
    }

    @Test
    public void testConcat_Stream() {
        System.out.println("concat stream");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asStream())
                               .count(), 5);
    }

    @Test
    public void testConcat_Spliterator() {
        System.out.println("concat spliterator");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asSpliterator())
                               .count(), 5);
    }

    @Test
    public void testConcat_Supplier() {
        System.out.println("concat supplier");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asSupplier())
                               .count(), 5);
    }

    @Test
    public void testConcat_On() {
        System.out.println("concat on");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concatOn(4, 5)
                               .count(), 5);
    }

    @Test
    public void testContains() {
        System.out.println("contains");
        Supplier<Enumerator<Integer>> supplier = () -> Enumerator.rangeInt(0, 10);
        for(Integer i : supplier.get().asEnumerable()) {
            assertTrue(supplier.get().contains(i));
            assertTrue(!supplier.get().contains(-i-1));
        }
    }

    @Test
    public void testCount() {
        System.out.println("count");
        assertEquals(Enumerator.on(1, 2, 3).count(), 3);
    }

    @Test
    public void testDistinct() {
        System.out.println("distinct");
        assertTrue(Enumerator.on(1, 2, 3)
                             .elementsEqual(Enumerator.on(1, 2, 3)
                                                      .distinct()));
    }

    @Test
    public void testElementAt() {
        System.out.println("elementAt");
        Supplier<Enumerator<Integer>> supply = () -> Enumerator.rangeInt(0, 10);
        for(int i=0; i<supply.get().count(); ++i) {
            assertEquals(Integer.valueOf(i), supply.get().elementAt(i).get());
        }
    }

    @Test
    public void testElementsEqual() {
        System.out.println("elementsEqual");
        assertTrue(Enumerator.on(1, 2, 3)
                             .elementsEqual(Enumerator.on(1, 2, 3)));
        assertTrue(!Enumerator.on(1, 2, 3)
                              .elementsEqual(Enumerator.on(1, 2, 3, 4)));
        assertTrue(!Enumerator.on(1, 2, 3)
                              .elementsEqual(Enumerator.on(1, 2, 4)));
    }

    @Test
    public void testEmpty() {
        System.out.println("empty");
        assertEquals(Enumerator.empty().count(), 0);
    }

    @Test
    public void testFilter() {
        System.out.println("filter");
        assertTrue(Enumerator.rangeInt(0, 10)
                             .filter(i -> 0 != i%2)
                             .elementsEqual(Enumerator.iterate(1, i -> i+2)
                                                      .takeWhile(i -> i < 10)));
    }

    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
        assertTrue(Enumerator.rangeInt(0, 10)
                             .flatMap(i -> Enumerator.rangeInt(i*10, (i+1)*10))
                             .elementsEqual(Enumerator.rangeInt(0, 100)));
        assertTrue(Enumerator.on(1, 2, 3)
                             .flatMap(i -> Enumerator.rangeInt(i*10, (i+1)*10))
                             .elementsEqual(Enumerator.rangeInt(10, 40)));
    }

    @Test
    public void testForEach() {
        System.out.println("forEach");
        final MutableInt m = new MutableInt(0);
        Enumerator.rangeInt(0, 100)
                  .forEach(i -> m.setValue(m.intValue()+1));
        assertEquals(m.intValue(), Enumerator.rangeInt(0, 100).count());
    }

    @Test
    public void testIndexedMap() {
        System.out.println("indexedMap");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .map((x,i) -> x - i)
                             .allMatch(x -> x == 0));
    }

    @Test
    public void testIterate() {
        System.out.println("iterate");
        assertTrue(Enumerator.iterate(0, i -> i+2)
                             .takeWhile(i -> i<100)
                             .elementsEqual(Enumerator.rangeInt(0, 100)
                                                      .filter(i -> 0 == i%2)));
    }

    @Test
    public void testLast() {
        System.out.println("last");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .last()
                               .get()
                               .intValue(), 99);
    }

    @Test
    public void testLimit() {
        System.out.println("limit");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .limit(10)
                               .count(), 10);
    }

    @Test
    public void testLimitWhile() {
        System.out.println("limitWhile");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .limitWhile(i -> i < 10)
                             .elementsEqual(Enumerator.rangeInt(0, 10)));
    }

    @Test
    public void testMap() {
        System.out.println("map");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .map(i -> 2*i)
                             .elementsEqual(Enumerator.iterate(0, i -> i+2)
                                                      .takeWhile(i -> i<200)));
    }

    @Test
    public void testMax() {
        System.out.println("max");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .max(Comparator.comparingInt(i -> i))
                               .get()
                               .intValue(), 99);
    }

    @Test
    public void testMin() {
        System.out.println("min");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .min(Comparator.comparingInt(i -> i))
                               .get()
                               .intValue(), 0);
    }

    @Test
    public void testNoneMatch() {
        System.out.println("noneMatch");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .noneMatch(i -> i<0));
        assertTrue(!Enumerator.rangeInt(0, 100)
                              .noneMatch(i -> i<1));
    }

    @Test
    public void testPeek() {
        System.out.println("peek");
        assertEquals(Enumerator.rangeInt(0, 10)
                               .peek(i -> System.out.println(i))
                               .count(), 10);
    }

    @Test
    public void testPrepend_Iterator() {
        System.out.println("prepend iterator");
        assertTrue(Enumerator.on(4, 5)
                             .prepend(Enumerator.on(1, 2, 3))
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testPrepend_Iterable() {
        System.out.println("prepend iterable");
        assertEquals(Enumerator.on(4, 5)
                               .prepend(Enumerator.on(1, 2, 3).asEnumerable())
                               .count(), 5);
    }

    @Test
    public void testPrepend_Enumeration() {
        System.out.println("prepend enumeration");
        assertEquals(Enumerator.on(4, 5)
                               .prepend(Enumerator.on(1, 2, 3).asEnumeration())
                               .count(), 5);
    }

    @Test
    public void testPrepend_Stream() {
        System.out.println("prepend stream");
        assertEquals(Enumerator.on(4, 5)
                               .prepend(Enumerator.on(1, 2, 3).asStream())
                               .count(), 5);
    }

    @Test
    public void testPrepend_Spliterator() {
        System.out.println("prepend spliterator");
        assertEquals(Enumerator.on(4, 5)
                               .prepend(Enumerator.on(1, 2, 3).asSpliterator())
                               .count(), 5);
    }

    @Test
    public void testPrepend_Supplier() {
        System.out.println("prepend supplier");
        assertEquals(Enumerator.on(4, 5)
                               .prepend(Enumerator.on(1, 2, 3).asSupplier())
                               .count(), 5);
    }

    @Test
    public void testPrepend_On() {
        System.out.println("prepend on");
        assertEquals(Enumerator.on(4, 5)
                               .prependOn(1, 2, 3)
                               .count(), 5);
    }

    @Test
    public void testReduce() {
        System.out.println("reduce");
        Enumerator<Long> empty = Enumerator.empty();
        assertTrue(!empty.reduce((x, y) -> x+y).isPresent());
        assertEquals(Enumerator.rangeIntClosed(1, 10)
                               .reduce((x, y) -> x+y)
                               .get()
                               .intValue(),
                     10*(10+1)/2);
    }

    @Test
    public void testReduce_Identity() {
        System.out.println("reduce with identity");
        assertEquals(Enumerator.rangeLongClosed(1, 10)
                               .reduce(0L, (x, y) -> x+y)
                               .longValue(),
                     10L*(10+1)/2);
    }

    @Test
    public void testRepeat() {
        System.out.println("repeat");
        assertTrue(Enumerator.repeat(1, 3)
                             .elementsEqual(Enumerator.on(1, 1, 1)));
    }

    @Test
    public void testRepeatAll_Static() {
        System.out.println("repeat all static");
        assertTrue(Enumerator.repeatAll(() -> Enumerator.on(1, 2, 3),
                                        3)
                             .elementsEqual(Enumerator.on(1, 2, 3,
                                                          1, 2, 3,
                                                          1, 2, 3)));
    }

    @Test
    public void testRepeatAll() {
        System.out.println("repeat all");
        assertTrue(
                Enumerator.on(1, 2, 3, 4, 5)
                          .repeatAll(10)
                          .elementsEqual(
                                  Enumerator.repeatAll(
                                          () -> Enumerator.on(1, 2, 3, 4, 5),
                                          10)));
    }

    @Test
    public void testRepeatEach() {
        System.out.println("repeat each");
        assertTrue(
                Enumerator
                        .on(1, 2, 3, 4, 5)
                        .repeatEach(10)
                        .elementsEqual(
                                Enumerator.on(1, 2, 3, 4, 5)
                                          .flatMap(x ->
                                                   Enumerator.repeat(x, 10))));
    }

    @Test
    public void testReverse() {
        System.out.println("reverse");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .reverse()
                               .last()
                               .get()
                               .intValue(), 0);
    }

    @Test
    public void testSingle() {
        System.out.println("single");
        assertEquals(Enumerator.on(1).single(), Integer.valueOf(1));
    }

    @Test(expected = InputMismatchException.class)
    public void testSingle_None() {
        System.out.println("single none");
        Enumerator.empty().single();
    }

    @Test(expected = InputMismatchException.class)
    public void testSingle_Dup() {
        System.out.println("single dup");
        Enumerator.rangeInt(0, 2).single();
    }

    @Test
    public void testSkip() {
        System.out.println("skip");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .skip(10)
                               .count(), 90);
    }

    @Test
    public void testSkipWhile() {
        System.out.println("skipWhile");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .skipWhile(i -> i<30)
                               .count(), 70);
    }

    @Test
    public void testSorted() {
        System.out.println("sorted");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .reverse()
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(0, 100)));
    }

    @Test
    public void testSorted_Comparator() {
        System.out.println("sorted");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .sorted(Comparator.comparingInt(i -> -i))
                             .elementsEqual(Enumerator.rangeInt(0, 100)
                                                      .reverse()));
    }

    @Test
    public void testToArray() {
        System.out.println("toArray");
        Long[] longs = Enumerator.rangeLong(0, 100).toArray(Long.class);
        for(int i = 0; i<100; ++i) {
            assertEquals(longs[i], Long.valueOf(i));
        }
    }

    @Test
    public void testToList() {
        System.out.println("toList");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .elementsEqual(Enumerator.rangeInt(0, 100)
                                                      .toList()
                                                      .iterator()));
    }

    @Test
    public void testToMap() {
        System.out.println("toMap");
        Map<Long, String> map = Enumerator.rangeLong(0, 100)
                                          .toMap(i -> i, i -> i.toString());
        for(Long x : map.keySet()) {
            assertEquals(map.get(x), x.toString());
        }
    }

    @Test
    public void testToSet() {
        System.out.println("toSet");
        assertEquals(Enumerator.repeat(100, 5).toSet().size(), 1);
    }

    @Test
    public void testUnion_Iterator() {
        System.out.println("union iterator");
        assertTrue(Enumerator.on(3, 4, 5)
                             .union(Enumerator.on(1, 2, 3))
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testUnion_Iterable() {
        System.out.println("union iterable");
        assertTrue(Enumerator.on(3, 4, 5)
                             .union(Enumerator.on(1, 2, 3).asEnumerable())
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testUnion_Enumeration() {
        System.out.println("union enumeration");
        assertTrue(Enumerator.on(3, 4, 5)
                             .union(Enumerator.on(1, 2, 3).asEnumeration())
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testUnion_Stream() {
        System.out.println("union stream");
        assertTrue(Enumerator.on(3, 4, 5)
                             .union(Enumerator.on(1, 2, 3).asStream())
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testUnion_Spliterator() {
        System.out.println("union spliterator");
        assertTrue(Enumerator.on(3, 4, 5)
                             .union(Enumerator.on(1, 2, 3).asSpliterator())
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testUnion_Supplier() {
        System.out.println("union supplier");
        assertTrue(Enumerator.on(3, 4, 5)
                             .union(Enumerator.on(1, 2, 3).asSupplier())
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testUnion_On() {
        System.out.println("union on");
        assertTrue(Enumerator.on(3, 4, 5)
                             .unionOn(1, 2, 3)
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    @Test
    public void testZipBoth() {
        System.out.println("zipBoth");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .zipBoth(Enumerator.rangeInt(100, 200))
                               .count(), 100);
    }

    @Test
    public void testZipLeft() {
        System.out.println("zipLeft");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .zipLeft(Enumerator.rangeInt(50, 200))
                               .count(), 100);
    }

    @Test
    public void testZipRight() {
        System.out.println("zipRight");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .zipRight(Enumerator.rangeInt(50, 200))
                               .count(), 150);
    }

    @Test
    public void testZipAny() {
        System.out.println("zipAny");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .zipAny(Enumerator.rangeInt(50, 200))
                               .count(), 150);
        assertEquals(Enumerator.rangeInt(0, 150)
                               .zipAny(Enumerator.rangeInt(100, 200))
                               .count(), 150);
        assertEquals(Enumerator.rangeInt(0, 100)
                               .zipAny(Enumerator.rangeInt(100, 200))
                               .count(), 100);
    }

    @Test
    public void testZipThree() {
        System.out.println("zipThree");
        assertEquals(Enumerator.rangeInt(0, 50)
                               .zipAll(Enumerator.rangeInt(0, 75),
                                       Enumerator.rangeInt(0, 100))
                               .count(), 100);
        assertEquals(Enumerator.on(1, 2, 3)
                               .zipAll(Enumerator.on(1, 2, 3, 4),
                                       Enumerator.on(1, 2, 3, 4, 5))
                               .count(), 5);
    }
}
