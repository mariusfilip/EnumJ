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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullableTest {

    public NullableTest() {
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
    public void testIsPresent() {
        System.out.println("isPresent");
        assertTrue(Nullable.of("something").isPresent());
        assertFalse(Nullable.empty().isPresent());
    }

    @Test
    public void testGet() {
        System.out.println("get");
        assertEquals("something", Nullable.of("something").get());
        try {
            assertNotNull(Nullable.empty().get());
        } catch(NoSuchElementException ex) {
            // do nothing
        }
    }

    @Test
    public void testSet() {
        System.out.println("set");
        final Nullable<String> n = Nullable.empty();
        n.set("something");
        assertEquals("something", n.get());
    }

    @Test
    public void testClear() {
        System.out.println("clear");
        final Nullable<String> n = Nullable.of("something");
        n.clear();
        assertFalse(n.isPresent());
    }

    @Test
    public void testOrElse() {
        System.out.println("orElse");
        assertEquals("something", Nullable.of("something").orElse("anything"));
        assertEquals("something", Nullable.empty().orElse("something"));
    }

    @Test
    public void testOrElseGet() {
        System.out.println("orElseGet");
        assertEquals("something", Nullable.of("something")
                                          .orElseGet(() -> "anything"));
        assertEquals("something", Nullable.empty()
                                          .orElseGet(() -> "something"));
    }

    @Test(expected = IllegalStateException.class)
    public void testOrElseThrow() throws Exception {
        System.out.println("orElseThrow");
        final Nullable<String> n = Nullable.of("something");
        final Nullable<String> e = Nullable.empty();
        assertEquals("something",
                     n.orElseThrow(() -> new NoSuchElementException()));
        assertNotNull(e.orElseThrow(() -> new IllegalStateException()));
    }

    @Test
    public void testEquals() {
        System.out.println("equals");
        assertEquals(Nullable.of("something"), Nullable.of("something"));
        assertFalse(Nullable.of("something").equals(Nullable.of("else")));
        assertFalse(Nullable.of("something").equals("something"));
        
        final Nullable<String> n = Nullable.of("something");
        assertEquals(n, n);
        assertFalse(n.equals(Nullable.empty()));
    }

    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        assertEquals("something".hashCode(),
                    Nullable.of("something").hashCode());
        assertEquals(Nullable.of("something").hashCode(),
                     Nullable.of("something").hashCode());
        assertFalse(Nullable.empty().equals(Nullable.empty()));
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        assertFalse("something".equals(Nullable.of("something").toString()));
    }

    @Test
    public void testEmpty() {
        System.out.println("empty");
        assertFalse(Nullable.empty().isPresent());
    }

    @Test
    public void testOf_GenericType() {
        System.out.println("of");
        assertTrue(Nullable.of("something").isPresent());
    }

    @Test
    public void testOf_Optional() {
        System.out.println("of");
        assertTrue(Nullable.of(Optional.of("something")).isPresent());
        assertFalse(Nullable.of(Optional.empty()).isPresent());
    }

    @Test
    public void testFilter() {
        System.out.println("filter");
        assertTrue(Nullable.of(3).filter(i -> i > 0).isPresent());
        assertFalse(Nullable.of(-3).filter(i -> i > 0).isPresent());
    }

    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
        assertEquals(Nullable.of("something")
                             .flatMap(s -> Nullable.of(s))
                             .get(),
                     "something");
    }

    @Test
    public void testIfPresent() {
        System.out.println("ifPresent");
        final AtomicInteger c = new AtomicInteger(0);
        Nullable.empty()
                .ifPresent(x -> c.incrementAndGet());
        Nullable.of("something")
                .ifPresent(s -> c.incrementAndGet());
        assertEquals(1, c.get());
    }

    @Test
    public void testMap() {
        System.out.println("map");
        assertEquals("somethingsomething",
                     Nullable.of("something")
                             .map(s -> s + s)
                             .get());
    }    
}
