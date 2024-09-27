package it.mormao.utils.random;

import javax.annotation.Nonnull;
import java.util.*;

public class AsciiAlphabet implements CharSequence {

    private final IntRange[] ranges;
    private final int length;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean useDigits;
        private boolean useUpper;
        private boolean useLower;
        private boolean useUnderscore;

        private Builder(){}

        public Builder withDigits() {
            this.useDigits = true;
            return this;
        }

        public Builder withUpper() {
            this.useUpper = true;
            return this;
        }

        public Builder withLower() {
            this.useLower = true;
            return this;
        }

        public Builder withUnderscore() {
            this.useUnderscore = true;
            return this;
        }

        public AsciiAlphabet build() {
            if (!useDigits && !useUpper && !useLower && !useUnderscore)
                throw new IllegalStateException("You must specify either digits or upper or lower");

            return new AsciiAlphabet(generateRanges(useDigits, useUpper, useLower, useUnderscore));
        }

        private static IntRange[] generateRanges(boolean useDigits, boolean useUpper, boolean useLower, boolean useUnderscore) {
            int rangeNum = 0;
            if (useLower)      rangeNum++;
            if (useDigits)     rangeNum++;
            if (useUpper)      rangeNum++;
            if (useUnderscore) rangeNum++;

            final var ranges = new IntRange[rangeNum];

            int lastAdded = 0;
            if (useLower)      ranges[lastAdded++] = LOWERCASE_LETTERS;
            if (useDigits)     ranges[lastAdded++] = DIGITS;
            if (useUpper)      ranges[lastAdded++] = UPPERCASE_LETTERS;
            if (useUnderscore) ranges[lastAdded] = UNDERSCORE;

            return ranges;
        }
    }

    private AsciiAlphabet(IntRange... ranges) {
        this.ranges = ranges;
        this.length = Arrays.stream(ranges)
                .mapToInt(IntRange::length)
                .sum();
    }

    public static final IntRange
            DIGITS             = IntRange.of('0', '9'),
            UPPERCASE_LETTERS  = IntRange.of('A', 'Z'),
            UNDERSCORE         = IntRange.singleton('_'),
            LOWERCASE_LETTERS  = IntRange.of('a', 'z');


    public static final AsciiAlphabet WITH_ALL_CHARS = AsciiAlphabet.builder()
            .withLower()
            .withUpper()
            .withDigits()
            .withUnderscore()
            .build();


    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int pos) {
        checkValidIndex(pos);
        for (IntRange range : ranges) {
            final int rangeLen = range.length();
            if (pos < rangeLen)
                return (char) (range.start() + pos);
            pos -= rangeLen;
        }
        return (char) (ranges[length - 1].end() - 1);
    }

    @Override
    @Nonnull
    public CharSequence subSequence(int start, int end) {
        checkRange(start, end);
        if (end == start) return "";
        if (start == 0 && end == length()) return this;

        final List<IntRange> newRanges = new ArrayList<>();
        for (IntRange range : ranges) {
            final int rangeLen = range.length();
            if (end < 0)
                break;
            if (start < rangeLen) {
                newRanges.add((start == 0 && end >= rangeLen)
                        ? range
                        : new IntRange(start, Math.min(end, rangeLen))
                );
            }
            if (start > 0) start -= rangeLen;
            end -= rangeLen;
        }
        return new AsciiAlphabet(newRanges.toArray(new IntRange[0]));
    }

    private void checkValidIndex(int pos) {
        if (pos < 0)
            throw new IndexOutOfBoundsException("Index was expected to be >= 0, but was " + pos);
        if (pos >= length)
            throw new IndexOutOfBoundsException("Index was expected to be less than " + length + ", but was " + pos);
    }

    private void checkRange(int start, int end) {
        checkValidIndex(start);
        checkValidIndex(end);
        if (end < start)
            throw new IllegalArgumentException("End index was expected to be >= " + start + ", but was " + end);
    }

    @Nonnull
    public String toString() {
        final char[] str = new char[length];
        int i = 0;
        for (IntRange range : ranges) {
            for (int r = range.start(); r < range.end(); r++) {
                str[i++] = (char) r;
            }
        }
        return new String(str);
    }
}
