package net.programmer.igoodie.tsl.plugin;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.util.ISerializable;

public class TSLPluginManifest implements ISerializable<JsonObject> {

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
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("PluginID", pluginId);
        json.addProperty("Name", name);
        json.addProperty("Version", version);
        json.addProperty("Author", author);
        return json;
    }

    @Override
    public void deserialize(JsonObject json) {
        this.pluginId = json.get("PluginID").getAsString();
        this.name = json.get("Name").getAsString();
        this.version = json.get("Version").getAsString();
        this.author = json.get("Author").getAsString();
    }

}
