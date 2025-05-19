package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class TSLCaptureInterpreter extends TSLInterpreter<TSLCapture.Ref, TSLParserImpl.CaptureRuleContext> {

    protected TSLCaptureId id;
    protected List<String> params;
    protected List<Either<TSLWord, TSLAction.Ref>> contents;

    @Override
    public TSLCapture.Ref yieldValue(TSLParserImpl.CaptureRuleContext tree) {
        return new TSLCapture.Ref(id, params, contents);
    }

    @Override
    public TSLCapture.Ref visitCaptureHeader(TSLParserImpl.CaptureHeaderContext ctx) {
        this.id = (TSLCaptureId) new TSLWordInterpreter().parseWord(ctx.id);

        this.params = ctx.captureParams().IDENTIFIER().stream()
                .map(ParseTree::getText)
                .toList();

        return null;
    }

    @Override
    public TSLCapture.Ref visitActionArgs(TSLParserImpl.ActionArgsContext ctx) {
        this.contents = new ArrayList<>();

        for (ParseTree child : ctx.children) {
            if (child instanceof TSLParserImpl.WordContext wordChild) {
                TSLWord word = new TSLWordInterpreter().interpret(wordChild);
                this.contents.add(Either.left(word));

            } else if (child instanceof TSLParserImpl.ActionNestContext nestChild) {
                TSLParserImpl.ActionContext actionTree = nestChild.action();
                TSLAction.Ref actionRef = new TSLActionInterpreter().interpret(actionTree);
                this.contents.add(Either.right(actionRef));
            }
        }

        return null;
    }

}
