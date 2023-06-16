package net.programmer.igoodie.tsl.parser.snippet.base;

import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.parser.helper.Copyable;
import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.helper.ListBuilder;
import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureParameter;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.util.ValuePipe;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class TSLSnippet<S extends TSLSnippet<S>> implements
        Collection<Either<TSLToken, TSLSnippet<?>>>,
        TSLCaptureParameterFiller<S>,
        Copyable<S> {

    protected List<Either<TSLToken, TSLSnippet<?>>> snippetEntries;

    public TSLSnippet(List<Either<TSLToken, TSLSnippet<?>>> entries) {
        if (entries.size() == 0 && !(this instanceof TSLUnparsedSnippet)) {
            throw new TSLInternalError("A snippet MUST have at least one entry.");
        }

        this.snippetEntries = entries;
    }

    protected final S getSelf() {
        @SuppressWarnings("unchecked")
        S self = (S) this;
        return self;
    }

    public List<Either<TSLToken, TSLSnippet<?>>> getSnippetEntries() {
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
    public Iterator<Either<TSLToken, TSLSnippet<?>>> iterator() {
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
    public boolean add(Either<TSLToken, TSLSnippet<?>> entry) {
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
    public boolean addAll(@NotNull Collection<? extends Either<TSLToken, TSLSnippet<?>>> c) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSLSnippet<?> that = (TSLSnippet<?>) o;
        return Objects.equals(this.snippetEntries, that.snippetEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snippetEntries);
    }

    @Override
    public String toString() {
        return String.format("{type=%s, entries=%s}",
                getClass().getSimpleName(),
                getSnippetEntries());
    }

    /* ------------- */

    public S fillCaptureCalls(Map<String, TSLCaptureSnippet> captures) {
        S filledSnippet = this.copy();

        List<Either<TSLToken, TSLSnippet<?>>> entries = new ArrayList<>();

        filledSnippet.snippetEntries.forEach(entry -> entry.consume(
                token -> {
                    if (!(token instanceof TSLCaptureCall)) {
                        entries.add(Either.left(token));
                        return;
                    }

                    TSLCaptureCall captureCall = (TSLCaptureCall) token;
                    TSLCaptureSnippet capture = captures.get(captureCall.getCaptureName());
                    if (capture == null) {
                        entries.add(Either.left(token));
                        return;
                    }

                    entries.addAll(capture.fillCaptureParameters(captureCall)
                            .fillCaptureCalls(captures).snippetEntries);
                },

                snippet -> ValuePipe.of(snippet)
                        .map(s -> s.fillCaptureCalls(captures))
                        .map(Either::<TSLToken, TSLSnippet<?>>right)
                        .consume(entries::add)
        ));

        filledSnippet.snippetEntries = entries;

        return filledSnippet;
    }

    @Override
    public S fillCaptureParameters(Map<String, TSLToken> arguments) {
        S filledSnippet = this.copy();

        filledSnippet.snippetEntries = filledSnippet.snippetEntries.stream()
                .map(entry -> entry.fold(
                        token -> Optional.of(token)
                                .filter(t -> t instanceof TSLCaptureParameter)
                                .map(t -> ((TSLCaptureParameter) t))
                                .map(t -> arguments.get(t.getParameterName()))
                                .map(Either::<TSLToken, TSLSnippet<?>>left)
                                .orElse(entry),

                        snippet -> Optional.of(snippet)
                                .map(s -> s.fillCaptureParameters(arguments))
                                .map(Either::<TSLToken, TSLSnippet<?>>right)
                                .orElse(entry)
                ))
                .collect(Collectors.toList());

        return filledSnippet;
    }

    /* ------------- */

    public static class EntryListBuilder extends ListBuilder<Either<TSLToken, TSLSnippet<?>>> {

        public EntryListBuilder(Supplier<List<Either<TSLToken, TSLSnippet<?>>>> initializer) {
            super(initializer);
        }

    }

}
