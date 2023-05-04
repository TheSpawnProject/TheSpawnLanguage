package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TSLCaptureSnippet extends TSLSnippet {

    protected TSLCaptureCall headerToken;
    protected TSLSymbol equalsSignToken;
    protected List<Either<TSLToken, TSLSnippet>> capturedEntries;

    public TSLCaptureSnippet(
            TSLCaptureCall headerToken,
            TSLSymbol equalsSignToken,
            List<Either<TSLToken, TSLSnippet>> capturedEntries
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

    public List<Either<TSLToken, TSLSnippet>> getCapturedEntries() {
        return capturedEntries;
    }

    /* -------------------------------- */

    public List<Either<TSLToken, TSLSnippet>> fillParameterCalls(Map<String, TSLToken> arguments) {
        return null; // TODO
    }

}
