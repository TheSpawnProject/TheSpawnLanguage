package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.base.TSLArguments;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.TSLParsingContext;
import net.programmer.igoodie.tsl.parser.snippet.TSLActionSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ForMetaAction extends TSLAction {

    public static final ForMetaAction INSTANCE = new ForMetaAction();

    private ForMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "FOR");
    }

    @Override
    public String getUsage() {
        return getName() + " <count> TIMES <action>";
    }

    @Override
    public @Nullable List<Couple<String, String>> getCompletionSnippets() {
        return Collections.singletonList(
                new Couple<>(getName(), getName() + " ${1:count} TIMES\n\t$0")
        );
    }

    @Override
    public @NotNull Couple<List<TSLToken>, TSLToken> splitByDisplaying(List<TSLToken> tokens) {
        return new Couple<>(tokens, null);
    }

    @Override
    public void validateTokens(TSLToken nameToken, ListAccessor<TSLToken> arguments, TSLParsingContext parsingContext) throws TSLSyntaxError {
        if (arguments.size() < 3) {
            throw new TSLSyntaxError("Expected loop count and the action", nameToken);
        }

        TSLToken timesKeyword = arguments.get(1).orElse(null);
        List<TSLToken> actionTokens = arguments.subList(2, arguments.size());

        if (!(timesKeyword instanceof TSLPlainWord)) {
            throw new TSLSyntaxError("TIMES keyword MUST be a plain string.", timesKeyword);
        }
        if (!timesKeyword.getRaw().equalsIgnoreCase("TIMES")) {
            throw new TSLSyntaxError("FOR statement must continue with a loop count and TIMES keyword.", timesKeyword);
        }

        // See if underlying action is parsable without errors
        parsingContext.getParser().parseAction(
                parsingContext.getPluginAliases(),
                parsingContext.getCaptureSnippets(),
                actionTokens);
    }

    @Override
    public void perform(ListAccessor<TSLToken> arguments, TSLContext context) {
        TSLToken countToken = arguments.get(0).orElse(null);
        List<TSLToken> actionTokens = arguments.subList(2, arguments.size());

        int count = TSLArguments.parseInt(countToken, context)
                .orElseThrow(() -> new TSLSyntaxError("Expected a number for count", countToken));

        for (int i = 0; i < count; i++) {
            TSLParser parser = new TSLParser(context);

            TSLActionSnippet actionSnippet = parser.parseAction(
                    context.getImportedPlugins(),
                    context.getCaptureSnippets(),
                    actionTokens);

            TSLAction actionDefinition = actionSnippet.getActionDefinition();
            actionDefinition.perform(ListAccessor.of(actionSnippet.getActionTokens()), context);
        }
    }

}
