package net.programmer.igoodie.tsl.parser.snippet.base;

import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

public interface TSLSnippetEntry {

    TextPosition getBeginningPos();

    TextPosition getEndingPos();

    default boolean isToken() {
        return this instanceof TSLToken;
    }

}
