package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.runtime.word.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.Collections;
import java.util.List;

/*
 * DO <action>
 * DO ${expr}
 */
public class DoAction extends TSLAction {

    protected Either<TSLExpression, TSLAction> subject;

    public DoAction(List<Either<TSLWord, TSLAction>> sourceArguments) throws TSLSyntaxException {
        super(sourceArguments);
    }

    @Override
    public void interpretArguments(TSLPlatform platform) throws TSLSyntaxException {
        if (this.sourceArguments.size() != 1) {
            throw new TSLSyntaxException("Expected 1 argument, found {}", this.sourceArguments.size());
        }

        this.subject = this.sourceArguments.get(0).map(
                word -> {
                    if (!(word instanceof TSLExpression expression)) {
                        throw new TSLSyntaxException("Unexpected token {}", word);
                    }
                    return expression;
                },
                action -> action
        );
    }

    @Override
    public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
        return this.subject.reduce(
                expression -> {
                    String value = expression.evaluate(ctx);
                    TSLPlainWord word = new TSLPlainWord(value);
                    return Collections.singletonList(word);
                },
                action -> action.perform(ctx)
        );
    }

}
