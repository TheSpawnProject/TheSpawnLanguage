package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.legacy.plugin.TSLPluginManifest;

public class TSLPluginLoadingException extends RuntimeException {

    protected TSLPluginManifest manifest;
    protected String filePath;

    public TSLPluginLoadingException(String reason) {
        this(reason, null);
    }

    public TSLPluginLoadingException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public TSLPluginLoadingException withManifest(TSLPluginManifest manifest) {
        this.manifest = manifest;
        return this;
    }

    public TSLPluginLoadingException withFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

}
