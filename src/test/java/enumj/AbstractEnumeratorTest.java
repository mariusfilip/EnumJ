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
        assertNotNull(new Reversible());
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
        final Out<String> out = new InOut<>();
        enumerator.internalNext(out);
        assertSame(enumerator.next(), out.get());
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
        final Out<String> out = new InOut();
        enumerator.internalNext(out);
        assertEquals("value", out.get());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCleanup() {
        System.out.println("cleanup");
        enumerator.cleanupThrow = true;
        assertFalse(enumerator.hasNext());
    }

    public class AbstractEnumeratorImpl<E> extends AbstractEnumerator<E> {

        public boolean hasNext;
        public E       value;
        public boolean cleanupThrow;

        @Override
        protected boolean internalHasNext() {
            return hasNext;
        }

        @Override
        protected void internalNext(Out<E> value) {
            value.set(this.value);
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
                .map(Enumerator.on(1, 2, 3),
                            (x,i) -> x.intValue()+i.intValue(),
                            false)
                .elementsEqual(Enumerator.on(1, 3, 5)));
        assertTrue(Enumerable.on(1, 2, 3)
                             .map((x,i) -> x.intValue()+i.intValue())
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
