package net.programmer.igoodie.legacy.parser.token;

import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TSLGroup extends TSLToken {

    protected List<TSLToken> groupTokens;

    public TSLGroup(int line, int character, List<TSLToken> innerTokens) {
        super(line, character);
        this.groupTokens = innerTokens;
    }

    public List<TSLToken> getGroupTokens() {
        return Collections.unmodifiableList(groupTokens);
    }

    @Override
    public String getTypeName() {
        return "Group";
    }

    @Override
    public String getRaw() {
        return "%" + groupTokens.stream().map(TSLToken::getRaw).collect(Collectors.joining(" ")) + "%";
    }

    @Override
    public String evaluate(TSLContext context) {
        try {
            // TODO: BUG -> SUMMON skeleton ~ ~ ~ %{CustomName:"\"${actor}'s Knight\""}%
            return groupTokens.stream()
                    .map(token -> token.evaluate(context))
                    .collect(Collectors.joining(" "));

        } catch (Exception e) {
            throw new TSLRuntimeError("Unable to evaluate group", this).causedBy(e);
        }
    }

}
