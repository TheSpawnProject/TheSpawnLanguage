package net.programmer.igoodie.tsl.runtime.listener;

import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRule;

import java.util.List;

public interface TSLRuleListener extends TSLEventListener {

    void onEventMismatch(TSLRule rule, TSLContext context, TSLEvent mismatchingEvent);

    void onPredicateMismatch(TSLRule rule, TSLContext context, TSLPredicate mismatchingPredicate);

    void beforeActionPerform(TSLRule rule, TSLContext context, List<TSLToken> actionTokens);

    void afterActionPerform(TSLRule rule, TSLContext context, List<TSLToken> actionTokens);

}
