package net.programmer.igoodie.tsl.plugin;

import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.util.ISerializable;
import org.jetbrains.annotations.NotNull;

import java.util.jar.Attributes;

public class TSLPluginManifest implements ISerializable<GoodieObject> {

    public static final String ATTR_PLUGIN_ID = "TSL-Plugin-Id";
    public static final String ATTR_PLUGIN_NAME = "TSL-Plugin-Name";
    public static final String ATTR_PLUGIN_VERSION = "TSL-Plugin-Version";
    public static final String ATTR_PLUGIN_AUTHOR = "TSL-Plugin-Author";
    public static final String ATTR_VERSION_TARGET = "TSL-Version-Target";

    private @NotNull String pluginId, name;
    private @NotNull String targetVersion;
    private String author;
    private @NotNull String version;
    private @NotNull Semver semver;

    public TSLPluginManifest(Attributes jarAttributes) {
        this(jarAttributes.getValue(ATTR_PLUGIN_ID),
                jarAttributes.getValue(ATTR_PLUGIN_NAME),
                jarAttributes.getValue(ATTR_PLUGIN_VERSION),
                jarAttributes.getValue(ATTR_PLUGIN_AUTHOR),
                jarAttributes.getValue(ATTR_VERSION_TARGET));
    }

    public TSLPluginManifest(@NotNull String pluginId,
                             @NotNull String name,
                             @NotNull String version,
                             String author) throws SemverException {
        this(pluginId, name, version, author, TheSpawnLanguage.TSL_VERSION);
    }

    public TSLPluginManifest(@NotNull String pluginId,
                             @NotNull String name,
                             @NotNull String version,
                             String author,
                             @NotNull String targetVersion) throws SemverException {
        this.pluginId = pluginId;
        this.name = name;
        this.author = author;
        this.targetVersion = targetVersion;
        setVersion(version);
    }

    private void setVersion(String version) throws SemverException {
        this.version = version;
        this.semver = new Semver(version, Semver.SemverType.NPM);
    }

    public @NotNull String getPluginId() {
        return pluginId;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getVersion() {
        return version;
    }

    public @NotNull String getTargetVersion() {
        return targetVersion;
    }

    public @NotNull Semver getSemver() {
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
        this.pluginId = goodie.getString("PluginId").orElse("unknown");
        this.name = goodie.getString("Name").orElse("Unknown Plugin");
        setVersion(goodie.getString("Version").orElse("0.0.0"));
        this.targetVersion = goodie.getString("Target").orElse("0.0.0");
        this.author = goodie.getString("Author").orElse(null);
    }

}
