package net.programmer.igoodie.runtime.event;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.StringUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TSLEvent {

    protected final String eventName;
    protected List<PropertyType<?>> propertyTypes;

    public TSLEvent(String eventName) {
        this.eventName = StringUtilities.upperFirstLetters(eventName);
        this.propertyTypes = new ArrayList<>();
    }

    public String getEventName() {
        return eventName;
    }

    public TSLEvent addPropertyType(PropertyType<?> propertyType) {
        this.propertyTypes.add(propertyType);
        return this;
    }

    public static class PropertyType<T> {

        @FunctionalInterface
        public interface PropertyReader<T> {
            Optional<T> read(GoodieObject eventArgs, String propertyName);
        }

        @FunctionalInterface
        public interface PropertyWriter<T> {
            void write(GoodieObject eventArgs, String propertyName, T value);
        }

        protected final PropertyReader<T> reader;
        protected final PropertyWriter<T> writer;

        private PropertyType(PropertyReader<T> reader, PropertyWriter<T> writer) {
            this.reader = reader;
            this.writer = writer;
        }

        public Property<T> create(String propertyName) {
            return new Property<>(propertyName, this.reader, this.writer);
        }

        public static class Property<T> extends PropertyType<T> {

            protected final String propertyName;

            public Property(String propertyName, PropertyReader<T> reader, PropertyWriter<T> writer) {
                super(reader, writer);
                this.propertyName = propertyName;
            }

            public Optional<T> read(GoodieObject eventArgs) {
                return this.reader.read(eventArgs, propertyName);
            }
        }

        public static final PropertyType<Boolean> BOOLEAN = new PropertyType<>(GoodieObject::getBoolean, GoodieObject::put);
        public static final PropertyType<String> STRING = new PropertyType<>(GoodieObject::getString, GoodieObject::put);
        public static final PropertyType<Character> CHAR = new PropertyType<>(GoodieObject::getCharacter, GoodieObject::put);
        public static final PropertyType<Byte> BYTE = new PropertyType<>(GoodieObject::getByte, GoodieObject::put);
        public static final PropertyType<Short> SHORT = new PropertyType<>(GoodieObject::getShort, GoodieObject::put);
        public static final PropertyType<Integer> INT = new PropertyType<>(GoodieObject::getInteger, GoodieObject::put);
        public static final PropertyType<Long> LONG = new PropertyType<>(GoodieObject::getLong, GoodieObject::put);
        public static final PropertyType<Float> FLOAT = new PropertyType<>(GoodieObject::getFloat, GoodieObject::put);
        public static final PropertyType<Double> DOUBLE = new PropertyType<>(GoodieObject::getDouble, GoodieObject::put);

    }

}
