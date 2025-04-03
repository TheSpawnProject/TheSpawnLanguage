package net.programmer.igoodie.tslsp;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tslsp.capability.TSLSemanticTokenCapability;
import net.programmer.igoodie.tslsp.service.TSLTextDocumentService;
import net.programmer.igoodie.tslsp.service.TSLWorkspaceService;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import java.util.concurrent.CompletableFuture;

public class TSLServer implements LanguageServer, LanguageClientAware {

    protected final TSLPlatform platform;

    protected LanguageClient client;

    protected TSLTextDocumentService textDocumentService = new TSLTextDocumentService(this);
    protected TSLWorkspaceService workspaceService = new TSLWorkspaceService();

    public TSLServer(TSLPlatform platform) {this.platform = platform;}

    public TSLPlatform getPlatform() {
        return platform;
    }

    @Override
    public void connect(LanguageClient languageClient) {
        this.client = languageClient;
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams initializeParams) {
        ServerInfo serverInfo = new ServerInfo("TSL", "1.5.0");

        ServerCapabilities capabilities = new ServerCapabilities();
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
        capabilities.setSemanticTokensProvider(new TSLSemanticTokenCapability().buildOptions());

        InitializeResult result = new InitializeResult();
        result.setServerInfo(serverInfo);
        result.setCapabilities(capabilities);

        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public void setTrace(SetTraceParams params) {
        // TODO
    }

    @Override
    public void cancelProgress(WorkDoneProgressCancelParams params) {
        // TODO
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return this.textDocumentService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return this.workspaceService;
    }

}
