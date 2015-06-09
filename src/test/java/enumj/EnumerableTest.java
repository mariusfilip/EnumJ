/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marius Filip
 */
public class EnumerableTest {

    public EnumerableTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        iterator = Enumerator.of(Arrays.asList(1, 2, 3).iterator());
        onceEnumerable = new OnceEnumerable(iterator);
    }
    Iterator<Integer> iterator;
    OnceEnumerable<Integer> onceEnumerable;

    @After
    public void tearDown() {
    }

    /**
     * Test of iterator method, of class OnceEnumerable.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        assertSame(onceEnumerable.iterator(), iterator);
    }

    /**
     * Test of enumerator method, of class Enumerable.
     */
    @Test
    public void testEnumerator() {
        System.out.println("enumerator");
        EnumerableGenerator.generatorPairs()
                           .limit(100)
                           .map(p -> Pair.of(p.getLeft().repeatable(),
                                             p.getRight().repeatable()))
                           .forEach(p -> {
                               assertTrue(p.getLeft()
                                           .enumerator()
                                           .elementsEqual(p.getRight()
                                                           .iterator()));
                           });
    }

    /**
     * Test of enumerating method, of class Enumerable.
     */
    @Test
    public void testEnumerating() {
        System.out.println("enumerating");
        EnumerableGenerator.generators()
                           .limit(100)
                           .map(gen -> gen.enumerable())
                           .forEach(en -> assertFalse(en.enumerating()));
    }

    /**
     * Test of on method, of class Enumerable.
     */
    @Test
    public void testOn() {
        System.out.println("on");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().onEnumerable(),
                                  p.getRight().onEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Iterable() {
        System.out.println("of");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofIterableEnumerable(),
                                  p.getRight().ofIterableEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Iterator() {
        System.out.println("of");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofIteratorEnumerable(),
                                  p.getRight().ofIteratorEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Enumeration() {
        System.out.println("of");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofEnumerationEnumerable(),
                                  p.getRight().ofEnumerationEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Stream() {
        System.out.println("of");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofStreamEnumerable(),
                                  p.getRight().ofStreamEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Spliterator() {
        System.out.println("of");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofSpliteratorEnumerable(),
                                  p.getRight().ofSpliteratorEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Supplier() {
        System.out.println("of");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofSupplierEnumerable(),
                                  p.getRight().ofSupplierEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of ofLazyIterable method, of class Enumerable.
     */
    @Test
    public void testOfLazyIterable() {
        System.out.println("ofLazyIterable");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazyIterableEnumerable(),
                                  p.getRight().ofLazyIterableEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of ofLazyIterator method, of class Enumerable.
     */
    @Test
    public void testOfLazyIterator() {
        System.out.println("ofLazyIterator");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazyIteratorEnumerable(),
                                  p.getRight().ofLazyIteratorEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of ofLazyEnumeration method, of class Enumerable.
     */
    @Test
    public void testOfLazyEnumeration() {
        System.out.println("ofLazyEnumeration");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazyEnumerationEnumerable(),
                                  p.getRight().ofLazyEnumerationEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of ofLazyStream method, of class Enumerable.
     */
    @Test
    public void testOfLazyStream() {
        System.out.println("ofLazyStream");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazyEnumerationEnumerable(),
                                  p.getRight().ofLazyEnumerationEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of ofLazySpliterator method, of class Enumerable.
     */
    @Test
    public void testOfLazySpliterator() {
        System.out.println("ofLazySpliterator");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().ofLazySpliteratorEnumerable(),
                                  p.getRight().ofLazySpliteratorEnumerable()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of ofLateBinding method, of class Enumerable.
     */
    @Test
    public void testOfLateBinding() {
        System.out.println("ofLateBinding");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> {
                    final Enumerable<Double> left = p.getLeft().repeatable();
                    final LateBindingEnumerable<Double> right =
                            p.getRight()
                             .ofLateBindingEnumerable();
                    right.bind(p.getRight().repeatable());
                    return Pair.of(left, right);
                })
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of as method, of class Enumerable.
     */
    @Test
    public void testAs() {
        System.out.println("as");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  p.getRight()
                                   .enumerable()
                                   .as(Integer.class)
                                   .as(Double.class)))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of asFiltered method, of class Enumerable.
     */
    @Test
    public void testAsFiltered() {
        System.out.println("asFiltered");
        EnumerableGenerator
                .generators()
                .limit(100)
                .map(gen -> gen.enumerable())
                .map(en -> en.asFiltered(Integer.class))
                .forEach(en -> assertEquals(0, en.enumerator().count()));
    }

    /**
     * Test of asEnumeration method, of class Enumerable.
     */
    @Test
    public void testAsEnumeration() {
        System.out.println("asEnumeration");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  Enumerable.of(p.getRight()
                                                 .enumerable()
                                                 .asEnumeration())))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of asSpliterator method, of class Enumerable.
     */
    @Test
    public void testAsSpliterator() {
        System.out.println("asSpliterator");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  Enumerable.of(p.getRight()
                                                 .enumerable()
                                                 .asSpliterator())))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of asStream method, of class Enumerable.
     */
    @Test
    public void testAsStream() {
        System.out.println("asStream");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  Enumerable.of(p.getRight()
                                                 .enumerable()
                                                 .asStream())))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of asTolerant method, of class Enumerable.
     */
    @Test
    public void testAsTolerant_Consumer() {
        System.out.println("asTolerant");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  Enumerable.of(p.getRight()
                                                 .enumerable()
                                                 .asTolerant(ex -> {}))))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of asTolerant method, of class Enumerable.
     */
    @Test
    public void testAsTolerant_Consumer_int() {
        System.out.println("asTolerant");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  Enumerable.of(p.getRight()
                                                 .enumerable()
                                                 .asTolerant(ex -> {}, 100))))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of append method, of class Enumerable.
     */
    @Test
    public void testAppend() {
        System.out.println("append");
        assertTrue(Enumerable.on(1, 2, 3)
                             .append(4, 5, 6)
                             .elementsEqual(Enumerable.on(1, 2, 3, 4, 5, 6)));
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  p.getRight().enumerable()))
                .forEach(p -> {
                    assertTrue(Enumerable.empty()
                                         .append(p.getLeft()
                                                  .enumerator()
                                                  .toArray(Double.class))
                                         .elementsEqual(p.getRight()));
                });
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  p.getRight().enumerable()))
                .forEach(p -> {
                    assertTrue(p.getLeft()
                                .append(Enumerator.empty()
                                                  .as(Double.class)
                                                  .toArray(Double.class))
                                .elementsEqual(p.getRight()));
                });
    }

    /**
     * Test of choiceOf method, of class Enumerable.
     */
    @Test
    public void testChoiceOf_4args() {
        System.out.println("choiceOf");
        final Supplier<IntSupplier> indexSupplier = () -> {
            final Random rnd = new Random(1001);
            return () -> rnd.nextInt(4);
        };
        assertTrue(Enumerable
                .choiceOf(indexSupplier,
                          Enumerable.on(1, 2, 3),
                          Enumerable.on(4, 5, 6),
                          Enumerable.on(7, 8, 9),
                          Enumerable.on(10, 11, 12))
                .sorted()
                .elementsEqual(Enumerable.on(1, 2, 3, 4, 5, 6,
                                             7, 8, 9, 10, 11, 12)));
    }

    /**
     * Test of choiceOf method, of class Enumerable.
     */
    @Test
    public void testChoiceOf_5args() {
        System.out.println("choiceOf");
        final Supplier<IntSupplier> indexSupplier = () -> {
            final Random rnd = new Random(1001);
            return () -> rnd.nextInt(4);
        };
        final Supplier<IntUnaryOperator> nextSupplier = () -> {
            return i -> (i+1)%4;
        };
        assertTrue(Enumerable
                .choiceOf(indexSupplier,
                          nextSupplier,
                          Enumerable.on(1, 2, 3),
                          Enumerable.on(4, 5, 6),
                          Enumerable.on(7, 8, 9),
                          Enumerable.on(10, 11, 12))
                .sorted()
                .elementsEqual(Enumerable.on(1, 2, 3, 4, 5, 6,
                                             7, 8, 9, 10, 11, 12)));
    }

    /**
     * Test of concat method, of class Enumerable.
     */
    @Test
    public void testConcat() {
        System.out.println("concat");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().repeatable(),
                                  p.getRight().repeatable()))
                .map(p -> Pair.of(p.getLeft(),
                                  p.getLeft().concat(p.getRight())))
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()))
                .map(p -> Pair.of(p.getLeft()
                                   .reduce(0.0, (x,y) -> x+y),
                                  p.getRight()
                                   .reduce(0.0, (x,y) -> x+y)))
                .forEach(p -> assertEquals("",
                                           p.getLeft()*2,
                                           p.getRight(),
                                           0.000001));
    }

    /**
     * Test of concatOn method, of class Enumerable.
     */
    @Test
    public void testConcatOn() {
        System.out.println("concatOn");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().repeatable(),
                                  p.getRight().repeatable()))
                .map(p -> Pair.of(p.getLeft(),
                                  p.getLeft().concatOn(p.getRight()
                                                        .enumerator()
                                                        .toArray(Double.class))))
                .map(p -> Pair.of(p.getLeft().enumerator(),
                                  p.getRight().enumerator()))
                .map(p -> Pair.of(p.getLeft()
                                   .reduce(0.0, (x,y) -> x+y),
                                  p.getRight()
                                   .reduce(0.0, (x,y) -> x+y)))
                .forEach(p -> assertEquals("",
                                           p.getLeft()*2,
                                           p.getRight(),
                                           0.000001));
    }

    /**
     * Test of distinct method, of class Enumerable.
     */
    @Test
    public void testDistinct() {
        System.out.println("distinct");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().repeatable(),
                                  p.getRight().repeatable()))
                .map(p -> Pair.of(p.getLeft(),
                                  p.getLeft().concatOn(p.getRight()
                                                        .enumerator()
                                                        .toArray(Double.class))))
                .map(p -> Pair.of(p.getLeft().distinct(),
                                  p.getRight().distinct()))
                .forEach(p -> assertTrue(p.getLeft()
                                          .elementsEqual(p.getRight())));
    }

    /**
     * Test of elementsEqual method, of class Enumerable.
     */
    @Test
    public void testElementsEqual() {
        System.out.println("elementsEqual");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().repeatable(),
                                  p.getRight().repeatable()))
                .map(p -> Pair.of(p.getLeft(),
                                  p.getRight()
                                   .reverse()
                                   .reverse()))
                .forEach(p -> p.getLeft().elementsEqual(p.getRight()));
    }

    /**
     * Test of empty method, of class Enumerable.
     */
    @Test
    public void testEmpty() {
        System.out.println("empty");
        assertEquals(0, Enumerable.empty().enumerator().count());
    }

    /**
     * Test of filter method, of class Enumerable.
     */
    @Test
    public void testFilter() {
        System.out.println("filter");
        EnumerableGenerator
                .generators()
                .limit(100)
                .map(gen -> gen.enumerable())
                .map(en -> en.filter(x -> x > 0))
                .forEach(en -> en.enumerator()
                                 .allMatch(x -> x > 0));
    }

    /**
     * Test of flatMap method, of class Enumerable.
     */
    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
        EnumerableGenerator
                .generators()
                .limit(100)
                .map(gen -> gen.enumerable())
                .map(en -> en.flatMap(x -> Enumerable.on(x, -x)))
                .map(en -> en.enumerator()
                             .reduce((x,y) -> x+y))
                .forEach(x -> assertEquals("", 0, x.orElse(0.0), 0.000001));
    }

    /**
     * Test of indexedMap method, of class Enumerable.
     */
    @Test
    public void testIndexedMap() {
        System.out.println("indexedMap");
        EnumerableGenerator
                .generators()
                .limit(100)
                .map(gen -> gen.repeatable())
                .map(en -> en.indexedMap((x,i) -> Pair.of(1+i.intValue(), x)))
                .map(en -> Pair.of(en.map(q -> q.getLeft())
                                     .enumerator()
                                     .reduce((x,y) -> x+y),
                                   en.enumerator().count()))
                .map(p -> Pair.of(p.getLeft().orElse(0),
                                  p.getRight()*(p.getRight()+1)/2))
                .forEach(p -> assertEquals("",
                                           p.getLeft(),
                                           p.getRight().intValue(),
                                           0.000001));
    }

    /**
     * Test of iterate method, of class Enumerable.
     */
    @Test
    public void testIterate() {
        System.out.println("iterate");
        assertTrue(Enumerable.iterate(0, i -> i+2)
                             .takeWhile(i -> i<100)
                             .elementsEqual(Enumerable.rangeInt(0, 100)
                                                      .filter(i -> 0 == i%2)));
    }

    /**
     * Test of limit method, of class Enumerable.
     */
    @Test
    public void testLimit() {
        System.out.println("limit");
        EnumerableGenerator
                .generators()
                .limit(100)
                .map(gen -> gen.repeatable())
                .map(en -> Pair.of(en,
                                   en.limit(10)
                                     .concat(en.skip(10))))
                .forEach(p -> p.getLeft()
                               .elementsEqual(p.getRight()));
    }

    /**
     * Test of limitWhile method, of class Enumerable.
     */
    @Test
    public void testLimitWhile() {
        System.out.println("limitWhile");
        EnumerableGenerator
                .generators()
                .limit(100)
                .map(gen -> gen.repeatable())
                .map(en -> Pair.of(en,
                                   en.limitWhile(x -> x<100)
                                     .concat(en.skipWhile(x -> x<100))))
                .forEach(p -> p.getLeft()
                               .elementsEqual(p.getRight()));
    }

    /**
     * Test of map method, of class Enumerable.
     */
    @Test
    public void testMap() {
        System.out.println("map");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().enumerable(),
                                  p.getRight().enumerable()))
                .forEach(p -> p.getLeft()
                               .elementsEqual(p.getRight()
                                               .map(x -> -x)
                                               .map(x -> -x)));
    }

    /**
     * Test of prepend method, of class Enumerable.
     */
    @Test
    public void testPrepend() {
        System.out.println("prepend");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().repeatable(),
                                  p.getRight().repeatable()))
                .map(p -> Pair.of(p.getLeft(),
                                  p.getRight()
                                   .skip(10)
                                   .prepend(p.getRight().take(10))))
                .forEach(p -> p.getLeft()
                               .elementsEqual(p.getRight()));
    }

    /**
     * Test of prependOn method, of class Enumerable.
     */
    @Test
    public void testPrependOn() {
        System.out.println("prependOn");
        EnumerableGenerator
                .generatorPairs()
                .limit(100)
                .map(p -> Pair.of(p.getLeft().repeatable(),
                                  p.getRight().repeatable()))
                .map(p -> Pair.of(p.getLeft(),
                                  p.getRight()
                                   .skip(10)
                                   .prependOn(p.getRight()
                                               .take(10)
                                               .enumerator()
                                               .toArray(Double.class))))
                .forEach(p -> p.getLeft()
                               .elementsEqual(p.getRight()));
    }

    /**
     * Test of range method, of class Enumerable.
     */
    @Test
    public void testRange() {
        System.out.println("range");
        assertTrue(Enumerable
                .rangeInt(0, 100)
                .elementsEqual(Enumerable
                        .range(0,
                               100,
                               x -> x+2,
                               Comparator.comparingInt(x->x))
                        .concat(
                        Enumerable.range(1,
                                         100,
                                         x -> x+2,
                                         Comparator.comparingInt(x->x)))
                        .sorted()));
    }

    /**
     * Test of rangeClosed method, of class Enumerable.
     */
    @Test
    public void testRangeClosed() {
        System.out.println("rangeClosed");
        assertTrue(Enumerable
                .rangeIntClosed(0, 100)
                .elementsEqual(Enumerable
                        .rangeClosed(0,
                                     100,
                                     x -> x+2,
                                     Comparator.comparingInt(x->x))
                        .concat(
                        Enumerable.rangeClosed(1,
                                               100,
                                               x -> x+2,
                                               Comparator.comparingInt(x->x)))
                        .sorted()));
    }

    /**
     * Test of rangeInt method, of class Enumerable.
     */
    @Test
    public void testRangeInt() {
        System.out.println("rangeInt");
        assertTrue(Enumerable.on(0, 1, 2, 3, 4, 5)
                             .elementsEqual(Enumerable.rangeInt(0, 6)));
    }

    /**
     * Test of rangeIntClosed method, of class Enumerable.
     */
    @Test
    public void testRangeIntClosed() {
        System.out.println("rangeIntClosed");
        assertTrue(Enumerable.on(0, 1, 2, 3, 4, 5)
                             .elementsEqual(Enumerable.rangeIntClosed(0, 5)));
    }

    /**
     * Test of rangeLong method, of class Enumerable.
     */
    @Test
    public void testRangeLong() {
        System.out.println("rangeLong");
        assertTrue(Enumerable.on(0L, 1L, 2L, 3L, 4L, 5L)
                             .elementsEqual(Enumerable.rangeLong(0, 6)));
    }

    /**
     * Test of rangeLongClosed method, of class Enumerable.
     */
    @Test
    public void testRangeLongClosed() {
        System.out.println("rangeLongClosed");
        assertTrue(Enumerable.on(0L, 1L, 2L, 3L, 4L, 5L)
                             .elementsEqual(Enumerable.rangeLongClosed(0, 5)));
    }

    /**
     * Test of repeat method, of class Enumerable.
     */
    @Test
    public void testRepeat() {
        System.out.println("repeat");
        assertTrue(Enumerable.on(1, 1, 1, 1, 1)
                             .elementsEqual(Enumerable.repeat(1, 5)));
    }

    /**
     * Test of repeatAll method, of class Enumerable.
     */
    @Test
    public void testRepeatAll() {
        System.out.println("repeatAll");
        assertTrue(Enumerable.on(1, 2, 1, 2, 1, 2)
                             .elementsEqual(Enumerable.on(1, 2)
                                                      .repeatAll(3)));
    }

    /**
     * Test of repeatEach method, of class Enumerable.
     */
    @Test
    public void testRepeatEach() {
        System.out.println("repeatEach");
        assertTrue(Enumerable.on(1, 1, 1, 2, 2, 2)
                             .elementsEqual(Enumerable.on(1, 2)
                                                      .repeatEach(3)));
    }

    /**
     * Test of reverse method, of class Enumerable.
     */
    @Test
    public void testReverse() {
        System.out.println("reverse");
        assertTrue(Enumerable.on(5, 4, 3, 2, 1)
                             .elementsEqual(Enumerable.on(1, 2, 3, 4, 5)
                                                      .reverse()));
    }

    /**
     * Test of skip method, of class Enumerable.
     */
    @Test
    public void testSkip() {
        System.out.println("skip");
        assertTrue(Enumerable.rangeInt(50, 100)
                             .elementsEqual(Enumerable.rangeInt(0, 100)
                                                      .skip(50)));
    }

    /**
     * Test of skipWhile method, of class Enumerable.
     */
    @Test
    public void testSkipWhile() {
        System.out.println("skipWhile");
        assertTrue(Enumerable.rangeInt(50, 100)
                             .elementsEqual(Enumerable.rangeInt(0, 100)
                                                      .skipWhile(x -> x<50)));
    }

    /**
     * Test of sorted method, of class Enumerable.
     */
    @Test
    public void testSorted_0args() {
        System.out.println("sorted");
        assertTrue(Enumerable.rangeInt(-99, 1)
                             .elementsEqual(Enumerable.rangeInt(0, 100)
                                                      .map(x -> -x)
                                                      .sorted()));
    }

    /**
     * Test of sorted method, of class Enumerable.
     */
    @Test
    public void testSorted_Comparator() {
        System.out.println("sorted");
        assertTrue(Enumerable.rangeInt(-99, 1)
                             .map(x -> -x)
                             .elementsEqual(Enumerable
                                     .rangeInt(0, 100)
                                     .sorted(Comparator.comparingInt(x->-x))));
    }

    /**
     * Test of take method, of class Enumerable.
     */
    @Test
    public void testTake() {
        System.out.println("take");
        assertTrue(Enumerable.rangeInt(0, 50)
                             .elementsEqual(Enumerable.rangeInt(0, 100)
                                                      .take(50)));
    }

    /**
     * Test of takeWhile method, of class Enumerable.
     */
    @Test
    public void testTakeWhile() {
        System.out.println("takeWhile");
        assertTrue(Enumerable.rangeInt(0, 50)
                             .elementsEqual(Enumerable.rangeInt(0, 100)
                                                      .takeWhile(x -> x<50)));
    }

    /**
     * Test of union method, of class Enumerable.
     */
    @Test
    public void testUnion() {
        System.out.println("union");
        assertTrue(Enumerable
                .rangeInt(0, 100)
                .elementsEqual(
                        Enumerable.rangeInt(0, 100+1)
                                  .map(i -> Enumerable.rangeInt(0, i))
                                  .enumerator()
                                  .reduce((en1,en2) -> en1.union(en2))
                                  .get()));
    }

    /**
     * Test of unionOn method, of class Enumerable.
     */
    @Test
    public void testUnionOn() {
        System.out.println("unionOn");
        assertTrue(Enumerable.on(1, 2, 3, 4, 5)
                             .elementsEqual(Enumerable
                                     .on(1, 2, 3)
                                     .unionOn(3, 4, 5)));
    }

    /**
     * Test of zipAny method, of class Enumerable.
     */
    @Test
    public void testZipAny() {
        System.out.println("zipAny");
        assertEquals(5,
                     Enumerable.on(1, 2, 3)
                               .zipAny(Enumerable.on(1, 2, 3, 4, 5))
                               .enumerator()
                               .count());
    }

    /**
     * Test of zipBoth method, of class Enumerable.
     */
    @Test
    public void testZipBoth() {
        System.out.println("zipBoth");
        assertEquals(3,
                     Enumerable.on(1, 2, 3)
                               .zipBoth(Enumerable.on(1, 2, 3, 4, 5))
                               .enumerator()
                               .count());
    }

    /**
     * Test of zipLeft method, of class Enumerable.
     */
    @Test
    public void testZipLeft() {
        System.out.println("zipLeft");
        assertEquals(3,
                     Enumerable.on(1, 2, 3)
                               .zipLeft(Enumerable.on(1, 2, 3, 4, 5))
                               .enumerator()
                               .count());
    }

    /**
     * Test of zipRight method, of class Enumerable.
     */
    @Test
    public void testZipRight() {
        System.out.println("zipRight");
        assertEquals(5,
                     Enumerable.on(1, 2, 3)
                               .zipRight(Enumerable.on(1, 2, 3, 4, 5))
                               .enumerator()
                               .count());
    }

    /**
     * Test of zipAll method, of class Enumerable.
     */
    @Test
    public void testZipAll() {
        System.out.println("zipAll");
        EnumerableGenerator
                .generators()
                .limit(100)
                .map(gen -> Enumerator.on((Enumerable)gen.repeatable(),
                                          gen.repeatable(),
                                          gen.repeatable(),
                                          gen.repeatable())
                                      .toArray(Enumerable.class))
                .map(arr -> {
                    final Enumerable<Double> first = arr[0];
                    final Enumerable<Double> second = arr[1];
                    final Enumerable<Double> third = arr[2];
                    final Enumerable<Double> fourth = arr[3];
                    final Enumerable[] result = new Enumerable[5];

                    result[0] = first;
                    result[1] = second;
                    result[2] = third;
                    result[3] = fourth;
                    result[4] = first.zipAll(second, third, fourth);

                    return result;
                })
                .forEach(arr -> assertEquals(
                        "",
                        arr[4].enumerator().count(),
                        Enumerator.on(arr[0].enumerator().count(),
                                      arr[1].enumerator().count(),
                                      arr[2].enumerator().count())
                                  .max(Comparator.comparingLong(x->x))
                                  .get(),
                        0.000001));
    }
}
