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
public class AbstractEnumerableTest {

    public AbstractEnumerableTest() {
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
        final AbstractEnumerable instance = new AbstractEnumerableThrow();
        assertFalse(instance.enumerating());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnumerator() {
        System.out.println("enumerator");
        final AbstractEnumerable instance = new AbstractEnumerableThrow();
        assertNull(instance.enumerator());
    }

    @Test(expected = IllegalStateException.class)
    public void testOnceEnumeratorThrow() {
        System.out.println("onceEnumerator throw");
        final AbstractEnumerable instance = new AbstractEnumerableOnce();
        assertTrue(instance.elementsEqual(Enumerable.on(1, 2, 3)));
        assertTrue(instance.elementsEqual(Enumerable.on(1, 2, 3)));
    }

    @Test
    public void testOnceEnumeratorNonThrow() {
        System.out.println("onceEnumerator");
        final AbstractEnumerable instance = new AbstractEnumerableOnce();
        assertTrue(instance.elementsEqual(Enumerable.on(1, 2, 3)));
    }

    /**
     * Test of internalEnumerator method, of class AbstractEnumerable.
     */
    @Test
    public void testInternalEnumerator() {
        System.out.println("internalEnumerator");
        final AbstractEnumerable instance = new AbstractEnumerableNonThrow();
        assertTrue(instance.enumerator().elementsEqual(instance.iterator()));
    }

    public class AbstractEnumerableOnce extends AbstractEnumerable<Integer> {

        @Override
        protected boolean internalOnceOnly() {
            return true;
        }
        @Override
        protected Enumerator<Integer> internalEnumerator() {
            return Enumerator.on(1, 2, 3);
        }
    }

    public class AbstractEnumerableThrow extends AbstractEnumerable<Integer> {

        @Override
        protected boolean internalOnceOnly() {
            return false;
        }
        @Override
        protected Enumerator<Integer> internalEnumerator() {
            throw new UnsupportedOperationException();
        }
    }

    public class AbstractEnumerableNonThrow extends AbstractEnumerableThrow {
        @Override
        protected boolean internalOnceOnly() {
            return false;
        }
        @Override
        protected Enumerator<Integer> internalEnumerator() {
            return Enumerator.on(1, 2, 3, 4);
        }
    }
}
