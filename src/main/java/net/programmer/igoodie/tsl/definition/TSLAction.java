package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.legacy.parser.TSLParserOld;
import net.programmer.igoodie.legacy.runtime.TSLRuleOld;
import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class TSLAction extends TSLDefinition {

    public TSLAction(TSLPlugin plugin, String name) {
        super(plugin, StringUtilities.upperSnake(name));
    }

    public abstract String getUsage();

    public abstract void validateTokens(TSLToken nameToken, List<TSLToken> arguments, @Nullable TSLRuleOld rule, TSLParserOld parser) throws TSLSyntaxError;

    public @NotNull Couple<List<TSLToken>, TSLToken> splitByDisplaying(List<TSLToken> tokens) {
        int displayingIndex = CollectionUtils.lastIndexOfBy(tokens,
                token -> token instanceof TSLPlainWord && token.getRaw().equalsIgnoreCase("DISPLAYING"));

        if (displayingIndex != -1 && displayingIndex == tokens.size() - 2) {
            List<TSLToken> actionArgs = tokens.subList(0, displayingIndex);
            TSLToken messageToken = tokens.get(displayingIndex + 1);
            return new Couple<>(actionArgs, messageToken);
        }

        return new Couple<>(tokens, null);
    }

    public abstract void perform(List<TSLToken> arguments, TSLContext context);

    public final void performRaw(List<TSLToken> tokens, TSLContext context) {
        Couple<List<TSLToken>, TSLToken> couple = splitByDisplaying(tokens);
        List<TSLToken> actionArgs = couple.getFirst();
        TSLToken messageToken = couple.getSecond();

        if (messageToken != null) {
            context.setMessageToken(messageToken);
        }

        perform(actionArgs, context);
    }

}
