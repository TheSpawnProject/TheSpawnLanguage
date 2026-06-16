package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.runtime.token.TSLGroup;
import net.programmer.igoodie.tsl.runtime.token.TSLPlaceholder;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.*;

public class TSLTemplateTransformer {

    protected List<TSLClause> clauses;

    public TSLTemplateTransformer(List<TSLClause> template) {
        this.clauses = template;
    }

    public List<TSLClause> getClauses() {
        return clauses;
    }

    protected Map<String, TSLClause> composeArgumentMap(List<String> paramNames, List<TSLClause> argList) {
        Map<String, TSLClause> argumentMap = new HashMap<>();

        ListAccessor<TSLClause> argListAccessor = ListAccessor.of(argList);

        for (int i = 0; i < paramNames.size(); i++) {
            String paramName = paramNames.get(i);
            argListAccessor.get(i).ifPresent(argument -> argumentMap.put(paramName, argument));
        }

        return argumentMap;
    }

    @Deprecated
    public TSLTemplateTransformer collapseCaptures(Map<String, TSLCapture> captureCache) {
        List<TSLClause> transformed = new ArrayList<>();

        for (TSLClause clause : this.clauses) {
            if (clause instanceof TSLCaptureCall captureCall) {
                TSLCapture capture = captureCache.get(captureCall.getId().getCaptureName());
                if (capture == null) {
                    throw new TSLPerformingException("Capture does not exist -> ${}", captureCall.getId().getCaptureName());
                }
                TSLTemplateTransformer transformer = new TSLTemplateTransformer(capture.getTemplate());
                List<TSLClause> capturedClauses = transformer
                        .collapsePlaceholders(capture.getParamNames(), captureCall.getArgs())
                        .collapseCaptures(captureCache)
                        .getClauses();
                transformed.addAll(capturedClauses);
                continue;
            }

            if (clause.isNest()) {
                TSLActionNest transformedNest = this.collapseCaptures(clause.asNest(), captureCache);
                transformed.add(transformedNest);
                continue;
            }

            transformed.add(clause);
        }

        this.clauses = transformed;

        return this;
    }

    @Deprecated
    protected TSLActionNest collapseCaptures(TSLActionNest nest, Map<String, TSLCapture> captureCache) {
        return null;
//        TSLAction.Deferred deferredAction = nest.getDeferredAction();
//
//        TSLToken displaying = deferredAction.getDisplaying();
//        List<TSLClause> newArgs = new ArrayList<>();
//
//        if (displaying instanceof TSLCaptureCall) {
//            List<TSLClause> transformedDisplaying = new TSLTemplateTransformer(Collections.singletonList(displaying))
//                    .collapseCaptures(captureCache)
//                    .getClauses();
//            if (transformedDisplaying.size() != 1) {
//                throw new TSLSyntaxException("Capture call on DISPLAYING statement evaluated to a list of tokens.").atToken(displaying);
//            }
//            displaying = transformedDisplaying.get(0).expectToken();
//        }
//
//        boolean needsTransformation = displaying != deferredAction.getDisplaying();
//
//        // TODO: Continue from here
//
//        for (TSLClause sourceArgument : deferredAction.getSourceArguments()) {
//            if (sourceArgument instanceof TSLPlaceholder placeholder) {
//                TSLToken argument = arguments.get(placeholder.getParameterName()).expectToken();
//                newArgs.add(argument);
//                needsTransformation = true;
//                continue;
//            }
//
//            if (sourceArgument instanceof TSLGroup group) {
//                TSLGroup transformedGroup = this.collapsePlaceholders(group, arguments);
//                newArgs.add(transformedGroup);
//                continue;
//            }
//
//            if (sourceArgument instanceof TSLCaptureCall captureCall) {
//                TSLCaptureCall transformedCall = this.collapsePlaceholders(captureCall, arguments);
//                newArgs.add(transformedCall);
//                continue;
//            }
//
//            newArgs.add(sourceArgument);
//        }
//
//        if (!needsTransformation) return nest;
//
//        deferredAction = new TSLAction.Deferred(deferredAction.getName(), newArgs)
//                .setYieldConsumer(deferredAction.getYieldConsumer())
//                .setDisplaying(displaying);
//
//        return new TSLActionNest(deferredAction);
    }

    /* --------------------------- */

    @Deprecated
    public TSLTemplateTransformer collapsePlaceholders(List<String> paramNames, List<TSLClause> argList) {
        return this.collapsePlaceholders(composeArgumentMap(paramNames, argList));
    }

    @Deprecated
    public TSLTemplateTransformer collapsePlaceholders(Map<String, TSLClause> arguments) {
        List<TSLClause> transformed = new ArrayList<>();

        for (TSLClause clause : this.clauses) {
            if (clause instanceof TSLPlaceholder placeholder) {
                TSLClause argument = arguments.get(placeholder.getParameterName());
                transformed.add(argument);
                continue;
            }

            if (clause instanceof TSLGroup group) {
                TSLGroup transformedGroup = this.collapsePlaceholders(group, arguments);
                transformed.add(transformedGroup);
                continue;
            }

            if (clause instanceof TSLCaptureCall captureCall) {
                TSLCaptureCall transformedCall = this.collapsePlaceholders(captureCall, arguments);
                transformed.add(transformedCall);
                continue;
            }

            if (clause.isNest()) {
                TSLActionNest transformedNest = this.collapsePlaceholders(clause.asNest(), arguments);
                transformed.add(transformedNest);
                continue;
            }

            transformed.add(clause);
        }

        this.clauses = transformed;

        return this;
    }

    @Deprecated
    protected TSLGroup collapsePlaceholders(TSLGroup group, Map<String, TSLClause> arguments) {
        boolean needsTransform = group.getArgs().stream().anyMatch(groupArg ->
                groupArg instanceof TSLGroup.Expression groupExpr
                        && groupExpr.getExpressionToken() instanceof TSLPlaceholder);

        if (!needsTransform) return group;

        List<TSLGroup.Token> newArgs = new ArrayList<>();

        for (TSLGroup.Token arg : group.getArgs()) {
            if (!(arg instanceof TSLGroup.Expression groupExpr)) {
                newArgs.add(arg);
                continue;
            }

            if (groupExpr.getExpressionToken() instanceof TSLPlaceholder placeholder) {
                TSLToken expressionToken = arguments.get(placeholder.getParameterName()).expectToken();
                TSLGroup.Expression newGroupExpr = new TSLGroup.Expression(expressionToken);
                newArgs.add(newGroupExpr);
                continue;
            }

            newArgs.add(arg);
        }

        return new TSLGroup(newArgs);
    }

    @Deprecated
    protected TSLCaptureCall collapsePlaceholders(TSLCaptureCall captureCall, Map<String, TSLClause> arguments) {
        boolean needsTransform = captureCall.getArgs().stream().anyMatch(arg ->
                arg instanceof TSLPlaceholder);

        if (!needsTransform) return captureCall;

        List<TSLClause> newArgs = new ArrayList<>();

        for (TSLClause arg : captureCall.getArgs()) {
            if (arg instanceof TSLPlaceholder placeholder) {
                TSLToken argument = arguments.get(placeholder.getParameterName()).expectToken();
                newArgs.add(argument);
                continue;
            }

            newArgs.add(arg);
        }

        return new TSLCaptureCall(captureCall.getId(), newArgs);
    }

    @Deprecated
    protected TSLActionNest collapsePlaceholders(TSLActionNest nest, Map<String, TSLClause> arguments) {
        TSLAction.Deferred deferredAction = nest.getDeferredAction();

        TSLToken displaying = deferredAction.getDisplaying();
        List<TSLClause> newArgs = new ArrayList<>();

        if (displaying instanceof TSLPlaceholder placeholder) {
            displaying = arguments.get(placeholder.getParameterName()).expectToken();
        }

        boolean needsTransformation = displaying != deferredAction.getDisplaying();

        for (TSLClause sourceArgument : deferredAction.getSourceArguments()) {
            if (sourceArgument instanceof TSLPlaceholder placeholder) {
                TSLToken argument = arguments.get(placeholder.getParameterName()).expectToken();
                newArgs.add(argument);
                needsTransformation = true;
                continue;
            }

            if (sourceArgument instanceof TSLGroup group) {
                TSLGroup transformedGroup = this.collapsePlaceholders(group, arguments);
                newArgs.add(transformedGroup);
                continue;
            }

            if (sourceArgument instanceof TSLCaptureCall captureCall) {
                TSLCaptureCall transformedCall = this.collapsePlaceholders(captureCall, arguments);
                newArgs.add(transformedCall);
                continue;
            }

            newArgs.add(sourceArgument);
        }

        if (!needsTransformation) return nest;

        deferredAction = new TSLAction.Deferred(deferredAction.getName(), newArgs)
                .setYieldConsumer(deferredAction.getYieldConsumer())
                .setDisplaying(displaying);

        return new TSLActionNest(deferredAction);
    }

    /* --------------------------- */

    // TODO: Rework
    // 1. Get capture by refereeCall.getId()
    // 2. Replace placeholders with refereeCall.getArgs()
    // 3. Recursively traverse clauses, and replace capture calls if present

    // collapseCaptures(List<TSLClause>): List<TSLClause>
    // collapseCapture(List<TSLClause>, Map<String, TSLCapture>): List<TSLClause>
    // collapsePlaceholders(List<TSLClause>, Map<String, TSLClause>): List<TSLClause>

    public TSLTemplateTransformer collapsePlaceholders2(Map<String, TSLClause> arguments) {
        List<TSLClause> transformedClauses = new ArrayList<>();

        for (TSLClause clause : this.clauses) {
            if (clause.isNest()) {
                // TODO
                continue;
            }

            if (clause.isToken()) {
                if (clause instanceof TSLPlaceholder placeholder) {
                    // TODO:
                    continue;
                }

                if (clause instanceof TSLGroup group) {
                    // TODO:
                    continue;
                }

                if (clause instanceof TSLCaptureCall captureCall) {
                    // TODO:
                    continue;
                }

                transformedClauses.add(clause);
            }
        }

        this.clauses = transformedClauses;
        return this;
    }

    protected static TSLGroup collapsePlaceholdersInGroup(TSLGroup group, Map<String, TSLClause> arguments) {
        boolean transformed = false;

        List<TSLGroup.Token> newArgs = new ArrayList<>();

        for (TSLGroup.Token arg : group.getArgs()) {
            if (arg instanceof TSLGroup.Expression groupExpr) {
                if (groupExpr.getExpressionToken() instanceof TSLPlaceholder placeholder) {
                    TSLToken expressionToken = arguments.get(placeholder.getParameterName()).expectToken();
                    TSLGroup.Expression newGroupExpr = new TSLGroup.Expression(expressionToken);
                    newGroupExpr.setSource(expressionToken.getSource());
                    newArgs.add(newGroupExpr);
                    transformed = true;
                    continue;
                }
            }

            newArgs.add(arg);
        }

        if (!transformed) return group;

        TSLGroup newGroup = new TSLGroup(newArgs);
        newGroup.setSource(group.getSource());
        return newGroup;
    }

    protected static TSLCaptureCall collapsePlaceholdersInCaptureCall(TSLCaptureCall captureCall, Map<String, TSLClause> arguments) {
        boolean transformed = false;

        List<TSLClause> newArgs = new ArrayList<>();

        for (TSLClause arg : captureCall.getArgs()) {
            if (arg instanceof TSLPlaceholder placeholder) {
                TSLToken argument = arguments.get(placeholder.getParameterName()).expectToken();
                newArgs.add(argument);
                transformed = true;
                continue;
            }

            newArgs.add(arg);
        }

        if (!transformed) return captureCall;

        TSLCaptureCall newCaptureCall = new TSLCaptureCall(captureCall.getId(), newArgs);
        newCaptureCall.setSource(captureCall.getSource());
        return newCaptureCall;
    }

    /* --------------------------- */

    public List<Object> evaluateTokens(TSLEventContext ctx) {
        List<Object> transformed = new ArrayList<>();

        for (TSLClause clause : clauses) {
            if (clause.isNest()) {
                List<Object> transformedNest = this.evaluateTokens(clause.asNest(), ctx);
                transformed.add(transformedNest);
                continue;
            }

            transformed.add(clause.asToken().evaluate(ctx));
        }

        return transformed;
    }

    protected List<Object> evaluateTokens(TSLActionNest nest, TSLEventContext ctx) {
        List<Object> transformed = new ArrayList<>();

        TSLAction.Deferred deferredAction = nest.getDeferredAction();

        transformed.add(deferredAction.getName());

        List<Object> argsEvaluated = new TSLTemplateTransformer(deferredAction.getSourceArguments())
                .evaluateTokens(ctx);

        transformed.add(argsEvaluated);

        // TODO: Add yielding and displaying in AST token order

        if (deferredAction.getDisplaying() != null) {
            Object displayingEvaluated = new TSLTemplateTransformer(Collections.singletonList(deferredAction.getDisplaying()))
                    .evaluateTokens(ctx).get(0);
            transformed.add(displayingEvaluated);
        }

        if (deferredAction.getYieldConsumer() != null) {
            deferredAction.getYieldConsumer().consume(
                    tslCaptureId -> transformed.add("$" + tslCaptureId.getCaptureName()),
                    expression -> transformed.add(expression.evaluate(ctx))
            );
        }

        return transformed;
    }

}
