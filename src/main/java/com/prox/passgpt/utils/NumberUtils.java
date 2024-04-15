package com.prox.passgpt.utils;

import java.util.Random;

public class NumberUtils {
    private static Random random = new Random();

    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

}
