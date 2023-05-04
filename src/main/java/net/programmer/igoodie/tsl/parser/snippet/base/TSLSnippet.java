package net.programmer.igoodie.tsl.parser.snippet.base;

import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class TSLSnippet implements Collection<TSLSnippetEntry> {

    protected List<TSLSnippetEntry> snippetEntries;

    public TSLSnippet(List<TSLSnippetEntry> entries) {
        if (entries.size() == 0) {
            throw new TSLInternalError("A snippet MUST have at least one entry.");
        }

        this.snippetEntries = entries;
    }

    public List<TSLSnippetEntry> getSnippetEntries() {
        return snippetEntries;
    }

    public TextPosition getBeginningPos() {
        return snippetEntries.get(0).getBeginningPos();
    }

    public TextPosition getEndingPos() {
        return snippetEntries.get(snippetEntries.size() - 1).getEndingPos();
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
    public Iterator<TSLSnippetEntry> iterator() {
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
    public boolean add(TSLSnippetEntry entry) {
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
    public boolean addAll(@NotNull Collection<? extends TSLSnippetEntry> c) {
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

}
