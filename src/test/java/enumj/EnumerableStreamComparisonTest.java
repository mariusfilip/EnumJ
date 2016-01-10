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

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EnumerableStreamComparisonTest {

    public EnumerableStreamComparisonTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        rnd = new Random(9691);
        statistics = new StreamComparatorStatistics();
    }
    protected Random rnd;
    protected StreamComparatorStatistics statistics;

    @After
    public void tearDown() {
        statistics = null;
        rnd = null;
    }

    private void testLong(int maxDepth, int maxLength) {
        final LongEnumerableComparator cmp =
                new LongEnumerableComparator(
                        rnd,
                        maxDepth,
                        maxLength);
        cmp.statistics = statistics;
        cmp.test((x,y) -> {
                     assertTrue(y.isPresent());
                     assertEquals(x, y.get());
                 },
                 () -> assertTrue(false));
    }

    private void testLongRepeatedly(int repeats,
                                    int maxDepth,
                                    int maxLength) {
        for(int i=0; i<repeats; ++i) {
            testLong(maxDepth, maxLength);
        }
    }

    @Test
    public void testLongFlat() {
        System.out.println("testLongFlat");
        testLong(0, 1024);
        statistics.print();
    }

    @Test
    public void testLongFlatRepeatedly() {
        System.out.println("testLongFlatRepeatedly");
        testLongRepeatedly(512, 0, 1024);
        statistics.print();
    }

    @Test
    public void testLongDeep1() {
        System.out.println("testLongDeep1");
        testLong(1, 512);
        statistics.print();
    }

    @Test
    public void testLongDeep1Repeatedly() {
        System.out.println("testLongDeep1Repeatedly");
        testLongRepeatedly(4, 1, 512);
        statistics.print();
    }

    @Test
    public void testLongDeep2() {
        System.out.println("testLongDeep2");
        testLong(2, 128);
        statistics.print();
    }

    @Test
    public void testLongDeep2Repeatedly() {
        System.out.println("testLongDeep2Repeatedly");
        testLongRepeatedly(2, 2, 128);
        statistics.print();
    }

    @Test
    public void testLongDeep4() {
        System.out.println("testLongDeep4");
        testLong(4, 16);
        statistics.print();
    }

    @Test
    public void testLongDeep4Repeatedly() {
        System.out.println("testLongDeep4Repeatedly");
        testLongRepeatedly(8, 4, 16);
        statistics.print();
    }

    @Test
    public void testLongDeep8() {
        System.out.println("testLongDeep8");
        testLong(8, 8);
        statistics.print();
    }

    @Test
    public void testLongDeep8Repeatedly() {
        System.out.println("testLongDeep8Repeatedly");
        testLongRepeatedly(4, 8, 8);
        statistics.print();
    }
}
