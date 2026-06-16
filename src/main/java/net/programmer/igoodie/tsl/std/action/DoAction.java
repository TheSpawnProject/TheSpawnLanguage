package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.Collections;
import java.util.List;

/*
 * DO <action>
 * DO ${expr}
 */
public class DoAction extends TSLAction {

    protected Either<TSLExpression, TSLAction> subject;

    public DoAction(List<TSLClause> sourceArguments) throws TSLSyntaxException {
        super(sourceArguments);
    }

    @Override
    public void parseArguments(TSLPlatform platform, List<TSLClause> arguments) throws TSLSyntaxException {
        if (arguments.size() != 1) {
            throw new TSLSyntaxException("Expected 1 argument, found {}", arguments.size());
        }

        TSLClause argument = arguments.get(0);

        if (argument.isToken()) {
            TSLExpression expression = argument.expectToken(TSLExpression.class);
            this.subject = Either.left(expression);
            return;
        }

        TSLAction action = argument.expectAction(platform);
        this.subject = Either.right(action);
    }

    @Override
    public List<TSLToken> perform(TSLEventContext ctx) throws TSLPerformingException {
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
