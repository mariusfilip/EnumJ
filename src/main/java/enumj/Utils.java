/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import org.apache.commons.lang3.mutable.MutableBoolean;

/**
 *
 * @author Marius Filip
 */
class Utils {
    public static <T> void ensureNotNull(T something, String message) {
        if (something == null) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void ensureNonNegative(long something, String message) {
        if (something < 0) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void ensureLessThan(long something, long limit,
                                      String message) {
        if (something >= limit) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void ensureNonEnumerating(Enumerator<?> enumerator) {
        if (enumerator.enumerating()) {
            throw new IllegalStateException(Messages.ILLEGAL_ENUMERATOR_STATE);
        }
    }
    public static void ensureOnce(MutableBoolean flag) {
        if (flag.booleanValue()) {
            throw new IllegalStateException(Messages.ILLEGAL_MULTIPLE_CALLS);
        }
        flag.setValue(true);
    }
}
