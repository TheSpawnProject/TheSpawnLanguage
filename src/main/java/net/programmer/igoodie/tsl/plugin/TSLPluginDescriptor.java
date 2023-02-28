package net.programmer.igoodie.tsl.plugin;

import org.pf4j.DefaultPluginDescriptor;

import java.util.List;

public class TSLPluginDescriptor extends DefaultPluginDescriptor {

    protected String pluginName;
    protected String targetPlatform = "*";

    public TSLPluginDescriptor() {
        super();
    }

    public TSLPluginDescriptor(
            String targetPlatform,
            String pluginClass,
            String pluginId,
            String pluginName,
            String pluginDescription,
            String pluginVersion,
            String pluginAuthor,
            String license,
            String requires,
            List<String> dependencies
    ) {
        super(
                pluginId,
                pluginDescription,
                pluginClass,
                pluginVersion,
                requires,
                pluginAuthor,
                license
        );
        this.setDependencies(String.join(", ", dependencies));
        this.targetPlatform = targetPlatform;
        this.pluginName = pluginName;
    }

    public String getTargetPlatform() {
        return targetPlatform;
    }

    public String getPluginName() {
        return pluginName;
    }

    protected void setTargetPlatform(String targetPlatform) {
        this.targetPlatform = targetPlatform;
    }

    protected void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    @Override
    public String toString() {
        return "TSLPluginDescriptor [pluginId=" + getPluginId()
                + ", pluginName=" + pluginName
                + ", targetPlatform=" + targetPlatform
                + ", pluginClass=" + getPluginClass()
                + ", version=" + getVersion()
                + ", provider=" + getProvider()
                + ", dependencies=" + getDependencies()
                + ", description=" + getPluginDescription()
                + ", requires=" + getRequires()
                + ", license=" + getLicense()
                + "]";
    }

}
