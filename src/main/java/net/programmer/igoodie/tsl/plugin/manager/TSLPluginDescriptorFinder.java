package net.programmer.igoodie.tsl.plugin.manager;

import net.programmer.igoodie.tsl.plugin.TSLPluginDescriptor;
import org.pf4j.DefaultPluginDescriptor;
import org.pf4j.ManifestPluginDescriptorFinder;
import org.pf4j.PluginDescriptor;
import org.pf4j.util.StringUtils;

import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class TSLPluginDescriptorFinder extends ManifestPluginDescriptorFinder {

    public static final String PLUGIN_PLATFORM = "Plugin-Platform";
    public static final String PLUGIN_NAME = "Plugin-Name";

    @Override
    protected DefaultPluginDescriptor createPluginDescriptorInstance() {
        return new TSLPluginDescriptor();
    }

    @Override
    protected PluginDescriptor createPluginDescriptor(Manifest manifest) {
        PluginDescriptor pluginDescriptor = super.createPluginDescriptor(manifest);

        Attributes attributes = manifest.getMainAttributes();

        String platform = attributes.getValue(PLUGIN_PLATFORM);
        if (StringUtils.isNullOrEmpty(platform)) {
            platform = "*";
        }

        String name = attributes.getValue(PLUGIN_NAME);
        if (StringUtils.isNullOrEmpty(name)) {
            name = "Unknown Plugin";
        }

        String dependencies = attributes.getValue(PLUGIN_DEPENDENCIES);
        if (dependencies == null) {
            dependencies = "";
        }

        return new TSLPluginDescriptor(
                platform,
                pluginDescriptor.getPluginClass(),
                pluginDescriptor.getPluginId(),
                name,
                pluginDescriptor.getPluginDescription(),
                pluginDescriptor.getVersion(),
                pluginDescriptor.getProvider(),
                pluginDescriptor.getLicense(),
                pluginDescriptor.getRequires(),
                Arrays.asList(dependencies.split(","))
        );
    }

}
