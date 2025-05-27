package org.quizfreely.classes.services;

import java.security.SecureRandom;
import java.util.Random;

public class ClassCodes {
    private static final Random random = new SecureRandom();
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int digit = 0; digit < length; digit++) {
            sb.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }
}

