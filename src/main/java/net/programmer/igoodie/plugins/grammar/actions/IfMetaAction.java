package net.programmer.igoodie.plugins.grammar.actions;

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

import java.util.List;

public class IfMetaAction extends TSLAction {

    public static final IfMetaAction INSTANCE = new IfMetaAction();

    private IfMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "IF");
    }

    @Override
    public String getUsage() {
        return getName() + " <condition> THEN <action> (ELSE IF <condition> THEN <action>)* (ELSE THEN <action>)?";
    }

    @Override
    public void validateTokens(List<TSLToken> arguments, TSLRule rule, TSLParser parser) throws TSLSyntaxError {
        if (arguments.size() < 2) {
            throw new TSLSyntaxError("Expected condition and the action.", rule);
        }

        List<List<TSLToken>> parts = splitParts(arguments, true);

        for (int i = 0; i < parts.size(); i++) {
            List<TSLToken> part = parts.get(i);

            if (i == 0) { // IF part
                if (part.size() < 3) {
                    throw new TSLSyntaxError("Invalid IF statement", rule);
                }

                TSLToken keywordThen = part.get(1);
                if (!(keywordThen instanceof TSLString)) {
                    throw new TSLSyntaxError("THEN keyword MUST be a plain string.", keywordThen);
                }
                if (!keywordThen.getRaw().equalsIgnoreCase("THEN")) {
                    throw new TSLSyntaxError("IF statement must continue with a condition and THEN keyword.", keywordThen);
                }

                List<TSLToken> actionPart = part.subList(2, part.size());
                parser.parseAction(null, actionPart);

            } else if (i != part.size() - 1) { // ELSEIF part
                if (part.size() < 4) {
                    throw new TSLSyntaxError("Invalid ELSEIF statement", rule);
                }

                TSLToken keywordThen = part.get(2);
                if (!(keywordThen instanceof TSLString)) {
                    throw new TSLSyntaxError("THEN keyword MUST be a plain string.", keywordThen);
                }
                if (!keywordThen.getRaw().equalsIgnoreCase("THEN")) {
                    throw new TSLSyntaxError("ELSEIF statement must continue with a condition and THEN keyword.", keywordThen);
                }

                List<TSLToken> actionPart = part.subList(3, part.size());
                parser.parseAction(null, actionPart);

            } else if (part.get(0).getRaw().equalsIgnoreCase("ELSE")) { // ELSE part
                List<TSLToken> actionPart = part.subList(1, part.size());
                parser.parseAction(null, actionPart);
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
                    TSLActionSnippet action = parser.parseAction(null, statement.get(1));
                    action.getActionDefinition().perform(action.getActionArgTokens(), context);
                    break;
                }

            } else {
                TSLParser parser = new TSLParser(context);
                TSLActionSnippet action = parser.parseAction(null, part);
                action.getActionDefinition().perform(action.getActionArgTokens(), context);
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
