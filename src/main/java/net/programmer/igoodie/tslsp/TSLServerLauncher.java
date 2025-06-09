package net.programmer.igoodie.tslsp;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class TSLServerLauncher {

    public static final TSLPlatform DEBUG_TSL;

    static {
        DEBUG_TSL = new TSLPlatform("The Spawn Language", 1.5f);
        DEBUG_TSL.OLD_initializeStd();
//        DEBUG_TSL.registerAction("PRINT", (platform, args) -> new TSLAction(platform, args) {
//            @Override
//            public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
//                System.out.println(">> " + args.stream()
//                        .map(Either::getLeft)
//                        .map(Optional::orElseThrow)
//                        .map(word -> word.evaluate(ctx))
//                        .collect(Collectors.joining(" ")));
//                return Collections.emptyList();
//            }
//        });
        DEBUG_TSL.registerEvent(new TSLEvent("Dummy Event")
                .addPropertyType(TSLEvent.Property.Builder.STRING.create("actor")));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        if (Arrays.asList(args).contains("--dev-server")) {
            try (ServerSocket serverSocket = new ServerSocket(3000)) {
                System.out.println("Waiting for connection @ " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted clientSocket: " + clientSocket);
                startServer(DEBUG_TSL, clientSocket.getInputStream(), clientSocket.getOutputStream());
                return;
            }
        }

        startServer(DEBUG_TSL, System.in, System.out);
    }

    public static void startServer(TSLPlatform platform, InputStream in, OutputStream out) throws ExecutionException, InterruptedException {
        TSLServer server = new TSLServer(platform);

        Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(server, in, out);
        LanguageClient remoteProxy = launcher.getRemoteProxy();
        server.connect(remoteProxy);

        launcher.startListening().get();
    }

}
