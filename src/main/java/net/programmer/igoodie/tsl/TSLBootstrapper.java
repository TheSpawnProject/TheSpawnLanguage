package net.programmer.igoodie.tsl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TSLBootstrapper {

    protected List<String> pluginPaths = Collections.emptyList();

    public TSLBootstrapper pluginPaths(String... pluginPaths) {
        this.pluginPaths = Arrays.asList(pluginPaths);
        return this;
    }

    public TheSpawnLanguage bootstrap() {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        return tsl;
    }

}
