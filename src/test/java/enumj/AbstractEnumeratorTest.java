/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.NoSuchElementException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEnumeratorTest {

    public AbstractEnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        enumerator = new AbstractEnumeratorImpl();
    }
    AbstractEnumeratorImpl<String> enumerator;

    @After
    public void tearDown() {
        enumerator = null;
    }

    @Test
    public void testEnumerating() {
        System.out.println("enumerating");
        assertFalse(enumerator.enumerating());
    }

    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        assertFalse(enumerator.hasNext());
    }

    @Test
    public void testNext() {
        System.out.println("hasNext");

        enumerator.hasNext = true;
        enumerator.value = "value";
        assertTrue(enumerator.hasNext());
        assertNotNull(enumerator.next());
        assertSame(enumerator.next(), enumerator.internalNext());
    }

    @Test
    public void testInternalHasNext() {
        System.out.println("internalHasNext");
        assertFalse(enumerator.internalHasNext());
        enumerator.hasNext = true;
        assertTrue(enumerator.internalHasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testInternalNextThrow() {
        System.out.println("internalNext");
        assertNull(enumerator.next());
    }

    @Test
    public void testInternalNext() {
        System.out.println("internalNext");
        enumerator.hasNext = true;
        enumerator.value = "value";
        assertEquals("value", enumerator.internalNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCleanup() {
        System.out.println("cleanup");
        enumerator.cleanupThrow = true;
        assertFalse(enumerator.hasNext());
    }

    public class AbstractEnumeratorImpl<E> extends AbstractEnumerator {

        public boolean hasNext;
        public E value;
        public boolean cleanupThrow;

        @Override
        protected boolean internalHasNext() {
            return hasNext;
        }

        @Override
        protected E internalNext() {
            return value;
        }
        
        @Override
        protected void cleanup() {
            super.cleanup();
            if (cleanupThrow) {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Test
    public void testDistinct() {
        System.out.println("distinct");
        assertTrue(Reversible.distinct(Enumerator.on(1, 1), false)
                                     .elementsEqual(Enumerator.on(1)));
        assertTrue(Enumerable.on(1, 1)
                             .distinct()
                             .elementsEqual(Enumerable.on(1)));
    }

    @Test
    public void testIndexedMap() {
        System.out.println("indexedMap");
        assertTrue(Reversible
                .indexedMap(Enumerator.on(1, 2, 3),
                            (x,i) -> x.intValue()+i.intValue(),
                            false)
                .elementsEqual(Enumerator.on(1, 3, 5)));
        assertTrue(Enumerable.on(1, 2, 3)
                             .indexedMap((x,i) -> x.intValue()+i.intValue())
                             .elementsEqual(Enumerable.on(1, 3, 5)));
    }

    @Test
    public void testPeek() {
        System.out.println("peek");
        final MutableInt x = new MutableInt(0);
        Reversible
                .peek(Enumerator.on(1, 2, 3, 4, 5),
                      e -> x.add(e),
                      false)
                .forEach(y -> {});
        assertEquals(1+2+3+4+5, x.intValue());
        Enumerable.on(1, 2, 3, 4, 5)
                  .peek(e -> x.add(e))
                  .forEach(y -> {});
        assertEquals(2*(1+2+3+4+5), x.intValue());        
    }
}
