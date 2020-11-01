package net.programmer.igoodie.tsl.registry;

public interface ITSLRegistry<T> {

    void register(T entry);

    boolean has(T entry);

    boolean has(String name);

    T get(String name);

}
