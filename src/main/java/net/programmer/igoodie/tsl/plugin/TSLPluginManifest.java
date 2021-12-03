package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.util.ISerializable;

import java.util.jar.Attributes;

public class TSLPluginManifest implements ISerializable<GoodieObject> {

    public static final String ATTR_PLUGIN_ID = "TSL-Plugin-Id";
    public static final String ATTR_PLUGIN_NAME = "TSL-Plugin-Name";
    public static final String ATTR_PLUGIN_VERSION = "TSL-Plugin-Version";
    public static final String ATTR_PLUGIN_AUTHOR = "TSL-Plugin-Author";

    protected String pluginId, name, version;
    protected String author;

    public TSLPluginManifest(Attributes jarAttributes) {
        this.pluginId = jarAttributes.getValue(ATTR_PLUGIN_ID);
        this.name = jarAttributes.getValue(ATTR_PLUGIN_NAME);
        this.version = jarAttributes.getValue(ATTR_PLUGIN_VERSION);
        this.author = jarAttributes.getValue(ATTR_PLUGIN_AUTHOR);
    }

    public TSLPluginManifest(String pluginId, String name, String version) {
        this.pluginId = pluginId;
        this.name = name;
        this.version = version;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public GoodieObject serialize() {
        GoodieObject goodie = new GoodieObject();
        goodie.put("PluginID", pluginId);
        goodie.put("Name", name);
        goodie.put("Version", version);
        goodie.put("Author", author);
        return goodie;
    }

    @Override
    public void deserialize(GoodieObject goodie) {
        this.pluginId = goodie.getString("PluginID").orElse(null);
        this.name = goodie.getString("Name").orElse(null);
        this.version = goodie.getString("Version").orElse(null);
        this.author = goodie.getString("Author").orElse(null);
    }

}
