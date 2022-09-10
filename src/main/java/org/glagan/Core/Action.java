package org.glagan.Core;

public class Action {
    protected String action;

    protected String context;

    protected String[] arguments;

    public Action(String action, String context, String[] arguments) {
        this.action = action;
        this.context = context;
        this.arguments = arguments;
    }

    public String getAction() {
        return action;
    }

    public String getContext() {
        return context;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getArgument(int index) {
        return arguments[index];
    }

    public boolean hasAtLeastXArguments(int amount) {
        if (arguments == null) {
            return false;
        }
        return arguments.length >= amount;
    }
}
