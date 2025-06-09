package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLException;

public interface TSLDeferred<T> {

    T resolve(TSLPlatform platform) throws TSLException;

}
