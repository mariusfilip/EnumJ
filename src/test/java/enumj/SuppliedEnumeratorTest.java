/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Optional;
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
public class SuppliedEnumeratorTest {
    
    public SuppliedEnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        enumerator = new SuppliedEnumerator<>(this::supply);
    }
    private int start = 0;
    private int end = 10;
    SuppliedEnumerator<Integer> enumerator;

    private Optional<Integer> supply() {
        if (start >= end) {
            return Optional.empty();
        }
        return Optional.of(start++);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class SuppliedEnumerator.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        for(int i = start; i<end; ++i) {
            assertTrue(enumerator.hasNext());
            enumerator.next();
        }
        assertTrue(!enumerator.hasNext());
    }

    /**
     * Test of nextValue method, of class SuppliedEnumerator.
     */
    @Test
    public void testNextValue() {
        System.out.println("nextValue");
        for(int i = start; i<end; ++i) {
            assertEquals(enumerator.next(), Integer.valueOf(i));
        }
    }
}
