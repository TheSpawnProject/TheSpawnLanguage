package net.programmer.igoodie.tsl.runtime.definition;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.StringUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TSLEvent {

    protected final String name;
    protected Map<String, Property<?>> propertyTypes;

    public TSLEvent(String name) {
        this.name = StringUtilities.upperFirstLetters(name);
        this.propertyTypes = new HashMap<>();
    }

    public String getName() {
        return name;
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
            if (value == null) return null;
            this.writer.write(eventArgs, propertyName, value);
            return value;
        }

        public static class Builder<T> {

            protected final Property.Reader<T> reader;
            protected final Property.Writer<T> writer;

            protected Builder(Property.Reader<T> reader, Property.Writer<T> writer) {
                this.reader = reader;
                this.writer = writer;
            }

            public Property<T> create(String propertyName) {
                return new Property<>(propertyName, this.reader, this.writer);
            }

            public static <T> Builder<T> of(Property.Reader<T> reader, Property.Writer<T> writer) {
                return new Builder<>(reader, writer);
            }

            public static final Builder<Boolean> BOOLEAN = of(GoodieObject::getBoolean, GoodieObject::put);
            public static final Builder<String> STRING = of(GoodieObject::getString, GoodieObject::put);
            public static final Builder<Character> CHAR = of(GoodieObject::getCharacter, GoodieObject::put);
            public static final Builder<Byte> BYTE = of(GoodieObject::getByte, GoodieObject::put);
            public static final Builder<Short> SHORT = of(GoodieObject::getShort, GoodieObject::put);
            public static final Builder<Integer> INT = of(GoodieObject::getInteger, GoodieObject::put);
            public static final Builder<Long> LONG = of(GoodieObject::getLong, GoodieObject::put);
            public static final Builder<Float> FLOAT = of(GoodieObject::getFloat, GoodieObject::put);
            public static final Builder<Double> DOUBLE = of(GoodieObject::getDouble, GoodieObject::put);

        }
    }

}
