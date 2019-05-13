package at.asteraether.adventuretree.adventure.state;

import at.asteraether.adventuretree.adventure.TextSpeed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {

    public static final int PREVIEW_SIZE = 10;

    private String text;
    private TextSpeed textSpeed;
    private final List<Option> options;
    private State next;

    public State(String text) {
        this(text, null, null);
    }

    public State(String text, TextSpeed textSpeed, State next) {
        this.text = text;
        this.textSpeed = textSpeed;
        this.next = next;
        options = new ArrayList<>();
    }

    public State(String text, State next) {
        this(text, null, next);
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public String getText() {
        return text;
    }

    public Option[] getOptions() {
        return options.toArray(new Option[0]);
    }

    public TextSpeed getTextSpeed() {
        return textSpeed;
    }

    public void setNext(State next) {
        this.next = next;
    }

    public State getNext() {
        return next;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSpeed(TextSpeed textSpeed) {
        this.textSpeed = textSpeed;
    }

    public void setOptions(List<Option> options) {
        this.options.clear();
        this.options.addAll(options);
    }

    @Override
    public String toString() {
        if (text.length() <= PREVIEW_SIZE) {
            return text;
        }
        return text.substring(0, 10);
    }
}
