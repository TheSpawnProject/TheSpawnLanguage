package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LexerModeCaptureCall extends LexerMode {

    @Override
    public int process(TSLLexerState state) {
        char character = state.getCharacterByOffset(0);

        if (character == '(') {
            state.pushChars('(');
            state.skipCharacters("(");

            Couple<TSLLexer, TSLLexerState> result = state.copyLexerSet();
            TSLLexer paramLexer = result.getFirst();
            TSLLexerState paramState = result.getSecond().withCommaDelimiterAllowed();

            System.out.println("Starting sub " + paramState.hashCode());

            paramLexer.lexUntil(s -> s.getModeDepth() == 1
                    && s.getCharacterByOffset(0) == ')');
            paramState.pushChars(")");

            state.moveScanningPosTo(paramState);

            List<Either<TSLToken, TSLSnippet<?>>> paramTokens = paramLexer.getSnippets().get(0).getSnippetEntries();

            TextRange range = state.constructTextRange();

            if (paramTokens.stream().anyMatch(Either::isRight)) {
                throw new TSLSyntaxError("Capture call cannot accept nested snippets")
                        .at(range);
            }

            state.pushToken(raw -> {
                String captureName = StringUtilities.shrink(raw, 1, 1);

                List<TSLToken> parameters = paramTokens
                        .stream()
                        .map(e -> e.left())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());

                return new TSLCaptureCall(range, captureName, parameters);
            });
            state.popMode();
            return CONTINUE;
        }

        if (Character.isSpaceChar(character)) {
            state.pushToken();
            state.popMode();
            return CONTINUE;
        }

        state.pushChars(character);
        return CONTINUE;
    }

}
