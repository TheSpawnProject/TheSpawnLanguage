package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.tsl.exception.TSLInternalError;
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
            Couple<TSLLexer, TSLLexerState> result = state.copyLexerSet();
            TSLLexer paramLexer = result.getFirst();
            TSLLexerState paramState = result.getSecond().withCommaDelimiterAllowed();

            paramLexer.lexUntil((l, s) -> {
                if (s.getCharacterByOffset(0) == ')'
                        && s.getModeDepth() == 1
                        && s.getNestDepth() == 2) {
                    s.popSnippet();
                    s.skipCharacters(")");
                    return true;
                }
                return false;
            });

            state.moveScanningPosTo(paramState);
            System.out.println(paramState.constructTextRange());
            System.out.println("Moved pos range: " + state.constructTextRange());

            List<Either<TSLToken, TSLSnippet<?>>> paramTokens = paramLexer.getSnippets()
                    .get(0).getSnippetEntries()
                    .get(0).right().orElseThrow(() -> new TSLInternalError(""))
                    .getSnippetEntries();

            TextRange range = state.constructTextRange();

            if (paramTokens.stream().anyMatch(Either::isRight)) {
                throw new TSLSyntaxError("Capture call cannot accept nested snippets")
                        .at(range);
            }

            state.pushToken(raw -> {
                String captureName = StringUtilities.shrink(raw, 1, 0);

                List<TSLToken> parameters = paramTokens
                        .stream()
                        .map(e -> e.left())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());

                System.out.println("Pushing CaptureCall token @ " + range);

                return new TSLCaptureCall(range, captureName, parameters);
            });
            state.popMode();
            return CONTINUE;
        }

        if (Character.isSpaceChar(character)
                || (state.doesAllowCommaDelimiter() && character == ',')) {
            state.pushToken();
            state.popMode();
            return CONTINUE;
        }

        state.pushChars(character);
        return CONTINUE;
    }

}
