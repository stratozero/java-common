package it.mormao.utils.random;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public class RandomStringGenerator {
    protected final Random random;
    protected final CharSequence alphabet;

    private static final CharSequence DEFAULT_ALPHABET = AsciiAlphabet.WITH_ALL_CHARS;

    private static final RandomStringGenerator DEFAULT_GENERATOR = new RandomStringGenerator(DEFAULT_ALPHABET, RandomHolder.RANDOM);

    private static class RandomHolder {
        private static final SecureRandom RANDOM = new SecureRandom();
    }

    public static RandomStringGenerator defaultGenerator() {
        return DEFAULT_GENERATOR;
    }

    public RandomStringGenerator(final CharSequence alphabet, final Random random){
        this.random = Objects.requireNonNull(random, "random");
        this.alphabet = Objects.requireNonNull(alphabet, "alphabet");
    }

    public String newRandomString(int size){
        if (size < 0)
            throw new IllegalArgumentException("Invalid string size requested: " + size);
        if (size == 0)
            return "";

        final char[] result = new char[size];
        final int len = alphabet.length();
        for (int i = 0; i < size; i++) {
            result[i] = alphabet.charAt(random.nextInt(len));
        }

        return String.valueOf(result);
    }

    public static String newAlfanumericRandomString(int size) {
        return RandomStringGenerator.defaultGenerator().newRandomString(size);
    }

    public static String newAlfanumericRandomString(int size, Random random) {
        return new RandomStringGenerator(DEFAULT_ALPHABET, random).newRandomString(size);
    }

    public static String newAsciiRandomString(int size, AsciiAlphabet alphabet) {
        return new RandomStringGenerator(alphabet, RandomHolder.RANDOM).newRandomString(size);
    }

    public static String newAsciiRandomString(int size, AsciiAlphabet alphabet, Random random) {
        return new RandomStringGenerator(alphabet, random).newRandomString(size);
    }

}
