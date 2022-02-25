package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;

import java.util.List;

public abstract class TSLComparator extends TSLDefinition {

    public TSLComparator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public abstract boolean verifySyntax(List<TSLToken> rightHand)
            throws TSLSyntaxError;

    public abstract boolean satisfies(Object leftHand, List<String> rightHand)
            throws TSLRuntimeError;

}
