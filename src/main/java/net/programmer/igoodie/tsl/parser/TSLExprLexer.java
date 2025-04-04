package net.programmer.igoodie.tsl.parser;

import java.io.IOException;

public class TSLExprLexer {

    protected final TSLLexerNew parent;

    protected StringBuilder script = new StringBuilder();

    // Opening-closing pairs
    // " | "
    // ' | '
    // { | }
    // ` | `
    // ${ | }

    public TSLExprLexer(TSLLexerNew parent) {
        this.parent = parent;
    }

    public TSLLexerNew.Token tokenizeExpression() throws IOException {
        if (parent.charStream.lookahead("${")) {
            this.script.append("${");
            parent.charStream.consume(2);

            while (parent.charStream.hasNext()) {
                if (parent.charStream.peek() == '"') {
                    this.skipString('"');
                    continue;
                }

                if (parent.charStream.peek() == '\'') {
                    this.skipString('\'');
                    continue;
                }

                this.script.append(parent.charStream.peek());
                parent.charStream.consume();
            }

            return parent.pushToken(TSLLexerNew.Token.Type.EXPR, script.toString());
        }

        return null;
    }


    protected void skipString(char quote) throws IOException {
        if (parent.charStream.peek() == quote) {
            this.script.append(quote);
            parent.charStream.consume();

            boolean isEscaping = false;

            while (parent.charStream.hasNext()) {
                if (isEscaping) {
                    isEscaping = false;
                }

                if (parent.charStream.peek() == '\\') {
                    isEscaping = true;
                }

                this.script.append(parent.charStream.peek());
                parent.charStream.consume();
            }
        }
    }

    protected String skipTemplateLiteral() {
        // TODO
        return null;
    }

}

// "..."
// '...'
// `...`
// `... ${ ... } ...`
// `... ${ ... "..." ... } ...`
// { ... }
