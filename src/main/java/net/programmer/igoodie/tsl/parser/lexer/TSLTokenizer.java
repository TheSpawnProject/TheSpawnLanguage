package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.token.TSLExpression;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;

public class TSLTokenizer {

    public static TSLToken tokenize(TextRange range, String raw) {
        // TODO

        TSLSymbol.Type symbolType = TSLSymbol.Type.bySymbol(raw);
        if (symbolType != null) {
            return new TSLSymbol(range, symbolType);
        }

        if (raw.startsWith("${") && raw.endsWith("}")) {
            String expression = StringUtilities.shrink(raw, 2, 1);
            return new TSLExpression(range, expression);
        }

        return new TSLPlainWord(range, raw);
    }

}
