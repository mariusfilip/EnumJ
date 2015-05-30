/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
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
        EnumerableGenerator.generators()
                           .limit(100)
                           .map(gen -> gen.onEnumerable())
                           .enumerator()
                           .forEach(en ->
                               assertTrue(en.enumerator()
                                            .elementsEqual(en.iterator())));
    }

    /**
     * Test of enumerating method, of class Enumerable.
     */
    @Test
    public void testEnumerating() {
        System.out.println("enumerating");
    }

    /**
     * Test of on method, of class Enumerable.
     */
    @Test
    public void testOn() {
        System.out.println("on");
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Iterable() {
        System.out.println("of");
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Iterator() {
        System.out.println("of");
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Enumeration() {
        System.out.println("of");
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Stream() {
        System.out.println("of");
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Spliterator() {
        System.out.println("of");
    }

    /**
     * Test of of method, of class Enumerable.
     */
    @Test
    public void testOf_Supplier() {
        System.out.println("of");
    }

    /**
     * Test of ofLazyIterable method, of class Enumerable.
     */
    @Test
    public void testOfLazyIterable() {
        System.out.println("ofLazyIterable");
    }

    /**
     * Test of ofLazyIterator method, of class Enumerable.
     */
    @Test
    public void testOfLazyIterator() {
        System.out.println("ofLazyIterator");
    }

    /**
     * Test of ofLazyEnumeration method, of class Enumerable.
     */
    @Test
    public void testOfLazyEnumeration() {
        System.out.println("ofLazyEnumeration");
    }

    /**
     * Test of ofLazyStream method, of class Enumerable.
     */
    @Test
    public void testOfLazyStream() {
        System.out.println("ofLazyStream");
    }

    /**
     * Test of ofLazySpliterator method, of class Enumerable.
     */
    @Test
    public void testOfLazySpliterator() {
        System.out.println("ofLazySpliterator");
    }

    /**
     * Test of ofLateBinding method, of class Enumerable.
     */
    @Test
    public void testOfLateBinding() {
        System.out.println("ofLateBinding");
    }

    /**
     * Test of as method, of class Enumerable.
     */
    @Test
    public void testAs() {
        System.out.println("as");
    }

    /**
     * Test of asFiltered method, of class Enumerable.
     */
    @Test
    public void testAsFiltered() {
        System.out.println("asFiltered");
    }

    /**
     * Test of asEnumeration method, of class Enumerable.
     */
    @Test
    public void testAsEnumeration() {
        System.out.println("asEnumeration");
    }

    /**
     * Test of asSpliterator method, of class Enumerable.
     */
    @Test
    public void testAsSpliterator() {
        System.out.println("asSpliterator");
    }

    /**
     * Test of asStream method, of class Enumerable.
     */
    @Test
    public void testAsStream() {
        System.out.println("asStream");
    }

    /**
     * Test of asTolerant method, of class Enumerable.
     */
    @Test
    public void testAsTolerant_Consumer() {
        System.out.println("asTolerant");
    }

    /**
     * Test of asTolerant method, of class Enumerable.
     */
    @Test
    public void testAsTolerant_Consumer_int() {
        System.out.println("asTolerant");
    }

    /**
     * Test of append method, of class Enumerable.
     */
    @Test
    public void testAppend() {
        System.out.println("append");
    }

    /**
     * Test of choiceOf method, of class Enumerable.
     */
    @Test
    public void testChoiceOf_4args() {
        System.out.println("choiceOf");
    }

    /**
     * Test of choiceOf method, of class Enumerable.
     */
    @Test
    public void testChoiceOf_5args() {
        System.out.println("choiceOf");
    }

    /**
     * Test of concat method, of class Enumerable.
     */
    @Test
    public void testConcat() {
        System.out.println("concat");
    }

    /**
     * Test of concatOn method, of class Enumerable.
     */
    @Test
    public void testConcatOn() {
        System.out.println("concatOn");
    }

    /**
     * Test of distinct method, of class Enumerable.
     */
    @Test
    public void testDistinct() {
        System.out.println("distinct");
    }

    /**
     * Test of elementsEqual method, of class Enumerable.
     */
    @Test
    public void testElementsEqual() {
        System.out.println("elementsEqual");
    }

    /**
     * Test of empty method, of class Enumerable.
     */
    @Test
    public void testEmpty() {
        System.out.println("empty");
    }

    /**
     * Test of filter method, of class Enumerable.
     */
    @Test
    public void testFilter() {
        System.out.println("filter");
    }

    /**
     * Test of flatMap method, of class Enumerable.
     */
    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
    }

    /**
     * Test of indexedMap method, of class Enumerable.
     */
    @Test
    public void testIndexedMap() {
        System.out.println("indexedMap");
    }

    /**
     * Test of iterate method, of class Enumerable.
     */
    @Test
    public void testIterate() {
        System.out.println("iterate");
    }

    /**
     * Test of limit method, of class Enumerable.
     */
    @Test
    public void testLimit() {
        System.out.println("limit");
    }

    /**
     * Test of limitWhile method, of class Enumerable.
     */
    @Test
    public void testLimitWhile() {
        System.out.println("limitWhile");
    }

    /**
     * Test of map method, of class Enumerable.
     */
    @Test
    public void testMap() {
        System.out.println("map");
    }

    /**
     * Test of prepend method, of class Enumerable.
     */
    @Test
    public void testPrepend() {
        System.out.println("prepend");
    }

    /**
     * Test of prependOn method, of class Enumerable.
     */
    @Test
    public void testPrependOn() {
        System.out.println("prependOn");
    }

    /**
     * Test of range method, of class Enumerable.
     */
    @Test
    public void testRange() {
        System.out.println("range");
    }

    /**
     * Test of rangeClosed method, of class Enumerable.
     */
    @Test
    public void testRangeClosed() {
        System.out.println("rangeClosed");
    }

    /**
     * Test of rangeInt method, of class Enumerable.
     */
    @Test
    public void testRangeInt() {
        System.out.println("rangeInt");
    }

    /**
     * Test of rangeIntClosed method, of class Enumerable.
     */
    @Test
    public void testRangeIntClosed() {
        System.out.println("rangeIntClosed");
    }

    /**
     * Test of rangeLong method, of class Enumerable.
     */
    @Test
    public void testRangeLong() {
        System.out.println("rangeLong");
    }

    /**
     * Test of rangeLongClosed method, of class Enumerable.
     */
    @Test
    public void testRangeLongClosed() {
        System.out.println("rangeLongClosed");
    }

    /**
     * Test of repeat method, of class Enumerable.
     */
    @Test
    public void testRepeat() {
        System.out.println("repeat");
    }

    /**
     * Test of repeatAll method, of class Enumerable.
     */
    @Test
    public void testRepeatAll() {
        System.out.println("repeatAll");
    }

    /**
     * Test of repeatEach method, of class Enumerable.
     */
    @Test
    public void testRepeatEach() {
        System.out.println("repeatEach");
    }

    /**
     * Test of reverse method, of class Enumerable.
     */
    @Test
    public void testReverse() {
        System.out.println("reverse");
    }

    /**
     * Test of skip method, of class Enumerable.
     */
    @Test
    public void testSkip() {
        System.out.println("skip");
    }

    /**
     * Test of skipWhile method, of class Enumerable.
     */
    @Test
    public void testSkipWhile() {
        System.out.println("skipWhile");
    }

    /**
     * Test of sorted method, of class Enumerable.
     */
    @Test
    public void testSorted_0args() {
        System.out.println("sorted");
    }

    /**
     * Test of sorted method, of class Enumerable.
     */
    @Test
    public void testSorted_Comparator() {
        System.out.println("sorted");
    }

    /**
     * Test of take method, of class Enumerable.
     */
    @Test
    public void testTake() {
        System.out.println("take");
    }

    /**
     * Test of takeWhile method, of class Enumerable.
     */
    @Test
    public void testTakeWhile() {
        System.out.println("takeWhile");
    }

    /**
     * Test of union method, of class Enumerable.
     */
    @Test
    public void testUnion() {
        System.out.println("union");
    }

    /**
     * Test of unionOn method, of class Enumerable.
     */
    @Test
    public void testUnionOn() {
        System.out.println("unionOn");
    }

    /**
     * Test of zipAny method, of class Enumerable.
     */
    @Test
    public void testZipAny() {
        System.out.println("zipAny");
    }

    /**
     * Test of zipBoth method, of class Enumerable.
     */
    @Test
    public void testZipBoth() {
        System.out.println("zipBoth");
    }

    /**
     * Test of zipLeft method, of class Enumerable.
     */
    @Test
    public void testZipLeft() {
        System.out.println("zipLeft");
    }

    /**
     * Test of zipRight method, of class Enumerable.
     */
    @Test
    public void testZipRight() {
        System.out.println("zipRight");
    }

    /**
     * Test of zipAll method, of class Enumerable.
     */
    @Test
    public void testZipAll() {
        System.out.println("zipAll");
    }
}
