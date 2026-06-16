package example.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PrintAction extends TSLAction {

    protected List<TSLToken> wordsToPrint;

    public PrintAction(List<TSLClause> sourceArguments) throws TSLSyntaxException {
        super(sourceArguments);
    }

    @Override
    public void parseArguments(TSLPlatform platform, List<TSLClause> arguments) throws TSLSyntaxException {
        this.wordsToPrint = this.sourceArguments.stream()
                .map(TSLClause::getToken)
                .map(Optional::orElseThrow)
                .toList();
    }

    @Override
    public List<TSLToken> perform(TSLEventContext ctx) throws TSLPerformingException {
        System.out.println("Printing >> " + this.wordsToPrint.stream()
                .map(word -> word.evaluate(ctx))
                .collect(Collectors.joining(" ")));

        // Yields nothing
        return Collections.emptyList();
    }

}
