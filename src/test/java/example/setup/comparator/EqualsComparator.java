package example.setup.comparator;

import net.programmer.igoodie.tsl.definition.TSLComparator;

public class EqualsComparator extends TSLComparator {

    public static final EqualsComparator INSTANCE = new EqualsComparator();

    private EqualsComparator() {
        super("=");
    }

    @Override
    public boolean compare(Object lefthand, String righthand) {
        if (lefthand instanceof String) {
            String leftString = (String) lefthand;
            return leftString.equalsIgnoreCase(righthand);

        } else if (lefthand instanceof Number) {
            Number leftNumber = (Number) lefthand;
            Number rightNumber = parseDouble(righthand, -1.0);
            return leftNumber.doubleValue() == rightNumber.doubleValue();
        }

        return false;
    }

}
