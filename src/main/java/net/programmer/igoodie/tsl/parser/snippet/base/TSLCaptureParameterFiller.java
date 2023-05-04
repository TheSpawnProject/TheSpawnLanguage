package net.programmer.igoodie.tsl.parser.snippet.base;

import net.programmer.igoodie.tsl.parser.token.base.TSLToken;

import java.util.Map;

public interface TSLCaptureParameterFiller<T> {

    T fillCaptureParameters(Map<String, TSLToken> arguments);

}
