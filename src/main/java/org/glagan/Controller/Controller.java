package org.glagan.Controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.glagan.Core.Action;
import org.glagan.Core.Game;

public class Controller {
    protected Game game;

    protected String context;

    protected HashMap<String, String> functions;

    protected Controller(Game game, String context, HashMap<String, String> functions) {
        this.game = game;
        this.context = context;
        this.functions = functions;
    }

    public boolean match(Action action) {
        return action.getContext().equals(context);
    }

    public String execute(Action action) {
        String functionForAction = functions.get(action.getAction());
        if (functionForAction != null) {
            try {
                return (String) this.getClass().getMethod(functionForAction, Action.class).invoke(this, action);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
                return "Failed to execute action `" + action.getAction() + "` on controller `"
                        + this.getClass().getSimpleName() + "`";
            }
        }
        return "Unknown action " + action.getAction() + " in Controller " + this.getClass().getSimpleName();
    }
}
