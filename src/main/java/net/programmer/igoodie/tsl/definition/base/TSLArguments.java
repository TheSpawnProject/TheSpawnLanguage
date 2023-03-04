package net.programmer.igoodie.tsl.definition.base;

import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class TSLArguments {

    public static Parser<String> STRING = new Parser<>(String.class, value -> value);

    public static Parser<Boolean> BOOLEAN = new Parser<>(Boolean.class, value -> {
        if (value.equalsIgnoreCase("TRUE") || value.equals("1")) {
            return true;
        } else {
            throw new TSLRuntimeError("Cannot parse Boolean from given value.");
        }
    });

    public static Parser<Double> DOUBLE = new Parser<>(Double.class, value -> {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new TSLRuntimeError("Cannot parse Double from given value.").causedBy(e);
        }
    });

    public static Parser<Float> FLOAT = new Parser<>(Float.class, value -> {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new TSLRuntimeError("Cannot parse Float from given value.").causedBy(e);
        }
    });

    /**
     * Able to parse those: <br/>
     * <ul>
     *     <li>"9" -> 9</li>
     *     <li>"9.99" -> 9 (Rounds down)</li>
     * </ul>
     */
    public static Parser<Integer> INTEGER = new Parser<>(Integer.class, value ->
            DOUBLE.logic.parse(value).intValue());

    /**
     * Able to parse those: <br/>
     * <ul>
     *     <li>"9" -> 9</li>
     *     <li>"9.99" -> 9 (Rounds down)</li>
     * </ul>
     */
    public static Parser<Short> SHORT = new Parser<>(Short.class, value ->
            DOUBLE.logic.parse(value).shortValue());

    /**
     * Able to parse those: <br/>
     * <ul>
     *     <li>"9" -> 9</li>
     *     <li>"9.99" -> 9 (Rounds down)</li>
     * </ul>
     */
    public static Parser<Byte> BYTE = new Parser<>(Byte.class, value ->
            DOUBLE.logic.parse(value).byteValue());

    public static Parser<java.util.UUID> UUID = new Parser<>(java.util.UUID.class, value -> {
        try {
            return java.util.UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new TSLRuntimeError("Cannot parse UUID from given value.").causedBy(e);
        }
    });

    public static Parser<OffsetDateTime> ISO_DATE = new Parser<>(OffsetDateTime.class, value -> {
        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new TSLRuntimeError("Cannot parse ISO Date from given value.").causedBy(e);
        }
    });

    public static class Parser<T> {

        public interface ParseLogic<T> {
            T parse(String value) throws TSLRuntimeError;
        }

        protected Class<?> type;
        protected ParseLogic<T> logic;

        public Parser(Class<?> type, ParseLogic<T> logic) {
            this.type = type;
            this.logic = logic;
        }

        public Class<?> getType() {
            return type;
        }

        public Optional<T> parse(TSLToken token, TSLContext context) throws TSLRuntimeError {
            if (token == null) return Optional.empty();
            String value = token.evaluate(context);
            return parse(value);
        }

        public Optional<T> parse(String value) throws TSLRuntimeError {
            T parsed = this.logic.parse(value);
            return Optional.ofNullable(parsed);
        }

    }

}
