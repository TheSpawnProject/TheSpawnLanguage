package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.util.ISerializable;

public class TSLPluginManifest implements ISerializable<GoodieObject> {

    protected String pluginId, name, version;
    protected String author;

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

    public TSLPluginManifest setAuthor(String author) {
        this.author = author;
        return this;
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
        this.pluginId = goodie.get("PluginID").asPrimitive().getString();
        this.name = goodie.get("Name").asPrimitive().getString();
        this.version = goodie.get("Version").asPrimitive().getString();
        this.author = goodie.get("Author").asPrimitive().getString();
    }

}
