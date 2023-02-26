package net.programmer.igoodie.tsl.plugin;

import org.pf4j.DefaultPluginDescriptor;

import java.util.List;

public class TSLPluginDescriptor extends DefaultPluginDescriptor {

    protected String pluginName;

    public TSLPluginDescriptor(
            String pluginClass,
            String pluginId,
            String pluginName,
            String pluginDescription,
            String pluginVersion,
            String pluginAuthor,
            String license,
            List<String> dependencies
    ) {
        super(
                pluginId,
                pluginDescription,
                pluginClass,
                pluginVersion,
                String.join(", ", dependencies),
                pluginAuthor,
                license
        );
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

}
