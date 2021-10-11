package example.plugin.comparator;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;

public class EqualsComparator extends TSLComparator {

    public static final EqualsComparator INSTANCE = new EqualsComparator();

    private EqualsComparator() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "=");
    }

    @Override
    public boolean verifySyntax(List<TSLToken> rightHand) throws TSLSyntaxError {
        return false;
    }

    @Override
    public boolean satisfies(Object leftHand, List<String> rightHand) throws TSLRuntimeError {
        return false;
    }

//    @Override
//    public boolean compare(Object lefthand, String righthand) {
//        if (lefthand instanceof String) {
//            String leftString = (String) lefthand;
//            return leftString.equalsIgnoreCase(righthand);
//
//        } else if (lefthand instanceof Number) {
//            Number leftNumber = (Number) lefthand;
//            Number rightNumber = parseDouble(righthand, -1.0);
//            return leftNumber.doubleValue() == rightNumber.doubleValue();
//        }
//
//        return false;
//    }

}
