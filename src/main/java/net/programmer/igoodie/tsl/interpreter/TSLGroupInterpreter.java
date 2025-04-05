package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.word.TSLGroup;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;

import java.util.ArrayList;
import java.util.List;

public class TSLGroupInterpreter extends TSLInterpreter<TSLGroup, TSLParserImpl.GroupContext> {

    protected List<TSLGroup.Word> args = new ArrayList<>();

    @Override
    public TSLGroup yieldValue(TSLParserImpl.GroupContext tree) {
        TSLGroup group = new TSLGroup(args);
        group.setSource(tree);
        return group;
    }

    @Override
    public TSLGroup visitGroupString(TSLParserImpl.GroupStringContext ctx) {
        TSLGroup.StringContent stringContent = new TSLGroup.StringContent(ctx.getText());
        stringContent.setSource(ctx);

        this.args.add(stringContent);

        return null;
    }

    @Override
    public TSLGroup visitGroupExpression(TSLParserImpl.GroupExpressionContext ctx) {
        TSLWordInterpreter interpreter = new TSLWordInterpreter();
        TSLWord word = interpreter.interpret(ctx.word());
        TSLGroup.Expression expr = new TSLGroup.Expression(word);
        expr.setSource(ctx);

        this.args.add(expr);

        return null;
    }

}
