package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
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

    protected @Nullable JSScope jsScope;

    public @Nullable JSScope getJsScope() {
        return jsScope;
    }

    public void setJsScope(@NotNull JSScope jsScope) {
        this.jsScope = jsScope;
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

    protected @Nullable File baseDir;

    public @Nullable File getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(@NotNull File baseDir) {
        this.baseDir = baseDir;
    }

    /* ----------------------------------- */

    // TODO: Consider turning importedPlugins and captureSnippets into associatedRuleset?
    // Maybe tsl performs without a ruleset should be discouraged?
    // A dummy ruleset can be used for extreme cases anyways

    public @NotNull Map<String, String> importedPlugins = new HashMap<>();

    public @NotNull Map<String, String> getImportedPlugins() {
        return importedPlugins;
    }

    public void setImportedPlugins(@NotNull Map<String, String> importedPlugins) {
        this.importedPlugins = importedPlugins;
    }

    /* ----------------------------------- */

    public @NotNull Map<String, TSLCaptureSnippet> captureSnippets = new HashMap<>();

    public @NotNull Map<String, TSLCaptureSnippet> getCaptureSnippets() {
        return captureSnippets;
    }

    public void setCaptureSnippets(@NotNull Map<String, TSLCaptureSnippet> captureSnippets) {
        this.captureSnippets = captureSnippets;
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
