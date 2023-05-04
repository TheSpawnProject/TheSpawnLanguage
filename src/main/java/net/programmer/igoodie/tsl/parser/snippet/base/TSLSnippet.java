package net.programmer.igoodie.tsl.parser.snippet.base;

import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.parser.helper.Copyable;
import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.helper.ListBuilder;
import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public abstract class TSLSnippet implements
        Collection<Either<TSLToken, TSLSnippet>>,
        TSLCaptureParameterFiller<TSLSnippet>,
        Copyable<TSLSnippet> {

    protected List<Either<TSLToken, TSLSnippet>> snippetEntries;

    public TSLSnippet(List<Either<TSLToken, TSLSnippet>> entries) {
        if (entries.size() == 0) {
            throw new TSLInternalError("A snippet MUST have at least one entry.");
        }

        this.snippetEntries = entries;
    }

    public List<Either<TSLToken, TSLSnippet>> getSnippetEntries() {
        return snippetEntries;
    }

    public TextPosition getBeginningPos() {
        return snippetEntries.get(0).fold(
                TSLToken::getBeginningPos,
                TSLSnippet::getBeginningPos
        );
    }

    public TextPosition getEndingPos() {
        return snippetEntries.get(snippetEntries.size() - 1).fold(
                TSLToken::getBeginningPos,
                TSLSnippet::getBeginningPos
        );
    }

    /* ------------------------ */

    @Override
    public int size() {
        return snippetEntries.size();
    }

    @Override
    public boolean isEmpty() {
        return snippetEntries.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return snippetEntries.contains(o);
    }

    @NotNull
    @Override
    public Iterator<Either<TSLToken, TSLSnippet>> iterator() {
        return snippetEntries.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return snippetEntries.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return snippetEntries.toArray(a);
    }

    @Override
    public boolean add(Either<TSLToken, TSLSnippet> entry) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(snippetEntries).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Either<TSLToken, TSLSnippet>> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format("{type=%s, entries=%s}",
                getClass().getSimpleName(),
                getSnippetEntries());
    }

    /* ------------- */

    public List<Either<TSLToken, TSLSnippet>> fillCaptures(Map<String, TSLCaptureSnippet> captures) {
        return null; // TODO
    }

    @Override
    public TSLSnippet fillCaptureParameters(Map<String, TSLToken> arguments) {
        return null; // TODO
    }

    /* ------------- */

    public static class EntryListBuilder extends ListBuilder<Either<TSLToken, TSLSnippet>> {

        public EntryListBuilder(Supplier<List<Either<TSLToken, TSLSnippet>>> initializer) {
            super(initializer);
        }

    }

}
