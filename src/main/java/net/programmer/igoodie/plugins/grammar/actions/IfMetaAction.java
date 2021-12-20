package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.snippet.TSLActionSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IfMetaAction extends TSLAction {

    public static final IfMetaAction INSTANCE = new IfMetaAction();

    private IfMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "IF");
    }

    @Override
    public String getUsage() {
        return getName() + " <condition> THEN <action> (ELSEIF <condition> THEN <action>)* (ELSE <action>)?";
    }

    @Override
    public @NotNull Couple<List<TSLToken>, TSLToken> splitByDisplaying(List<TSLToken> tokens) {
        return new Couple<>(tokens, null); // Does not support DISPLAYING statement
    }

    @Override
    public void validateTokens(TSLToken nameToken, List<TSLToken> arguments, TSLRule rule, TSLParser parser) throws TSLSyntaxError {
        if (arguments.size() < 3) {
            throw new TSLSyntaxError("Expected condition and the action.", nameToken);
        }

        List<List<TSLToken>> parts = splitParts(arguments, true);

        for (int i = 0; i < parts.size(); i++) {
            List<TSLToken> part = parts.get(i);

            if (i == 0) { // IF part
                if (part.size() < 3) {
                    throw new TSLSyntaxError("Invalid IF statement", nameToken);
                }

                TSLToken thenKeyword = part.get(1);
                List<TSLToken> actionTokens = part.subList(2, part.size());

                if (!(thenKeyword instanceof TSLString)) {
                    throw new TSLSyntaxError("THEN keyword MUST be a plain string.", thenKeyword);
                }
                if (!thenKeyword.getRaw().equalsIgnoreCase("THEN")) {
                    throw new TSLSyntaxError("IF statement must continue with a condition and THEN keyword.", thenKeyword);
                }
                parser.parseAction(null, actionTokens);

            } else if (i != parts.size() - 1) { // ELSEIF part
                if (part.size() < 4) {
                    throw new TSLSyntaxError("Invalid ELSEIF statement", nameToken);
                }

                TSLToken thenKeyword = part.get(2);
                List<TSLToken> actionTokens = part.subList(3, part.size());

                if (!(thenKeyword instanceof TSLString)) {
                    throw new TSLSyntaxError("THEN keyword MUST be a plain string.", thenKeyword);
                }
                if (!thenKeyword.getRaw().equalsIgnoreCase("THEN")) {
                    throw new TSLSyntaxError("ELSEIF statement must continue with a condition and THEN keyword.", thenKeyword);
                }
                parser.parseAction(null, actionTokens);

            } else if (part.get(0).getRaw().equalsIgnoreCase("ELSE")) { // ELSE part
                List<TSLToken> actionTokens = part.subList(1, part.size());
                parser.parseAction(null, actionTokens);
            }
        }
    }

    @Override
    public void perform(List<TSLToken> arguments, TSLContext context) {
        List<List<TSLToken>> parts = splitParts(arguments, false);

        for (List<TSLToken> part : parts) {
            List<List<TSLToken>> statement = CollectionUtils.splitBy(part, false, token -> token instanceof TSLString
                    && token.getRaw().equalsIgnoreCase("THEN"));

            if (statement.size() == 2) {
                TSLToken condition = statement.get(0).get(0);
                if (condition.isTrue(context)) {
                    TSLParser parser = new TSLParser(context);
                    TSLActionSnippet actionSnippet = parser.parseAction(null, statement.get(1));
                    TSLAction actionDefinition = actionSnippet.getActionDefinition();
                    actionDefinition.performRaw(actionSnippet.getActionTokens(), context);
                    break;
                }

            } else {
                TSLParser parser = new TSLParser(context);
                TSLActionSnippet actionSnippet = parser.parseAction(null, part);
                TSLAction actionDefinition = actionSnippet.getActionDefinition();
                actionDefinition.performRaw(actionSnippet.getActionTokens(), context);
                break;
            }
        }
    }

    private List<List<TSLToken>> splitParts(List<TSLToken> arguments, boolean includeDelimiter) {
        return CollectionUtils.splitBy(arguments, includeDelimiter, token -> {
            if (!(token instanceof TSLString)) return false;
            return token.getRaw().equalsIgnoreCase("ELSEIF")
                    || token.getRaw().equalsIgnoreCase("ELSE");
        });
    }

}
