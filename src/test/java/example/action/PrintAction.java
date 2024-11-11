package example.action;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;

public class PrintAction extends TSLAction {

    protected String textToPrint;

    public PrintAction(List<String> args) throws TSLSyntaxException {
        super(args);
        args = consumeMessagePart(args);
        this.textToPrint = String.join(" ", args);
    }

    @Override
    public boolean perform(TSLEventContext ctx) {
        System.out.println("Printing >> " + replaceExpressions(textToPrint, ctx));
        return true;
    }

}
