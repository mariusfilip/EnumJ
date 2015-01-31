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
public class WhileEnumeratorTest {

    public WhileEnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        iterator = Arrays.asList(nums).iterator();
        enumerator = new WhileEnumerator<>(iterator, i -> i<lim);
    }
    final Integer[] nums = { 0, 1, 2, 3, 4, 5, 6 };
    final int lim = 3;
    Iterator<Integer> iterator;
    WhileEnumerator<Integer> enumerator;

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        for(int i=0; i<lim; ++i) {
            assertTrue(enumerator.hasNext());
            assertEquals(enumerator.next(), Integer.valueOf(i));
        }
        assertTrue(!enumerator.hasNext());
    }
}
