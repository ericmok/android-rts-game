package tests;

import junit.framework.TestCase;

import java.util.Comparator;

import structure.RewriteOnlyArray;

/**
 * Created by eric on 3/11/15.
 */
public class RewriteOnlyArrayTest extends TestCase {

    public Comparator<A> comparator = new Comparator<A>() {
        @Override
        public int compare(A a, A a2) {
            if (a.value < a2.value)
                return -1;
            else if (a.value > a2.value)
                return 1;
            else
                return 0;
        }
    };

    public static class A {
        public double value = 0;
    }

    public RewriteOnlyArrayTest() {
    }

    public void testQuickSort() {
        RewriteOnlyArray<A> rewriteOnlyArray = new RewriteOnlyArray<A>(A.class, 4);
        rewriteOnlyArray.resetWriteIndex();

        A a0 = rewriteOnlyArray.takeNextWritable();
        a0.value = 7;
        A a1 = rewriteOnlyArray.takeNextWritable();
        a1.value = 2;
        A a2 = rewriteOnlyArray.takeNextWritable();
        a2.value = 4;
        A a3 = rewriteOnlyArray.takeNextWritable();
        a3.value = 5;

        assertTrue(rewriteOnlyArray.size() == 4);

        assertTrue(comparator.compare(a0, a1) == 1);
        assertTrue(comparator.compare(a1, a2) == -1);
        assertTrue(comparator.compare(a2, a3) == -1);

        rewriteOnlyArray.sort(comparator);

        for (int i = 0; i < 3; i++) {
            assertTrue(comparator.compare(rewriteOnlyArray.get(i), rewriteOnlyArray.get(i + 1)) < 0);
        }
    }
}
