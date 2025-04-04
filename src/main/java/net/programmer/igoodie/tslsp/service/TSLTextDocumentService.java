package net.programmer.igoodie.tslsp.service;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.CharStream;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tslsp.TSLServer;
import net.programmer.igoodie.tslsp.capability.TSLSemanticTokenCapability;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.TextDocumentService;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TSLTextDocumentService implements TextDocumentService {

    protected final TSLServer server;

    // TODO: Abstract under a TSLDocument file
    protected Map<String, TextDocumentItem> openDocuments = new HashMap<>();
    protected Map<String, TSLRuleset> openRulesets = new HashMap<>();
    protected Map<TSLRuleset, List<TSLLexer.Token>> openTokens = new HashMap<>();
    protected Map<String, TSLSyntaxException> syntaxErrors = new HashMap<>();

    public TSLTextDocumentService(TSLServer server) {this.server = server;}

    protected void parseRuleset(String uri, String target, String script) {
        try {
            CharStream charStream = CharStream.fromString(script);
            TSLLexer lexer = new TSLLexer(charStream);
            List<TSLLexer.Token> tokens = lexer.tokenize();
            TSLParser parser = new TSLParser(this.server.getPlatform(), target, tokens);
            TSLRuleset ruleset = parser.parse();
            this.openRulesets.put(uri, ruleset);
            this.openTokens.put(ruleset, tokens);
            this.syntaxErrors.remove(uri);

        } catch (TSLSyntaxException e) {
            this.syntaxErrors.put(uri, e);

        } catch (Exception e) {
            // TODO: idk what to do
        }
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        TextDocumentItem textDocument = params.getTextDocument();
        if (!textDocument.getLanguageId().equals("tsl")) return;

        String uriRaw = textDocument.getUri();
        URI uri = URI.create(uriRaw);
        this.openDocuments.put(uriRaw, textDocument);

        this.parseRuleset(uriRaw, uri.getPath(), textDocument.getText());
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        TextDocumentIdentifier textDocumentId = params.getTextDocument();

        String uri = textDocumentId.getUri();

        this.openDocuments.remove(uri);
        this.openRulesets.remove(uri);
        this.syntaxErrors.remove(uri);
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        VersionedTextDocumentIdentifier textDocumentId = params.getTextDocument();
        List<TextDocumentContentChangeEvent> contentChanges = params.getContentChanges();

        String uriRaw = textDocumentId.getUri();
        if (!this.openDocuments.containsKey(uriRaw)) return;

        URI uri = URI.create(uriRaw);

        this.parseRuleset(uriRaw, uri.getPath(), contentChanges.get(0).getText());
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {}

    @Override
    public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
        TextDocumentIdentifier textDocumentId = params.getTextDocument();
        String uri = textDocumentId.getUri();

        TSLRuleset ruleset = this.openRulesets.get(uri);

        if (ruleset == null) {
            TSLSyntaxException syntaxException = this.syntaxErrors.get(uri);
            return CompletableFuture.completedFuture(null);
        }

        List<Integer> data = new ArrayList<>();

        List<TSLLexer.Token> tokens = this.openTokens.get(ruleset);
        System.out.println(tokens);

        for (TSLLexer.Token token : tokens) {
            data.add(token.lineNo);
            data.add(token.charNo);
            data.add(token.value.length()); // TODO: properly implement for TSLGroup tokens
            data.add(token.type == TSLLexer.TokenType.WORD
                    ? TSLSemanticTokenCapability.TokenType.VARIABLE.id
                    : TSLSemanticTokenCapability.TokenType.KEYWORD.id);
        }

        SemanticTokens semanticTokens = new SemanticTokens();
        semanticTokens.setData(data);
        return CompletableFuture.completedFuture(semanticTokens);
    }

//
//    @Override
//    public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams params) {
//        return TextDocumentService.super.onTypeFormatting(params);
//    }

}
