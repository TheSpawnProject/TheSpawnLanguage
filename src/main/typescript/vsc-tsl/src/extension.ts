import { connect } from "node:net";
import * as vscode from "vscode";
import {
  LanguageClient,
  LanguageClientOptions,
  ServerOptions,
} from "vscode-languageclient/node";

let client: LanguageClient;

export function activate(context: vscode.ExtensionContext) {
  console.log("Activation on TSL");

  const serverOptions: ServerOptions = async () => {
    const socket = connect({
      host: "0.0.0.0",
      port: 3000,
    });

    return {
      reader: socket,
      writer: socket,
    };
  };

  const clientOptions: LanguageClientOptions = {
    documentSelector: [{ scheme: "file", language: "tsl" }],
  };

  client = new LanguageClient(
    "tsl",
    "TSL Language Server",
    serverOptions,
    clientOptions
  );

  client.start();
}

export function deactivate(): Thenable<void> | undefined {
  return client ? client.stop() : undefined;
}
