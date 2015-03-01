/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class EnumerableEnumerationTest {

    public EnumerableEnumerationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        enum1 = Enumerator.rangeInt(1, 10);
        enum2 = Enumerator.rangeInt(1, 10);
        en = new EnumerableEnumeration<>(enum2);
    }
    private Enumerator<Integer> enum1;
    private Enumerator<Integer> enum2;
    private EnumerableEnumeration<Integer> en;

    @After
    public void tearDown() {
    }

    /**
     * Test of hasMoreElements method, of class EnumerableEnumeration.
     */
    @Test
    public void testHasMoreElements() {
        System.out.println("hasMoreElements");
        while(enum1.hasNext()) {
            assertTrue(en.hasMoreElements());
            enum1.next();
            en.nextElement();
        }
        assertTrue(!en.hasMoreElements());
    }

    /**
     * Test of nextElement method, of class EnumerableEnumeration.
     */
    @Test
    public void testNextElement() {
        System.out.println("nextElement");
        while(enum1.hasNext()) {
            assertTrue(en.hasMoreElements());
            assertEquals(enum1.next(), en.nextElement());
        }
    }
}
