package net.programmer.igoodie.tsl.definition.base;

import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.OptionalUtils;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public class TSLArguments {

    public static Optional<Boolean> parseBoolean(TSLToken token, TSLContext context) {
        return Optional.of(token.isTrue(context));
    }

    public static Optional<Double> parseDouble(TSLToken token, TSLContext context) {
        String value = token.evaluate(context);
        return OptionalUtils.create(() -> Double.parseDouble(value));
    }

    public static Optional<Float> parseFloat(TSLToken token, TSLContext context) {
        String value = token.evaluate(context);
        return OptionalUtils.create(() -> Float.parseFloat(value));
    }

    /**
     * Able to parse those: <br/>
     * <ul>
     *     <li>"9" -> 9</li>
     *     <li>"9.99" -> 9 (Rounds down)</li>
     * </ul>
     */
    public static Optional<Integer> parseInt(TSLToken token, TSLContext context) {
        return parseFloat(token, context).map(Float::intValue);
    }


    /**
     * Able to parse those: <br/>
     * <ul>
     *     <li>"9" -> 9</li>
     *     <li>"9.99" -> 9 (Rounds down)</li>
     * </ul>
     */
    public static Optional<Short> parseShort(TSLToken token, TSLContext context) {
        return parseFloat(token, context).map(Float::shortValue);
    }


    /**
     * Able to parse those: <br/>
     * <ul>
     *     <li>"9" -> 9</li>
     *     <li>"9.99" -> 9 (Rounds down)</li>
     * </ul>
     */
    public static Optional<Byte> parseByte(TSLToken token, TSLContext context) {
        return parseFloat(token, context).map(Float::byteValue);
    }

    public static Optional<UUID> parseUUID(TSLToken token, TSLContext context) {
        String value = token.evaluate(context);
        return OptionalUtils.create(() -> UUID.fromString(value));
    }

    public static Optional<OffsetDateTime> parseIsoDate(TSLToken token, TSLContext context) {
        String value = token.evaluate(context);
        return OptionalUtils.create(() -> OffsetDateTime.parse(value));
    }

}
