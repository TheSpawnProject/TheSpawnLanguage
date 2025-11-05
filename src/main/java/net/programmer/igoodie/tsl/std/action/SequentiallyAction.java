package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * SEQUENTIALLY <action>
 * [AND <action>]+
 */
public class SequentiallyAction extends TSLAction {

    protected List<TSLAction> actions;

    public SequentiallyAction(List<Either<TSLWord, TSLAction>> sourceArguments) throws TSLSyntaxException {
        super(sourceArguments);
    }

    @Override
    public void interpretArguments(TSLPlatform platform) throws TSLSyntaxException {
        this.actions = new ArrayList<>();

        for (int i = 0; i < this.sourceArguments.size(); i++) {
            Either<TSLWord, TSLAction> arg = this.sourceArguments.get(i);

            if (i % 2 == 0) {
                TSLAction action = arg.getRight().orElseThrow(() ->
                        new TSLSyntaxException("Expected an action, instead found -> {}", arg.getLeft().orElseThrow()));
                this.actions.add(action);
                continue;
            }

            TSLWord keywordAnd = arg.getLeft().orElseThrow();

            if (!TSLPlainWord.isKeyword(keywordAnd, "AND")) {
                throw new TSLSyntaxException("Expected an AND delimiter between actions.");
            }
        }

        if (this.sourceArguments.size() % 2 != 1) {
            throw new TSLSyntaxException("Expected an action, after AND");
        }
    }

    @Override
    public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
        for (TSLAction action : this.actions) {
            action.perform(ctx);
        }

        return Collections.emptyList();
    }

}
