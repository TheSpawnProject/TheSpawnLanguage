package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippetEntry;

import java.util.List;

public class TSLUnparsedSnippet extends TSLSnippet {

    public TSLUnparsedSnippet(List<TSLSnippetEntry> entries) {
        super(entries);
    }

    public void pushEntry(TSLSnippetEntry entry) {
        this.snippetEntries.add(entry);
    }

}
