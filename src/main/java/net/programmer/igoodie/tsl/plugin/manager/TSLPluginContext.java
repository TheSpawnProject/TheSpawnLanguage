package net.programmer.igoodie.tsl.plugin.manager;

import org.pf4j.PluginWrapper;

public class TSLPluginContext {

    protected PluginWrapper wrapper;

    public TSLPluginContext(PluginWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public PluginWrapper getWrapper() {
        return wrapper;
    }

}
