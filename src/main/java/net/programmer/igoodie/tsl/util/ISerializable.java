package net.programmer.igoodie.tsl.util;

public interface ISerializable<T> {

    T serialize();

    void deserialize(T from);

}
