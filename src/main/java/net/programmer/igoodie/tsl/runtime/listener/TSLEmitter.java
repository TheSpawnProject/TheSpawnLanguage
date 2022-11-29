package net.programmer.igoodie.tsl.runtime.listener;

public interface TSLEmitter<L extends TSLListener> {

    void addListener(L listener);

    void removeListener(L listener);

}
