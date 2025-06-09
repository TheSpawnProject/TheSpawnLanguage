package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TSLDoc extends TSLWord {

    protected final String content;

    public TSLDoc(String content) {
        String[] lines = content.replaceFirst("^#\\*\\*", "")
                .replaceFirst("\\*#$", "")
                .split("\\r?\\n");

        this.content = Arrays.stream(lines)
                .map(line -> line.replaceFirst("^\\s*\\*\\s?", ""))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public String getContent() {
        return content;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        throw new IllegalStateException("TSLDocs aren't meant for evaluation.");
    }

}
