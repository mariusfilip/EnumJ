/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Enumeration;
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
public class EnumerationEnumeratorTest {
    
    public EnumerationEnumeratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        enum1 = Enumerator.rangeInt(0, 20);
        enum2 = Enumerator.rangeInt(0, 20);
        en = enum2.asEnumeration();
        enum3 = Enumerator.of(en);
    }
    private Enumerator<Integer> enum1;
    private Enumerator<Integer> enum2;
    private Enumeration<Integer> en;
    private Enumerator<Integer> enum3;
    
    @After
    public void tearDown() {
        enum1 = enum2 = enum3 = null;
        en = null;
    }

    /**
     * Test of mayContinue method, of class EnumerationEnumerator.
     */
    @Test
    public void testMayContinue() {
        System.out.println("mayContinue");
        while(enum3.hasNext()) {
            assertTrue(enum1.hasNext());
            enum3.next();
            enum1.next();
        }
        assertTrue(!enum1.hasNext());
    }

    /**
     * Test of nextValue method, of class EnumerationEnumerator.
     */
    @Test
    public void testNextValue() {
        System.out.println("nextValue");
        while(enum3.hasNext()) {
            assertTrue(enum1.hasNext());
            assertEquals(enum3.next(), enum1.next());
        }
        assertTrue(!enum1.hasNext());
    }
}
