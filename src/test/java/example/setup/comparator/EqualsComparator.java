package example.setup.comparator;

import net.programmer.igoodie.tsl.definition.TSLComparator;

public class EqualsComparator extends TSLComparator {

    public static final EqualsComparator INSTANCE = new EqualsComparator();

    private EqualsComparator() {
        super("=");
    }

    @Override
    public boolean compare(Object lefthand, Object righthand) {
        if (lefthand instanceof String && righthand instanceof String) {
            String leftString = (String) lefthand;
            String rightString = (String) righthand;
            return leftString.equalsIgnoreCase(rightString);

        } else if (lefthand instanceof Number && righthand instanceof Number) {
            Number leftNumber = (Number) lefthand;
            Number rightNumber = (Number) righthand;
            return leftNumber.doubleValue() == rightNumber.doubleValue();
        }

        return false;
    }

}
