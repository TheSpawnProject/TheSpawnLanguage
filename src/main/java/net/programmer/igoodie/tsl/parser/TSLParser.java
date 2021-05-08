package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.snippet.TSLActionSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.List;
import java.util.stream.Collectors;

public class TSLParser {

    private TheSpawnLanguage tsl;

    public TSLParser(TheSpawnLanguage tsl) {
        this.tsl = tsl;
    }

    public TSLRuleset parse(String script) {
        TSLRuleset ruleset = new TSLRuleset();

        TSLLexer lexer = new TSLLexer(script);
        lexer.lex();

        for (TSLSnippetBuffer snippet : lexer.getSnippets()) {
            if (snippet.getType() == TSLSnippetBuffer.Type.TAG) {
                TSLTagSnippet tslTagSnippet = parseTag(ruleset, snippet);
                ruleset.addTag(tslTagSnippet.getTag(), tslTagSnippet.getTagName(), tslTagSnippet.getTagArguments());

            } else if (snippet.getType() == TSLSnippetBuffer.Type.CAPTURE) {
                TSLCaptureSnippet tslCaptureSnippet = parseCapture(ruleset, snippet);
                ruleset.getCaptures().put(tslCaptureSnippet.getName(), tslCaptureSnippet);

            } else if (snippet.getType() == TSLSnippetBuffer.Type.RULE) {
                TSLActionSnippet tslActionSnippet = parseAction(ruleset, snippet);
                // TODO: implement stuff xd
//                System.out.println(tslActionSnippet.flatten());
            }
        }

        return ruleset;
    }

    private TSLTagSnippet parseTag(TSLRuleset ruleset, TSLSnippetBuffer snippet) {
        List<TSLToken> tokens = snippet.getTokens();

        if (tokens.size() < 2) {
            throw new TSLSyntaxError("Malformed tag snippet", snippet);
        }

        TSLToken tagNameToken = tokens.get(1);
        TSLTag tagDefinition = tsl.TAG_REGISTRY.get(tagNameToken.getRaw());

        if (tagDefinition == null) {
            throw new TSLSyntaxError(String.format("Unknown tag name -> %s", tagNameToken.getRaw()), snippet);
        }

        if (!(tagNameToken instanceof TSLString)) {
            throw new TSLSyntaxError(String.format("Invalid value -> %s", tagNameToken.getRaw()), snippet);
        }

        List<TSLToken> argTokens = tokens.subList(1, tokens.size());

        for (TSLToken argToken : argTokens) {
            if (!(argToken instanceof TSLString)) {
                throw new TSLSyntaxError(String.format("Invalid value -> %s", argToken.getRaw()), snippet);
            }
        }

        return new TSLTagSnippet(ruleset,
                tagDefinition,
                ((TSLSymbol) tokens.get(0)),
                ((TSLString) tagNameToken),
                tokens.subList(2, tokens.size()).stream()
                        .map(token -> ((TSLString) token))
                        .collect(Collectors.toList()));
    }

    public TSLCaptureSnippet parseCapture(TSLRuleset ruleset, TSLSnippetBuffer snippet) {
        List<TSLToken> tokens = snippet.getTokens();

        if (tokens.size() < 3) {
            throw new TSLSyntaxError("Malformed capture snippet", snippet);
        }

        TSLToken captureNameToken = tokens.get(0);

        if (!(captureNameToken instanceof TSLCaptureCall)) {
            throw new TSLSyntaxError(String.format("Invalid capture header -> %s", captureNameToken.getRaw()), snippet);
        }

        TSLToken equalsSign = tokens.get(1);

        if (!(equalsSign instanceof TSLSymbol) || ((TSLSymbol) equalsSign).getType() != TSLSymbol.Type.CAPTURE_DECLARATION) {
            throw new TSLSyntaxError(String.format("Unexpected token -> %s", equalsSign.getRaw()), snippet);
        }
        List<TSLToken> capturedTokens = tokens.subList(2, tokens.size());

        return new TSLCaptureSnippet(ruleset,
                ((TSLCaptureCall) captureNameToken),
                ((TSLSymbol) equalsSign),
                capturedTokens);
    }

    public TSLActionSnippet parseAction(TSLRuleset ruleset, TSLSnippetBuffer snippet) {
        // TODO: refactor. Here for testing purposes
        return new TSLActionSnippet(ruleset, snippet.getTokens());
    }

}
