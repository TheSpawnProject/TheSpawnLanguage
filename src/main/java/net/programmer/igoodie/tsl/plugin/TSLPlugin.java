package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.registry.TSLRegistry;

public abstract class TSLPlugin {

    protected TheSpawnLanguage language;
    protected TSLPluginManifest manifest;

    public TSLPluginManifest getManifest() {
        return manifest;
    }

    public String prependNamespace(String value) {
        return manifest.getPluginId() + ":" + value;
    }

    public void setLanguage(TheSpawnLanguage language) {
        if (this.language != null)
            throw new IllegalStateException();
        this.language = language;
    }

    public TheSpawnLanguage getLanguage() {
        return language;
    }

    public void registerTags(TSLRegistry<TSLTag> registry) {}

    public void registerDecorators(TSLRegistry<TSLDecorator> registry) {}

    public void registerEvents(TSLRegistry<TSLEvent> registry) {}

    public void registerActions(TSLRegistry<TSLAction> registry) {}

    public void registerComparators(TSLRegistry<TSLComparator> registry) {}

    public void registerPredicates(TSLRegistry<TSLPredicate> registry) {}

    public void registerFunctions(TSLRegistry<TSLFunction> registry) {}

}
