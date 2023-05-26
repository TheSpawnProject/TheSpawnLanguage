package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.helper.Copyable;
import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSLCaptureSnippet extends TSLSnippet<TSLCaptureSnippet> {

    protected TSLCaptureCall headerToken;
    protected TSLSymbol equalsSignToken;
    protected List<Either<TSLToken, TSLSnippet<?>>> capturedEntries;

    public TSLCaptureSnippet(
            TSLCaptureCall headerToken,
            TSLSymbol equalsSignToken,
            List<Either<TSLToken, TSLSnippet<?>>> capturedEntries
    ) {
        super(new EntryListBuilder(ArrayList::new)
                .addElement(Either.left(headerToken))
                .addElement(Either.left(equalsSignToken))
                .addElements(capturedEntries)
                .build());
        this.headerToken = headerToken;
        this.equalsSignToken = equalsSignToken;
        this.capturedEntries = capturedEntries;
    }

    public TSLCaptureCall getHeaderToken() {
        return headerToken;
    }

    public TSLSymbol getEqualsSignToken() {
        return equalsSignToken;
    }

    public List<Either<TSLToken, TSLSnippet<?>>> getCapturedEntries() {
        return capturedEntries;
    }

    @Override
    public TSLCaptureSnippet copy() {
        return new TSLCaptureSnippet(
                headerToken.copy(),
                equalsSignToken.copy(),
                Copyable.copyUnmodifiableList(capturedEntries));
    }

    /* -------------------------------- */

    public TSLCaptureSnippet fillCaptureParameters(TSLCaptureCall caller) {
        return super.fillCaptureParameters(this.argumentsMapFromCaller(caller));
    }

    public Map<String, TSLToken> argumentsMapFromCaller(TSLCaptureCall caller) {
        if (caller.getArguments().size() < headerToken.getArguments().size()) {
            throw new TSLRuntimeError("Insufficient amount of parameters while calling capture"); // TODO: Add caller as associated token
        }

        Map<String, TSLToken> arguments = new HashMap<>();
        List<TSLToken> headerTokenArguments = headerToken.getArguments();
        for (int i = 0; i < headerTokenArguments.size(); i++) {
            TSLToken parameterName = headerTokenArguments.get(i);
            TSLToken argument = caller.getArguments().get(i);
            arguments.put(parameterName.getRaw(), argument);
        }
        return arguments;
    }

}
