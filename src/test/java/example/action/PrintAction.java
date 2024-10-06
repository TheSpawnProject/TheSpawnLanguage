package example.action;

import net.programmer.igoodie.exception.TSLSyntaxException;
import net.programmer.igoodie.node.action.TSLAction;
import net.programmer.igoodie.node.event.TSLEventContext;
import net.programmer.igoodie.util.Pair;

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
    public void perform(TSLEventContext ctx) {
        System.out.println(replaceExpressions(textToPrint, ctx));
    }

}
