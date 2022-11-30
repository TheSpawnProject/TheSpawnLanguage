package net.programmer.igoodie.tsl.definition.base;

import net.programmer.igoodie.tsl.compat.LSPFeatures;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;

public abstract class TSLDefinition implements TSLRegistrable, LSPFeatures {

    protected TSLPlugin plugin;
    protected String name;

    public TSLDefinition(TSLPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public TSLPlugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getRegistryId() {
        return plugin.getManifest().getPluginId() + ":" + getName();
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getName());
    }

}
