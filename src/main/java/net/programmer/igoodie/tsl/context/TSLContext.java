package net.programmer.igoodie.tsl.context;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.LinkedList;
import java.util.List;

public class TSLContext {

    protected JSEngine engine;

    public JSEngine getEngine() {
        return engine;
    }

    public void setEngine(JSEngine engine) {
        this.engine = engine;
    }

    /* ----------------------------------- */

    protected JsonObject eventArguments = new JsonObject();

    public JsonObject getEventArguments() {
        return eventArguments;
    }

    public void setEventArguments(JsonObject eventArguments) {
        this.eventArguments = eventArguments;
    }

    /* ----------------------------------- */

    protected List<TSLToken> actionTokens = new LinkedList<>();

    public List<TSLToken> getActionTokens() {
        return actionTokens;
    }

    public void setActionTokens(List<TSLToken> actionTokens) {
        this.actionTokens = actionTokens;
    }

}
