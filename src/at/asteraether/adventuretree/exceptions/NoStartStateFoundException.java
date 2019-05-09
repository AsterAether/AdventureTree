package at.asteraether.adventuretree.exceptions;

public class NoStartStateFoundException extends RuntimeException {
    public NoStartStateFoundException() {
        super("No start state was found");
    }
}
