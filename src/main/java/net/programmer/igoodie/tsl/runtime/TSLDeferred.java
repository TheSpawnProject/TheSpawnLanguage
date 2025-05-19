package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TSLPlatform;

import java.util.Optional;

public interface TSLDeferred<T> {

    Optional<T> resolve(TSLPlatform platform);

}
