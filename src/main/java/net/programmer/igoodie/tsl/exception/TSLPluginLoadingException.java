package net.programmer.igoodie.tsl.exception;

public class TSLPluginLoadingException extends RuntimeException {

    protected String filePath;

    public TSLPluginLoadingException(String reason) {
        this(reason, null, null);
    }

    public TSLPluginLoadingException(String reason, String filePath) {
        this(reason, null, filePath);
    }

    public TSLPluginLoadingException(String reason, Throwable cause, String filePath) {
        super(reason, cause);
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

}
