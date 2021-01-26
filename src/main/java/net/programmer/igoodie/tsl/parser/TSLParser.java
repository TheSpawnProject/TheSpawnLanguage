package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
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

        for (TSLSnippet snippet : lexer.getSnippets()) {
            System.out.println(snippet);

            if (snippet.getType() == TSLSnippet.Type.TAG) {
                parseTag(ruleset, snippet);
            }
        }

        return ruleset;
    }

    private void parseTag(TSLRuleset ruleset, TSLSnippet snippet) {
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

        ruleset.addTag(tagDefinition,
                ((TSLString) tagNameToken),
                tokens.subList(2, tokens.size()).stream()
                        .map(t -> ((TSLString) t))
                        .collect(Collectors.toList()));
    }

}
