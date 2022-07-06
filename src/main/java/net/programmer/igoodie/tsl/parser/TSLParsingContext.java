package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TSLParsingContext {

    private final TSLParser parser;
    private final Map<String, String> pluginAliases;
    private final Map<String, TSLCaptureSnippet> captureSnippets;

    public TSLParsingContext(TSLParser parser, @Nullable Map<String, String> pluginAliases, @Nullable Map<String, TSLCaptureSnippet> captureSnippets) {
        this.parser = parser;
        this.pluginAliases = pluginAliases;
        this.captureSnippets = captureSnippets;
    }

    public TSLParser getParser() {
        return parser;
    }

    public Map<String, String> getPluginAliases() {
        return pluginAliases;
    }

    public Map<String, TSLCaptureSnippet> getCaptureSnippets() {
        return captureSnippets;
    }

}
