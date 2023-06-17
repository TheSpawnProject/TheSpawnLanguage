package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;

public class TSLTokenizer {

    public static TSLToken tokenize(TextRange range, String raw) {
        // TODO

        TSLSymbol.Type symbolType = TSLSymbol.Type.bySymbol(raw);
        if (symbolType != null) return new TSLSymbol(range, symbolType);

        return new TSLPlainWord(range, raw);
    }

}
