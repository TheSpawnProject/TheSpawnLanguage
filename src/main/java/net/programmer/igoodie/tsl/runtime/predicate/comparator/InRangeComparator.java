package net.programmer.igoodie.tsl.runtime.predicate.comparator;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InRangeComparator extends TSLComparator {

    public static Pattern RANGE_PATTERN = Pattern.compile("^\\[(?<min>.+),(?<max>.+)\\]$");

    protected double min, max;

    public InRangeComparator(Object right) throws TSLSyntaxException {
        super(right);

        Matcher matcher = RANGE_PATTERN.matcher(right.toString());

        if (!matcher.find())
            throw new TSLSyntaxException("Expected format like [1.0,2.0], found -> {}", right);

        try {
            min = Double.parseDouble(matcher.group("min"));
            max = Double.parseDouble(matcher.group("max"));

        } catch (NumberFormatException e) {
            throw new TSLSyntaxException("Expected valid numbers, found -> {} and {}", matcher.group(1), matcher.group(2));
        }

        if (min > max)
            throw new TSLSyntaxException("Expected first value to be less than the second value.");
    }

    @Override
    public boolean compare(Object left) {
        if (!(left instanceof Number))
            return false;

        double number = ((Number) left).doubleValue();

        return min <= number && number <= max;
    }

}
