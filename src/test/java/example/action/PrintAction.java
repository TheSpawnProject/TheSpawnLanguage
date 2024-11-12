package example.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;

public class PrintAction extends TSLAction {

    protected String textToPrint;

    public PrintAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeMessagePart(args);
        this.textToPrint = String.join(" ", args);
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        System.out.println("Printing >> " + replaceExpressions(textToPrint, ctx));
        return true;
    }

}
