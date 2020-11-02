package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.tsl.registry.*;

public abstract class TSLPlugin {

    protected TSLPluginManifest manifest;

    public TSLPlugin(TSLPluginManifest manifest) {
        this.manifest = manifest;
    }

    public TSLPluginManifest getManifest() {
        return manifest;
    }

    public String prependNamespace(String value) {
        return manifest.getPluginId() + ":" + value;
    }

    public void registerTags(TagRegistry registry) {}

    public void registerDecorators(DecoratorRegistry registry) {}

    public void registerEvents(EventRegistry registry) {}

    public void registerEventFields(EventFieldRegistry registry) {}

    public void registerActions(ActionRegistry registry) {}

    public void registerComparator(ComparatorRegistry registry) {}

    public void registerFunctions(FunctionRegistry registry) {}

}
