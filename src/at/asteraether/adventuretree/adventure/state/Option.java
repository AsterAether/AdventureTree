package at.asteraether.adventuretree.adventure.state;

import at.asteraether.adventuretree.adventure.variable.action.OptionAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Option implements Serializable {

    private String title;
    private State next;
    private final List<OptionAction> actions;

    public Option(String title) {
        this(title, null);
    }

    public Option(String title, State next) {
        this.title = title;
        this.next = next;
        actions = new ArrayList<>();
    }

    public void setNext(State next) {
        this.next = next;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public State getNext() {
        return next;
    }

    public void addAction(OptionAction action) {
        actions.add(action);
    }

    public OptionAction[] getActions() {
        return actions.toArray(new OptionAction[0]);
    }
}
