// Generated from net/programmer/igoodie/tsl/TSLParserImpl.g4 by ANTLR 4.13.1
package net.programmer.igoodie.tsl.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TSLParserImpl}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TSLParserImplVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#tslWords}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTslWords(TSLParserImpl.TslWordsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#tslRuleset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTslRuleset(TSLParserImpl.TslRulesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#tslRules}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTslRules(TSLParserImpl.TslRulesContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#tslRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTslRule(TSLParserImpl.TslRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#tslRuleDoc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTslRuleDoc(TSLParserImpl.TslRuleDocContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#tslDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTslDirective(TSLParserImpl.TslDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#tslDirectiveArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTslDirectiveArgs(TSLParserImpl.TslDirectiveArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#reactionRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReactionRule(TSLParserImpl.ReactionRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(TSLParserImpl.ActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#actionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBody(TSLParserImpl.ActionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#actionId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionId(TSLParserImpl.ActionIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#actionArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionArgs(TSLParserImpl.ActionArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#actionNest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionNest(TSLParserImpl.ActionNestContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#actionYields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionYields(TSLParserImpl.ActionYieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#actionDisplaying}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionDisplaying(TSLParserImpl.ActionDisplayingContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent(TSLParserImpl.EventContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#eventName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventName(TSLParserImpl.EventNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#eventPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventPredicate(TSLParserImpl.EventPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#predicateExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateExpression(TSLParserImpl.PredicateExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#predicateOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateOperation(TSLParserImpl.PredicateOperationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#captureRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaptureRule(TSLParserImpl.CaptureRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#captureHeader}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaptureHeader(TSLParserImpl.CaptureHeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#captureParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaptureParams(TSLParserImpl.CaptureParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup(TSLParserImpl.GroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#groupString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupString(TSLParserImpl.GroupStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#groupExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupExpression(TSLParserImpl.GroupExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#captureCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaptureCall(TSLParserImpl.CaptureCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#captureArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaptureArgs(TSLParserImpl.CaptureArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#word}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWord(TSLParserImpl.WordContext ctx);
	/**
	 * Visit a parse tree produced by {@link TSLParserImpl#predicateWord}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateWord(TSLParserImpl.PredicateWordContext ctx);
}