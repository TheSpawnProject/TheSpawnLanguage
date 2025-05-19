// Generated from net/programmer/igoodie/tsl/TSLParserImpl.g4 by ANTLR 4.13.1
package net.programmer.igoodie.tsl.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TSLParserImpl}.
 */
public interface TSLParserImplListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#tslWords}.
	 * @param ctx the parse tree
	 */
	void enterTslWords(TSLParserImpl.TslWordsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#tslWords}.
	 * @param ctx the parse tree
	 */
	void exitTslWords(TSLParserImpl.TslWordsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#tslRuleset}.
	 * @param ctx the parse tree
	 */
	void enterTslRuleset(TSLParserImpl.TslRulesetContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#tslRuleset}.
	 * @param ctx the parse tree
	 */
	void exitTslRuleset(TSLParserImpl.TslRulesetContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#tslRules}.
	 * @param ctx the parse tree
	 */
	void enterTslRules(TSLParserImpl.TslRulesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#tslRules}.
	 * @param ctx the parse tree
	 */
	void exitTslRules(TSLParserImpl.TslRulesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#tslRule}.
	 * @param ctx the parse tree
	 */
	void enterTslRule(TSLParserImpl.TslRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#tslRule}.
	 * @param ctx the parse tree
	 */
	void exitTslRule(TSLParserImpl.TslRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#tslRuleDoc}.
	 * @param ctx the parse tree
	 */
	void enterTslRuleDoc(TSLParserImpl.TslRuleDocContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#tslRuleDoc}.
	 * @param ctx the parse tree
	 */
	void exitTslRuleDoc(TSLParserImpl.TslRuleDocContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#tslDirective}.
	 * @param ctx the parse tree
	 */
	void enterTslDirective(TSLParserImpl.TslDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#tslDirective}.
	 * @param ctx the parse tree
	 */
	void exitTslDirective(TSLParserImpl.TslDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#tslDirectiveArgs}.
	 * @param ctx the parse tree
	 */
	void enterTslDirectiveArgs(TSLParserImpl.TslDirectiveArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#tslDirectiveArgs}.
	 * @param ctx the parse tree
	 */
	void exitTslDirectiveArgs(TSLParserImpl.TslDirectiveArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#reactionRule}.
	 * @param ctx the parse tree
	 */
	void enterReactionRule(TSLParserImpl.ReactionRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#reactionRule}.
	 * @param ctx the parse tree
	 */
	void exitReactionRule(TSLParserImpl.ReactionRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(TSLParserImpl.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(TSLParserImpl.ActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#actionBody}.
	 * @param ctx the parse tree
	 */
	void enterActionBody(TSLParserImpl.ActionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#actionBody}.
	 * @param ctx the parse tree
	 */
	void exitActionBody(TSLParserImpl.ActionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#actionId}.
	 * @param ctx the parse tree
	 */
	void enterActionId(TSLParserImpl.ActionIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#actionId}.
	 * @param ctx the parse tree
	 */
	void exitActionId(TSLParserImpl.ActionIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#actionArgs}.
	 * @param ctx the parse tree
	 */
	void enterActionArgs(TSLParserImpl.ActionArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#actionArgs}.
	 * @param ctx the parse tree
	 */
	void exitActionArgs(TSLParserImpl.ActionArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#actionNest}.
	 * @param ctx the parse tree
	 */
	void enterActionNest(TSLParserImpl.ActionNestContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#actionNest}.
	 * @param ctx the parse tree
	 */
	void exitActionNest(TSLParserImpl.ActionNestContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#actionYields}.
	 * @param ctx the parse tree
	 */
	void enterActionYields(TSLParserImpl.ActionYieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#actionYields}.
	 * @param ctx the parse tree
	 */
	void exitActionYields(TSLParserImpl.ActionYieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#actionDisplaying}.
	 * @param ctx the parse tree
	 */
	void enterActionDisplaying(TSLParserImpl.ActionDisplayingContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#actionDisplaying}.
	 * @param ctx the parse tree
	 */
	void exitActionDisplaying(TSLParserImpl.ActionDisplayingContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(TSLParserImpl.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(TSLParserImpl.EventContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#eventName}.
	 * @param ctx the parse tree
	 */
	void enterEventName(TSLParserImpl.EventNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#eventName}.
	 * @param ctx the parse tree
	 */
	void exitEventName(TSLParserImpl.EventNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#eventPredicate}.
	 * @param ctx the parse tree
	 */
	void enterEventPredicate(TSLParserImpl.EventPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#eventPredicate}.
	 * @param ctx the parse tree
	 */
	void exitEventPredicate(TSLParserImpl.EventPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#predicateExpression}.
	 * @param ctx the parse tree
	 */
	void enterPredicateExpression(TSLParserImpl.PredicateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#predicateExpression}.
	 * @param ctx the parse tree
	 */
	void exitPredicateExpression(TSLParserImpl.PredicateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#predicateOperation}.
	 * @param ctx the parse tree
	 */
	void enterPredicateOperation(TSLParserImpl.PredicateOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#predicateOperation}.
	 * @param ctx the parse tree
	 */
	void exitPredicateOperation(TSLParserImpl.PredicateOperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#captureRule}.
	 * @param ctx the parse tree
	 */
	void enterCaptureRule(TSLParserImpl.CaptureRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#captureRule}.
	 * @param ctx the parse tree
	 */
	void exitCaptureRule(TSLParserImpl.CaptureRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#captureHeader}.
	 * @param ctx the parse tree
	 */
	void enterCaptureHeader(TSLParserImpl.CaptureHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#captureHeader}.
	 * @param ctx the parse tree
	 */
	void exitCaptureHeader(TSLParserImpl.CaptureHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#captureParams}.
	 * @param ctx the parse tree
	 */
	void enterCaptureParams(TSLParserImpl.CaptureParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#captureParams}.
	 * @param ctx the parse tree
	 */
	void exitCaptureParams(TSLParserImpl.CaptureParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#group}.
	 * @param ctx the parse tree
	 */
	void enterGroup(TSLParserImpl.GroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#group}.
	 * @param ctx the parse tree
	 */
	void exitGroup(TSLParserImpl.GroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#groupString}.
	 * @param ctx the parse tree
	 */
	void enterGroupString(TSLParserImpl.GroupStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#groupString}.
	 * @param ctx the parse tree
	 */
	void exitGroupString(TSLParserImpl.GroupStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#groupExpression}.
	 * @param ctx the parse tree
	 */
	void enterGroupExpression(TSLParserImpl.GroupExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#groupExpression}.
	 * @param ctx the parse tree
	 */
	void exitGroupExpression(TSLParserImpl.GroupExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#captureCall}.
	 * @param ctx the parse tree
	 */
	void enterCaptureCall(TSLParserImpl.CaptureCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#captureCall}.
	 * @param ctx the parse tree
	 */
	void exitCaptureCall(TSLParserImpl.CaptureCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#captureArgs}.
	 * @param ctx the parse tree
	 */
	void enterCaptureArgs(TSLParserImpl.CaptureArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#captureArgs}.
	 * @param ctx the parse tree
	 */
	void exitCaptureArgs(TSLParserImpl.CaptureArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#word}.
	 * @param ctx the parse tree
	 */
	void enterWord(TSLParserImpl.WordContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#word}.
	 * @param ctx the parse tree
	 */
	void exitWord(TSLParserImpl.WordContext ctx);
	/**
	 * Enter a parse tree produced by {@link TSLParserImpl#predicateWord}.
	 * @param ctx the parse tree
	 */
	void enterPredicateWord(TSLParserImpl.PredicateWordContext ctx);
	/**
	 * Exit a parse tree produced by {@link TSLParserImpl#predicateWord}.
	 * @param ctx the parse tree
	 */
	void exitPredicateWord(TSLParserImpl.PredicateWordContext ctx);
}