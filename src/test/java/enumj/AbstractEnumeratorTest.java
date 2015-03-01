/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.NoSuchElementException;
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

    /**
     * Test of next method, of class AbstractEnumerator.
     */
    @Test(expected = NoSuchElementException.class)
    public void testNextFail() {
        System.out.println("hasNext fail");
        enumerator.next();
    }

    @Test
    public void testNext() {
        System.out.println("hasNext");
        
        enumerator.hasNext = true;
        enumerator.value = "value";
        assertTrue(enumerator.hasNext());
        assertNotNull(enumerator.next());
    }

    /**
     * Test of nextValue method, of class AbstractEnumerator.
     */
    @Test
    public void testNextValue() {
        System.out.println("nextValue");
        
        enumerator.hasNext = true;
        enumerator.value = "value";
        assertSame(enumerator.next(), enumerator.nextValue());
    }

    public class AbstractEnumeratorImpl<E> extends AbstractEnumerator {
        
        public boolean hasNext;
        public E value;

        @Override
        protected boolean mayContinue() {
            return hasNext;
        }
        
        @Override
        protected E nextValue() {
            return value;
        }
    }
}
