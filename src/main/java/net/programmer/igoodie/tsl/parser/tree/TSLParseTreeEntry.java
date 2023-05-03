package net.programmer.igoodie.tsl.parser.tree;

import net.programmer.igoodie.tsl.parser.token.TSLToken;

public interface TSLParseTreeEntry {

    default boolean isToken() {
        return this instanceof TSLToken;
    }

}
