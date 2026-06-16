package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.runtime.token.TSLGroup;
import net.programmer.igoodie.tsl.runtime.token.TSLPlaceholder;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                List<TSLClause> transformedClauses = new TSLTemplateTransformer(clause.asNest().getClauses())
//                        .collapsePlace
                        .collapseCaptures(captureCache)
                        .getClauses();
                TSLTokenNest transformedNest = new TSLTokenNest(transformedClauses);
                transformed.add(transformedNest);
                continue;
            }

            transformed.add(clause);
        }

        this.clauses = transformed;

        return this;
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
                List<TSLClause> transformedClauses = new TSLTemplateTransformer(clause.asNest().getClauses())
                        .collapsePlaceholders(arguments)
                        .getClauses();
                TSLTokenNest transformedNest = new TSLTokenNest(transformedClauses);
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
                newGroupExpr.setSource(expressionToken.getSource());
                newArgs.add(newGroupExpr);
                continue;
            }

            newArgs.add(arg);
        }

        TSLGroup newGroup = new TSLGroup(newArgs);
        newGroup.setSource(group.getSource());
        return newGroup;
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

        TSLCaptureCall newCaptureCall = new TSLCaptureCall(captureCall.getId(), newArgs);
        newCaptureCall.setSource(captureCall.getSource());
        return newCaptureCall;
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
                if (clause instanceof TSLPlaceholder placeholder) {

                }

            } else if (clause.isToken()) {

            }
        }

        this.clauses = transformedClauses;
        return this;
    }

    protected static TSLGroup collapsePlaceholdersInGroup(TSLGroup group, Map<String, TSLClause> arguments) {
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
                newGroupExpr.setSource(expressionToken.getSource());
                newArgs.add(newGroupExpr);
                continue;
            }

            newArgs.add(arg);
        }

        TSLGroup newGroup = new TSLGroup(newArgs);
        newGroup.setSource(group.getSource());
        return newGroup;
    }

    public List<Object> evaluateTokens(TSLEventContext ctx) {
        List<Object> transformed = new ArrayList<>();

        for (TSLClause clause : clauses) {
            if (clause.isNest()) {
                transformed.add(new TSLTemplateTransformer(clause.asNest().getClauses())
                        .evaluateTokens(ctx));
                continue;
            }

            transformed.add(clause.asToken().evaluate(ctx));
        }

        return transformed;
    }

}
