/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.mutable.MutableInt;
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

    /**
     * Test of on method, of class Enumerator.
     */
    @Test
    public void testOn() {
        System.out.println("on");
        assertNotNull(Enumerator.on(1, 2, 3));
    }

    /**
     * Test of of method, of class Enumerator.
     */
    @Test
    public void testOf_Iterator() {
        System.out.println("of iterator");
        assertNotNull(Enumerator.of(Arrays.asList(1, 2, 3).iterator()));
    }

    /**
     * Test of of method, of class Enumerator.
     */
    @Test
    public void testOf_Iterable() {
        System.out.println("of iterable");
        assertNotNull(Enumerator.of(Arrays.asList(1, 2, 3)));
    }

    /**
     * Test of of method, of class Enumerator.
     */
    @Test
    public void testOf_Stream() {
        System.out.println("of stream");
        assertNotNull(Enumerator.of(Stream.of("a", "b", "c")));
    }

    /**
     * Test of of method, of class Enumerator.
     */
    @Test
    public void testOf_Spliterator() {
        System.out.println("of spliterator");
        assertNotNull(Enumerator.of(Stream.of("a", "b", "c").spliterator()));
    }

    /**
     * Test of of method, of class Enumerator.
     */
    @Test
    public void testOf_Supplier() {
        System.out.println("of supplier");
        assertNotNull(Enumerator.of(() -> Optional.empty()));
    }

    /**
     * Test of choiceOf method, of class Enumerator.
     */
    @Test
    public void testOf_choice() {
        System.out.println("of choice");
        assertTrue(Enumerator.choiceOf(() -> 0,
                                   Enumerator.on(1),
                                   Enumerator.on(2, 3),
                                   Enumerator.on(4, 5, 6))
                             .elementsEqual(Enumerator.rangeIntClosed(1, 6)));
    }

    /**
     * Test of of method, of class Enumerator.
     */
    @Test
    public void testOf_repeating_elem() {
        System.out.println("of repeating elem");
        assertEquals(Enumerator.repeat("a", 3).count(), 3);
    }

    /**
     * Test of as method, of class Enumerator.
     */
    @Test
    public void testAs() {
        System.out.println("as");
        Enumerator.on(1, 2, 3)
                  .as(Integer.class)
                  .forEach(i -> assertTrue(i >= 1));
    }

    /**
     * Test of as method, of class Enumerator.
     */
    @Test
    public void testAsFiltered() {
        System.out.println("as filtered");
        Enumerator.on(1, 2, 3)
                  .asFiltered(Long.class)
                  .forEach(i -> assertTrue(i >= 1));
    }

    /**
     * Test of asIterable method, of class Enumerator.
     */
    @Test
    public void testAsIterable() {
        System.out.println("asIterable");
        assertNotNull(Enumerator.on(1, 2, 3).asIterable());
    }

    /**
     * Test of asStream method, of class Enumerator.
     */
    @Test
    public void testAsStream() {
        System.out.println("asStream");
        assertNotNull(Enumerator.on(1, 2, 3).asStream());
    }

    /**
     * Test of asSpliterator method, of class Enumerator.
     */
    @Test
    public void testAsSpliterator() {
        System.out.println("asSpliterator");
        assertNotNull(Enumerator.on(1, 2, 3).asSpliterator());
    }

    /**
     * Test of asSupplier method, of class Enumerator.
     */
    @Test
    public void testAsSupplier() {
        System.out.println("asSupplier");
        assertNotNull(Enumerator.on(1, 2, 3).asSupplier());
    }

    /**
     * Test of allMatch method, of class Enumerator.
     */
    @Test
    public void testAllMatch() {
        System.out.println("allMatch");
        assertTrue(Enumerator.on(1, 1, 1).allMatch(i -> 1 == i));
        assertTrue(!Enumerator.on(1, 2, 3, 4).allMatch(i -> i > 1));
    }

    /**
     * Test of anyMatch method, of class Enumerator.
     */
    @Test
    public void testAnyMatch() {
        System.out.println("anyMatch");
        assertTrue(Enumerator.on(1, 2, 3).anyMatch(i -> 2 == i));
        assertTrue(!Enumerator.on(1, 2, 3).anyMatch(i -> 4 == i));
    }

    /**
     * Test of append method, of class Enumerator.
     */
    @Test
    public void testAppend() {
        System.out.println("append");
        assertEquals(Enumerator.on(1, 2, 3).append(4, 5).count(), 5);
    }

    /**
     * Test of collect method, of class Enumerator.
     */
    @Test
    public void testCollect() {
        System.out.println("collect");
        assertEquals(Enumerator.on(1, 2, 3)
                               .collect(Collectors.toList())
                               .size(), 3);
    }

    /**
     * Test of concat method, of class Enumerator.
     */
    @Test
    public void testConcat_Iterator() {
        System.out.println("concat");
        assertTrue(Enumerator.on(1, 2, 3)
                             .concat(Enumerator.on(4, 5))
                             .elementsEqual(Enumerator.rangeInt(1, 6)));
    }

    /**
     * Test of concat method, of class Enumerator.
     */
    @Test
    public void testConcat_Iterable() {
        System.out.println("concat");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asIterable())
                               .count(), 5);
    }

    /**
     * Test of concat method, of class Enumerator.
     */
    @Test
    public void testConcat_Stream() {
        System.out.println("concat");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asStream())
                               .count(), 5);
    }

    /**
     * Test of concat method, of class Enumerator.
     */
    @Test
    public void testConcat_Spliterator() {
        System.out.println("concat");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asSpliterator())
                               .count(), 5);
    }

    /**
     * Test of concat method, of class Enumerator.
     */
    @Test
    public void testConcat_Supplier() {
        System.out.println("concat");
        assertEquals(Enumerator.on(1, 2, 3)
                               .concat(Enumerator.on(4, 5).asSupplier())
                               .count(), 5);
    }

    /**
     * Test of contains method, of class Enumerator.
     */
    @Test
    public void testContains() {
        System.out.println("contains");
        Supplier<Enumerator<Integer>> supplier = () -> Enumerator.rangeInt(0, 10);
        for(Integer i : supplier.get().asIterable()) {
            assertTrue(supplier.get().contains(i));
            assertTrue(!supplier.get().contains(-i-1));
        }
    }

    /**
     * Test of count method, of class Enumerator.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        assertEquals(Enumerator.on(1, 2, 3).count(), 3);
    }

    /**
     * Test of distinct method, of class Enumerator.
     */
    @Test
    public void testDistinct() {
        System.out.println("distinct");
        assertTrue(Enumerator.on(1, 2, 3)
                             .elementsEqual(Enumerator.on(1, 2, 3)
                                                      .distinct()));
    }

    /**
     * Test of elementAt method, of class Enumerator.
     */
    @Test
    public void testElementAt() {
        System.out.println("elementAt");
        Supplier<Enumerator<Integer>> supply = () -> Enumerator.rangeInt(0, 10);
        for(int i=0; i<supply.get().count(); ++i) {
            assertEquals(Integer.valueOf(i), supply.get().elementAt(i).get());
        }
    }

    /**
     * Test of elementsEqual method, of class Enumerator.
     */
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

    /**
     * Test of empty method, of class Enumerator.
     */
    @Test
    public void testEmpty() {
        System.out.println("empty");
        assertEquals(Enumerator.empty().count(), 0);
    }

    /**
     * Test of filter method, of class Enumerator.
     */
    @Test
    public void testFilter() {
        System.out.println("filter");
        assertTrue(Enumerator.rangeInt(0, 10)
                             .filter(i -> 0 != i%2)
                             .elementsEqual(Enumerator.iterate(1, i -> i+2)
                                                      .limitWhile(i -> i < 10)));
    }

    /**
     * Test of flatMap method, of class Enumerator.
     */
    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
        assertTrue(Enumerator.rangeInt(0, 10)
                             .flatMap(i -> Enumerator.rangeInt(i*10, (i+1)*10))
                             .elementsEqual(Enumerator.rangeInt(0, 100)));
    }

    /**
     * Test of forEach method, of class Enumerator.
     */
    @Test
    public void testForEach() {
        System.out.println("forEach");
        final MutableInt m = new MutableInt(0);
        Enumerator.rangeInt(0, 100)
                  .forEach(i -> m.setValue(m.intValue()+1));
        assertEquals(m.intValue(), Enumerator.rangeInt(0, 100).count());
    }

    /**
     * Test of iterate method, of class Enumerator.
     */
    @Test
    public void testIterate() {
        System.out.println("iterate");
        assertTrue(Enumerator.iterate(0, i -> i+2)
                             .limitWhile(i -> i<100)
                             .elementsEqual(Enumerator.rangeInt(0, 100)
                                                      .filter(i -> 0 == i%2)));
    }

    /**
     * Test of last method, of class Enumerator.
     */
    @Test
    public void testLast() {
        System.out.println("last");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .last()
                               .get()
                               .intValue(), 99);
    }

    /**
     * Test of limit method, of class Enumerator.
     */
    @Test
    public void testLimit() {
        System.out.println("limit");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .limit(10)
                               .count(), 10);
    }

    /**
     * Test of limitWhile method, of class Enumerator.
     */
    @Test
    public void testLimitWhile() {
        System.out.println("limitWhile");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .limitWhile(i -> i < 10)
                             .elementsEqual(Enumerator.rangeInt(0, 10)));
    }

    /**
     * Test of map method, of class Enumerator.
     */
    @Test
    public void testMap() {
        System.out.println("map");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .map(i -> 2*i)
                             .elementsEqual(Enumerator.iterate(0, i -> i+2)
                                                      .limitWhile(i -> i<200)));
    }

    /**
     * Test of max method, of class Enumerator.
     */
    @Test
    public void testMax() {
        System.out.println("max");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .max(Comparator.comparingInt(i -> i))
                               .get()
                               .intValue(), 99);
    }

    /**
     * Test of min method, of class Enumerator.
     */
    @Test
    public void testMin() {
        System.out.println("min");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .min(Comparator.comparingInt(i -> i))
                               .get()
                               .intValue(), 0);
    }

    /**
     * Test of noneMatch method, of class Enumerator.
     */
    @Test
    public void testNoneMatch() {
        System.out.println("noneMatch");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .noneMatch(i -> i<0));
        assertTrue(!Enumerator.rangeInt(0, 100)
                              .noneMatch(i -> i<1));
    }

    /**
     * Test of peek method, of class Enumerator.
     */
    @Test
    public void testPeek() {
        System.out.println("peek");
        assertEquals(Enumerator.rangeInt(0, 10)
                               .peek(i -> System.out.println(i))
                               .count(), 10);
    }

    /**
     * Test of prepend method, of class Enumerator.
     */
    @Test
    public void testPrepend() {
        System.out.println("prepend");
        assertTrue(Enumerator.on(3, 4, 5)
                             .prepend(0, 1, 2)
                             .elementsEqual(Enumerator.rangeInt(0, 6)));
    }

    /**
     * Test of reduce method, of class Enumerator.
     */
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

    /**
     * Test of reduce method, of class Enumerator.
     */
    @Test
    public void testReduce_Identity() {
        System.out.println("reduce with identity");
        assertEquals(Enumerator.rangeLongClosed(1, 10)
                               .reduce(0L, (x, y) -> x+y)
                               .longValue(),
                     10L*(10+1)/2);
    }

    /**
     * Test of reverse method, of class Enumerator.
     */
    @Test
    public void testReverse() {
        System.out.println("reverse");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .reverse()
                               .last()
                               .get()
                               .intValue(), 0);
    }

    /**
     * Test of single method, of class Enumerator.
     */
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

    /**
     * Test of skip method, of class Enumerator.
     */
    @Test
    public void testSkip() {
        System.out.println("skip");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .skip(10)
                               .count(), 90);
    }

    /**
     * Test of skipWhile method, of class Enumerator.
     */
    @Test
    public void testSkipWhile() {
        System.out.println("skipWhile");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .skipWhile(i -> i<30)
                               .count(), 70);
    }

    /**
     * Test of sorted method, of class Enumerator.
     */
    @Test
    public void testSorted() {
        System.out.println("sorted");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .reverse()
                             .sorted()
                             .elementsEqual(Enumerator.rangeInt(0, 100)));
    }

    /**
     * Test of sorted method, of class Enumerator.
     */
    @Test
    public void testSorted_Comparator() {
        System.out.println("sorted");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .sorted(Comparator.comparingInt(i -> -i))
                             .elementsEqual(Enumerator.rangeInt(0, 100)
                                                      .reverse()));
    }

    /**
     * Test of toArray method, of class Enumerator.
     */
    @Test
    public void testToArray() {
        System.out.println("toArray");
        Long[] longs = Enumerator.rangeLong(0, 100).toArray(Long.class);
        for(int i = 0; i<100; ++i) {
            assertEquals(longs[i], Long.valueOf(i));
        }
    }

    /**
     * Test of toList method, of class Enumerator.
     */
    @Test
    public void testToList() {
        System.out.println("toList");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .elementsEqual(Enumerator.rangeInt(0, 100)
                                                      .toList()
                                                      .iterator()));
    }

    /**
     * Test of toMap method, of class Enumerator.
     */
    @Test
    public void testToMap() {
        System.out.println("toMap");
        Map<Long, String> map = Enumerator.rangeLong(0, 100)
                                          .toMap(i -> i, i -> i.toString());
        for(Long x : map.keySet()) {
            assertEquals(map.get(x), x.toString());
        }
    }

    /**
     * Test of toSet method, of class Enumerator.
     */
    @Test
    public void testToSet() {
        System.out.println("toSet");
        assertEquals(Enumerator.repeat(100, 5).toSet().size(), 1);
    }

    /**
     * Test of union method, of class Enumerator.
     */
    @Test
    public void testUnion() {
        System.out.println("union");
        assertTrue(Enumerator.rangeInt(0, 100)
                             .union(Enumerator.rangeInt(50, 150))
                             .elementsEqual(Enumerator.rangeInt(0, 150)));
    }

    /**
     * Test of zipBoth method, of class Enumerator.
     */
    @Test
    public void testZipBoth() {
        System.out.println("zipBoth");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .zipBoth(Enumerator.rangeInt(100, 200))
                               .count(), 100);
    }

    /**
     * Test of zipLeft method, of class Enumerator.
     */
    @Test
    public void testZipLeft() {
        System.out.println("zipLeft");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .zipLeft(Enumerator.rangeInt(50, 200))
                               .count(), 100);
    }

    /**
     * Test of zipRight method, of class Enumerator.
     */
    @Test
    public void testZipRight() {
        System.out.println("zipRight");
        assertEquals(Enumerator.rangeInt(0, 100)
                               .zipRight(Enumerator.rangeInt(50, 200))
                               .count(), 150);
    }

    /**
     * Test of zipAny method, of class Enumerator.
     */
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
}
