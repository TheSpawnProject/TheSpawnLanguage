package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TSLPlatform;

public interface TSLDeferred<T> {

    T resolve(TSLPlatform platform);

}
