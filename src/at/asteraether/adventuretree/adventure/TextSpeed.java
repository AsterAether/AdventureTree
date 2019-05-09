package at.asteraether.adventuretree.adventure;

public enum TextSpeed {
    DEFAULT(25),
    SUPER_SLOW(100),
    SLOW(40),
    NORMAL(25),
    FAST(10),
    INSTANT(0);

    private final long textTimeout;

    TextSpeed(long textTimeout) {
        this.textTimeout = textTimeout;
    }

    public long getTextTimeout() {
        return textTimeout;
    }
}
