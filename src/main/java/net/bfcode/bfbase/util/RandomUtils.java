package net.bfcode.bfbase.util;

import java.util.ArrayList;
import java.util.Random;

public class RandomUtils
{
    private static final String[] alpha;
    private static final String[] numeric;
    
    public static String randomAlphaNumeric(final Integer maxChar) {
        final ArrayList<String> alphaText = new ArrayList<String>();
        for (final String text : RandomUtils.alpha) {
            alphaText.add(text);
        }
        for (final String nubmer : RandomUtils.numeric) {
            alphaText.add(nubmer);
        }
        String randomName = null;
        for (int i = 0; i < maxChar; ++i) {
            final Random random = new Random();
            final Integer rand = random.nextInt(alphaText.size());
            if (randomName == null) {
                randomName = alphaText.get(rand);
            }
            randomName += alphaText.get(rand);
        }
        return randomName;
    }
    
    public static Integer randomNumeric(final Integer maxChar) {
        final ArrayList<String> alphaText = new ArrayList<String>();
        for (final String nubmer : RandomUtils.numeric) {
            alphaText.add(nubmer);
        }
        String randomName = null;
        for (int i = 0; i < maxChar; ++i) {
            final Random random = new Random();
            final Integer rand = random.nextInt(alphaText.size());
            if (randomName == null) {
                randomName = alphaText.get(rand);
            }
            randomName += alphaText.get(rand);
        }
        return Integer.parseInt(randomName);
    }
    
    public static String randomString(final Integer maxChar) {
        final ArrayList<String> alphaText = new ArrayList<String>();
        for (final String text : RandomUtils.alpha) {
            alphaText.add(text);
        }
        String randomName = null;
        for (int i = 0; i < maxChar; ++i) {
            final Random random = new Random();
            final Integer rand = random.nextInt(alphaText.size());
            if (randomName == null) {
                randomName = alphaText.get(rand);
            }
            randomName += alphaText.get(rand);
        }
        return randomName;
    }
    
    static {
        alpha = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
        numeric = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
    }
}
