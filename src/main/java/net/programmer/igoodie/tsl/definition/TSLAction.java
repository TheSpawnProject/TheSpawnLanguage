package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.util.StringUtils;

import java.util.List;

public abstract class TSLAction extends TSLDefinition {

    public TSLAction(String name) {
        super(StringUtils.upperSnake(name));
    }

    public abstract void perform(List<TSLToken> tokens, TSLContext context);

}
