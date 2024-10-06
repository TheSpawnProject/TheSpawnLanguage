package net.programmer.igoodie.runtime.event;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.StringUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TSLEvent {

    protected final String eventName;
    protected Map<String, Property<?>> propertyTypes;

    public TSLEvent(String eventName) {
        this.eventName = StringUtilities.upperFirstLetters(eventName);
        this.propertyTypes = new HashMap<>();
    }

    public String getEventName() {
        return eventName;
    }

    public Property<?> getPropertyType(String fieldName) {
        return propertyTypes.get(fieldName);
    }

    public TSLEvent addPropertyType(Property<?> propertyType) {
        this.propertyTypes.put(propertyType.propertyName, propertyType);
        return this;
    }

    public static class Property<T> {

        @FunctionalInterface
        public interface Reader<T> {
            Optional<T> read(GoodieObject eventArgs, String propertyName);
        }

        @FunctionalInterface
        public interface Writer<T> {
            void write(GoodieObject eventArgs, String propertyName, T value);
        }

        protected final String propertyName;
        protected final Reader<T> reader;
        protected final Writer<T> writer;

        public Property(String propertyName, Reader<T> reader, Writer<T> writer) {
            this.propertyName = propertyName;
            this.reader = reader;
            this.writer = writer;
        }

        public Optional<T> read(GoodieObject eventArgs) {
            return this.reader.read(eventArgs, propertyName);
        }

        public T write(GoodieObject eventArgs, T value) {
            this.writer.write(eventArgs, propertyName, value);
            return value;
        }
    }

    public static class PropertyBuilder<T> {

        protected final Property.Reader<T> reader;
        protected final Property.Writer<T> writer;

        public PropertyBuilder(Property.Reader<T> reader, Property.Writer<T> writer) {
            this.reader = reader;
            this.writer = writer;
        }

        public Property<T> create(String propertyName) {
            return new Property<>(propertyName, this.reader, this.writer);
        }

        public static final PropertyBuilder<Boolean> BOOLEAN = new PropertyBuilder<>(GoodieObject::getBoolean, GoodieObject::put);
        public static final PropertyBuilder<String> STRING = new PropertyBuilder<>(GoodieObject::getString, GoodieObject::put);
        public static final PropertyBuilder<Character> CHAR = new PropertyBuilder<>(GoodieObject::getCharacter, GoodieObject::put);
        public static final PropertyBuilder<Byte> BYTE = new PropertyBuilder<>(GoodieObject::getByte, GoodieObject::put);
        public static final PropertyBuilder<Short> SHORT = new PropertyBuilder<>(GoodieObject::getShort, GoodieObject::put);
        public static final PropertyBuilder<Integer> INT = new PropertyBuilder<>(GoodieObject::getInteger, GoodieObject::put);
        public static final PropertyBuilder<Long> LONG = new PropertyBuilder<>(GoodieObject::getLong, GoodieObject::put);
        public static final PropertyBuilder<Float> FLOAT = new PropertyBuilder<>(GoodieObject::getFloat, GoodieObject::put);
        public static final PropertyBuilder<Double> DOUBLE = new PropertyBuilder<>(GoodieObject::getDouble, GoodieObject::put);

    }

}
