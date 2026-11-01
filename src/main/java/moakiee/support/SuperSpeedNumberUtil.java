/*
 * This file includes code adapted from MakeAE2Better by QiuYe.
 * Licensed under the MIT License.
 * Original Source: https://github.com/qiuye2024github/MaekAE2Better
 * Full license text: src/main/resources/LICENSE_MakeAE2Better.txt
 */
package moakiee.support;

import moakiee.Ae2OcConfig;

import java.lang.reflect.Field;

public final class SuperSpeedNumberUtil {

    private SuperSpeedNumberUtil() {
    }

    /** Bus speed boost only; ExtendedAE applies its own busSpeed separately. */
    public static int convertLongToIntSaturating(long value) {
        int multiplier = ae2oc_getSuperSpeedMultiplier();
        if (value > Integer.MAX_VALUE / multiplier) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE / multiplier) {
            return Integer.MIN_VALUE;
        }
        return (int) (value * multiplier);
    }

    /** Both operands are ints, so their product fits in a long before clamping. */
    public static int multiplyIntSaturating(int value, int multiplier) {
        long result = (long) value * multiplier;
        return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, result));
    }

    public static long boostLongSaturating(long value) {
        long multiplier = (long) ae2oc_getSuperSpeedMultiplier() * ae2oc_getExtendedAeBusSpeed();
        if (value <= 0L || multiplier <= 0L) {
            return 0L;
        }
        if (value > Long.MAX_VALUE / multiplier) {
            return Long.MAX_VALUE;
        }
        return value * multiplier;
    }

    private static int ae2oc_getExtendedAeBusSpeed() {
        try {
            Class<?> eppConfigClass = Class.forName("com.glodblock.github.extendedae.config.EPPConfig");
            Field busSpeedField = eppConfigClass.getDeclaredField("busSpeed");
            busSpeedField.setAccessible(true);
            int value = busSpeedField.getInt(null);
            return Math.max(value, 1);
        } catch (Throwable ignored) {
            return 1;
        }
    }

    private static int ae2oc_getSuperSpeedMultiplier() {
        return Math.max(Ae2OcConfig.getSuperSpeedCardMultiplier(), 1);
    }
}
