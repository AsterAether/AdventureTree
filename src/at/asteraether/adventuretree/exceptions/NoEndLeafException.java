package at.asteraether.adventuretree.exceptions;

public class NoEndLeafException extends RuntimeException {

    public NoEndLeafException() {
        super("No end leaf found");
    }
}
