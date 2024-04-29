package io.wdsj.homoium.util;

public class FastMath {
    /**
     * @author FX - PR0CESS
     * ~1.25x faster than {@link Math#round(float)}
     */
    public static int round(float a) {
        if (true) return a > 0F ? (int)(a + .5F) : (int)(a - .5F);
        return Math.round(a);
    }

    /**
     * @author FX - PR0CESS
     * ~1.28x faster than {@link Math#round(double)}
     */
    public static long round(double a) {
        if (true) return a > 0D ? (long)(a + .5D) : (long)(a - .5D);
        return Math.round(a);
    }
}
