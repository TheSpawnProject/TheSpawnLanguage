package net.programmer.igoodie.tsl.runtime.listener;

public interface TSLEventEmitter<L extends TSLEventListener> {

    void addListener(L listener);

    void removeListener(L listener);

}
