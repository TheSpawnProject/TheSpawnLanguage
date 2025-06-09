package example.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PrintAction extends TSLAction {

    protected final List<TSLWord> wordsToPrint;

    public PrintAction(TSLPlatform platform, List<Either<TSLWord, TSLAction>> args) throws TSLSyntaxException {
        super(platform, args);

        this.wordsToPrint = args.stream()
                .map(Either::getLeft)
                .map(Optional::orElseThrow)
                .toList();
    }

    @Override
    public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
        System.out.println("Printing >> " + this.wordsToPrint.stream()
                .map(word -> word.evaluate(ctx))
                .collect(Collectors.joining(" ")));

        // Yields nothing
        return Collections.emptyList();
    }

}
