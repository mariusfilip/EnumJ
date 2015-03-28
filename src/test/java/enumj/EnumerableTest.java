/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Arrays;
import java.util.Iterator;
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
        iterator = Arrays.asList(1, 2, 3).iterator();
        enumerable = new OnceEnumerable(iterator);
    }
    Iterator<Integer> iterator;
    OnceEnumerable<Integer> enumerable;

    @After
    public void tearDown() {
    }

    /**
     * Test of iterator method, of class OnceEnumerable.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        assertSame(enumerable.iterator(), iterator);
    }
}
