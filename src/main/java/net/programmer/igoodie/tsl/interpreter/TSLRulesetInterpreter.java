package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.word.TSLDoc;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSLRulesetInterpreter extends TSLInterpreter<TSLDeferred<TSLRuleset>, TSLParserImpl.TslRulesetContext> {

    protected List<TSLDeferred<TSLCapture>> captures = new ArrayList<>();
    protected List<TSLDeferred<TSLRule>> rules = new ArrayList<>();
    protected Map<Object, TSLDoc> tslDocs = new HashMap<>();

    @Override
    public TSLDeferred<TSLRuleset> yieldValue(TSLParserImpl.TslRulesetContext tree) {
        return platform -> {
            // TODO: Parse TSLDirectives, and check for target

            TSLRuleset ruleset = new TSLRuleset("TODO:Target");

            this.captures.stream()
                    .map(deferredCapture -> deferredCapture.resolve(platform))
                    .peek(capture -> {
                        TSLDoc tslDoc = this.tslDocs.get(capture);
                        if (tslDoc != null) capture.attachDoc(tslDoc);
                    })
                    .forEach(ruleset::addCapture);

            this.rules.stream()
                    .map(deferredRule -> deferredRule.resolve(platform))
                    .peek(rule -> {
                        TSLDoc tslDoc = this.tslDocs.get(rule);
                        if (tslDoc != null) rule.attachDoc(tslDoc);
                    })
                    .forEach(ruleset::addRule);

            return ruleset;
        };
    }

    @Override
    public TSLDeferred<TSLRuleset> visitTslRule(TSLParserImpl.TslRuleContext ctx) {
        TSLParserImpl.TslRuleDocContext tsldocTree = ctx.tslRuleDoc();

        TSLDoc tslDoc = null;

        if (tsldocTree != null) {
            List<TerminalNode> tsldocNodes = tsldocTree.TSLDOC_COMMENT();
            TerminalNode lastNode = tsldocNodes.get(tsldocNodes.size() - 1);

            tslDoc = new TSLDoc(lastNode.getText());
            tslDoc.setSource(lastNode);
        }

        TSLParserImpl.ReactionRuleContext reactionRuleTree = ctx.reactionRule();

        if (reactionRuleTree != null) {
            TSLDeferred<TSLRule> deferredRule = new TSLRuleInterpreter().interpret(reactionRuleTree);
            this.rules.add(deferredRule);

            if (tslDoc != null) this.tslDocs.put(deferredRule, tslDoc);

            return null;
        }

        TSLParserImpl.CaptureRuleContext captureRuleTree = ctx.captureRule();

        if (captureRuleTree != null) {
            TSLDeferred<TSLCapture> deferredCapture = new TSLCaptureInterpreter().interpret(captureRuleTree);
            this.captures.add(deferredCapture);

            if (tslDoc != null) this.tslDocs.put(deferredCapture, tslDoc);

            return null;
        }

        throw new TSLInternalException("Matched unexpected rule");
    }

}
