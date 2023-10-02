package net.programmer.igoodie.legacy.parser.snippet;

import net.programmer.igoodie.legacy.parser.token.TSLToken;
import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.util.TSLCollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Deprecated
public abstract class TSLSnippet implements Collection<TSLToken> {

    protected List<TSLToken> allTokens;

    public TSLSnippet(List<TSLToken> allTokens) {
        if (allTokens.size() <= 0) {
            throw new TSLInternalError("A Snippet MUST have at least one token.");
        }

        this.allTokens = allTokens;
    }

    public List<TSLToken> getAllTokens() {
        return allTokens;
    }

    public int getBeginningLine() {
        return allTokens.get(0).getLine();
    }

    public int getEndingLine() {
        return allTokens.get(allTokens.size() - 1).getLine();
    }

    @Override
    public int size() {
        return allTokens.size();
    }

    @Override
    public boolean isEmpty() {
        return allTokens.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return allTokens.contains(o);
    }

    @NotNull
    @Override
    public Iterator<TSLToken> iterator() {
        return allTokens.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return allTokens.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return allTokens.toArray(a);
    }

    @Override
    public boolean add(TSLToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(allTokens).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends TSLToken> c) {
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
        return String.format("{type=%s, tokens=%s}",
                getClass().getSimpleName(),
                getAllTokens());
    }

    protected static List<TSLToken> flatTokens(Object... tokens) {
        return TSLCollectionUtils.flatMergeAll(TSLToken.class, tokens);
    }

}
