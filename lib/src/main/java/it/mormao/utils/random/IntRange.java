package it.mormao.utils.random;

public record IntRange(int start, int end) {
    public IntRange {
        if (start < 0)
            throw new IndexOutOfBoundsException("Range start should be >= 0 but is " + start);
        if (end < 0)
            throw new IndexOutOfBoundsException("Range end should be >= 0 but is " + end);
        if (end < start)
            throw new IllegalArgumentException("Range boundaries appears to be inverted: from " + start  + " to " + end);
    }

    public static IntRange of(char start, char end) {
        return new IntRange(start, (int) end + 1);
    }

    public static IntRange singleton(final int num) {
        return new IntRange(num, num + 1);
    }

    public int length() {
        return end - start;
    }
}
