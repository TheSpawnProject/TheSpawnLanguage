package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.tsl.registry.ActionRegistry;
import net.programmer.igoodie.tsl.registry.EventFieldRegistry;
import net.programmer.igoodie.tsl.registry.EventRegistry;
import net.programmer.igoodie.tsl.registry.FunctionRegistry;

public abstract class TSLPlugin {

    protected TSLPluginManifest manifest;

    public TSLPlugin(TSLPluginManifest manifest) {
        this.manifest = manifest;
    }

    public TSLPluginManifest getManifest() {
        return manifest;
    }

    public void registerEvents(EventRegistry registry) {}

    public void registerEventFields(EventFieldRegistry registry) {}

    public void registerActions(ActionRegistry registry) {}

    public void registerFunctions(FunctionRegistry registry) {}

}
