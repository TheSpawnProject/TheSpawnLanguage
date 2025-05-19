package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TSLDoc extends TSLWord {

    protected final String doc;

    public TSLDoc(String doc) {
        String[] lines = doc.replaceFirst("^#\\*\\*", "")
                .replaceFirst("\\*#$", "")
                .split("\\r?\\n");

        this.doc = Arrays.stream(lines)
                .map(line -> line.replaceFirst("^\\s*\\*", ""))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.joining(" "));
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        throw new IllegalStateException("TSLDocs aren't meant for evaluation.");
    }

}
