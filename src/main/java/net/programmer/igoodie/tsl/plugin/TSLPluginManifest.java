package net.programmer.igoodie.tsl.plugin;

import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.util.ISerializable;

import java.util.jar.Attributes;

public class TSLPluginManifest implements ISerializable<GoodieObject> {

    public static final String ATTR_PLUGIN_ID = "TSL-Plugin-Id";
    public static final String ATTR_PLUGIN_NAME = "TSL-Plugin-Name";
    public static final String ATTR_PLUGIN_VERSION = "TSL-Plugin-Version";
    public static final String ATTR_PLUGIN_AUTHOR = "TSL-Plugin-Author";
    public static final String ATTR_VERSION_TARGET = "TSL-Version-Target";

    protected String pluginId, name;
    protected String targetVersion;
    protected String version;
    protected String author;
    private Semver semver;

    public TSLPluginManifest(Attributes jarAttributes) {
        this.pluginId = jarAttributes.getValue(ATTR_PLUGIN_ID);
        this.name = jarAttributes.getValue(ATTR_PLUGIN_NAME);
        this.author = jarAttributes.getValue(ATTR_PLUGIN_AUTHOR);
        this.targetVersion = jarAttributes.getValue(ATTR_VERSION_TARGET);
        setVersion(jarAttributes.getValue(ATTR_PLUGIN_VERSION));
    }

    private void setVersion(String version) throws SemverException {
        this.version = version;
        this.semver = new Semver(version, Semver.SemverType.NPM);
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

    public String getTargetVersion() {
        return targetVersion;
    }

    public Semver getSemver() {
        return semver;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public GoodieObject serialize() {
        GoodieObject goodie = new GoodieObject();
        goodie.put("PluginId", pluginId);
        goodie.put("Name", name);
        goodie.put("Version", version);
        goodie.put("Target", targetVersion);
        goodie.put("Author", author);
        return goodie;
    }

    @Override
    public void deserialize(GoodieObject goodie) {
        this.pluginId = goodie.getString("PluginId").orElse(null);
        this.name = goodie.getString("Name").orElse(null);
        this.version = goodie.getString("Version").orElse(null);
        this.targetVersion = goodie.getString("Target").orElse(null);
        this.author = goodie.getString("Author").orElse(null);
    }

}
