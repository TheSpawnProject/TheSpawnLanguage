package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TSLRulesetInterpreter extends TSLInterpreter<TSLDeferred<TSLRuleset>, TSLParserImpl.TslRulesetContext> {

    protected List<TSLDeferred<TSLCapture>> captures = new ArrayList<>();
    protected List<TSLDeferred<TSLRule>> rules = new ArrayList<>();

    @Override
    public TSLDeferred<TSLRuleset> yieldValue(TSLParserImpl.TslRulesetContext tree) {
        return platform -> {
            // TODO: Parse TSLDirectives, and check for target
            return Optional.empty();
        };
    }

    @Override
    public TSLDeferred<TSLRuleset> visitTslRule(TSLParserImpl.TslRuleContext ctx) {
        TSLParserImpl.TslRuleDocContext tsldocTree = ctx.tslRuleDoc();

        if (tsldocTree != null) {
            // TODO Attach TSLDoc to created rule
        }

        TSLParserImpl.ReactionRuleContext reactionRuleTree = ctx.reactionRule();

        if (reactionRuleTree != null) {
            this.rules.add(new TSLRuleInterpreter().interpret(reactionRuleTree));

            return null;
        }

        TSLParserImpl.CaptureRuleContext captureRuleTree = ctx.captureRule();

        if (captureRuleTree != null) {
            this.captures.add(new TSLCaptureInterpreter().interpret(captureRuleTree));

            return null;
        }

        throw new TSLInternalException("Matched unexpected rule");
    }

}
