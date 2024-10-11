package example.action;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.Pair;

import java.util.List;

public class PrintAction extends TSLAction {

    protected String textToPrint;

    public PrintAction(List<String> args) throws TSLSyntaxException {
        super(args);
        Pair<List<String>, List<String>> pair = splitDisplaying(args);
        this.message = pair.getRight();
        this.textToPrint = String.join(" ", pair.getLeft());
    }

    @Override
    public boolean perform(TSLEventContext ctx) {
        System.out.println("Printing >> " + replaceExpressions(textToPrint, ctx));
        return true;
    }

}
