package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import org.jetbrains.annotations.Nullable;

public abstract class TSLPlugin {

    private TSLPluginManifest manifest;

    public TSLPlugin() {}

    public TSLPlugin(TSLPluginManifest manifest) {
        this.manifest = manifest;
    }

    public TSLPluginManifest getManifest() {
        return manifest;
    }

    public @Nullable String getDescription() {
        return null;
    }

    public @Nullable String getBannerURL() {
        return null;
    }

    public String prependNamespace(String value) {
        return manifest.getPluginId() + ":" + value;
    }

    public void registerTags(TSLRegistry<TSLTag> registry) {}

    public void registerDecorators(TSLRegistry<TSLDecorator> registry) {}

    public void registerEvents(TSLRegistry<TSLEvent> registry) {}

    public void registerActions(TSLRegistry<TSLAction> registry) {}

    public void registerComparators(TSLRegistry<TSLComparator> registry) {}

    public void registerPredicates(TSLRegistry<TSLPredicate> registry) {}

    public void registerFunctionLibraries(TSLRegistry<TSLFunctionLibrary> registry) {}

}
