package net.programmer.igoodie.tsl.context;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;
import java.util.Map;

public class TSLContext {

    protected @NotNull TheSpawnLanguage tsl;

    public TSLContext(@NotNull TheSpawnLanguage tsl) {
        this.tsl = tsl;
    }

    public @NotNull TheSpawnLanguage getTsl() {
        return tsl;
    }

    /* ----------------------------------- */

    protected @Nullable ScriptableObject ruleScope;

    public @Nullable ScriptableObject getRuleScope() {
        return ruleScope;
    }

    public void setRuleScope(@NotNull ScriptableObject ruleScope) {
        this.ruleScope = ruleScope;
    }

    /* ----------------------------------- */

    protected @Nullable TSLEvent event;

    public @Nullable TSLEvent getEvent() {
        return event;
    }

    public void setEvent(@NotNull TSLEvent event) {
        this.event = event;
    }

    /* ----------------------------------- */

    protected @NotNull GoodieObject eventArguments = new GoodieObject();

    public @NotNull GoodieObject getEventArguments() {
        return eventArguments;
    }

    public void setEventArguments(@NotNull GoodieObject eventArguments) {
        this.eventArguments = eventArguments;
    }

    /* ----------------------------------- */

    protected @NotNull GoodieObject attributes = new GoodieObject();

    public @NotNull GoodieObject getAttributes() {
        return attributes;
    }

    public void setAttributes(@NotNull GoodieObject attributes) {
        this.attributes = attributes;
    }

    /* ----------------------------------- */

    public @NotNull Map<String, String> importedPlugins = new HashMap<>();

    public @NotNull Map<String, String> getImportedPlugins() {
        return importedPlugins;
    }

    public void setImportedPlugins(@NotNull Map<String, String> importedPlugins) {
        this.importedPlugins = importedPlugins;
    }

    /* ----------------------------------- */

    protected @Nullable TSLToken messageToken;

    public @Nullable TSLToken getMessageToken() {
        return messageToken;
    }

    public void setMessageToken(@Nullable TSLToken messageToken) {
        this.messageToken = messageToken;
    }

}
