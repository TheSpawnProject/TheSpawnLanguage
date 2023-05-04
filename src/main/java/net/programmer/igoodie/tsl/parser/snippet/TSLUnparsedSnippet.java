package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.helper.Copyable;
import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;

import java.util.ArrayList;
import java.util.List;

// TODO: TSLSnippet<T <-- TSLUnparsedSnippet> for self type reference
public class TSLUnparsedSnippet extends TSLSnippet {

    public TSLUnparsedSnippet() {
        this(new ArrayList<>());
    }

    public TSLUnparsedSnippet(List<Either<TSLToken, TSLSnippet>> entries) {
        super(entries);
    }

    @Override
    public TSLUnparsedSnippet copy() {
        return new TSLUnparsedSnippet(Copyable.copyUnmodifiableList(snippetEntries));
    }

    public void pushToken(TSLToken token) {
        snippetEntries.add(Either.left(token));
    }

    public void pushSnippet(TSLSnippet snippet) {
        snippetEntries.add(Either.right(snippet));
    }

}
