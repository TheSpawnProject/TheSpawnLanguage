package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.runtime.token.TSLGroup;
import net.programmer.igoodie.tsl.runtime.token.TSLPlaceholder;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.*;

@Deprecated(forRemoval = true)
public class TSLCaptureResolver {

    protected final Map<String, TSLCapture> captureCache;
    protected final TSLCapture capture;
    protected final Map<String, TSLClause> arguments;

    protected List<TSLClause> resolution = new ArrayList<>();

    public TSLCaptureResolver(Map<String, TSLCapture> captureCache, TSLCapture capture, Map<String, TSLClause> arguments) {
        this.captureCache = captureCache;
        this.capture = capture;
        this.arguments = arguments;
    }

    public TSLCaptureResolver(Map<String, TSLCapture> captureCache, TSLCapture capture, List<TSLClause> arguments) {
        this(captureCache, capture, createArgumentMap(capture.paramNames, arguments));
    }

    protected static Map<String, TSLClause> createArgumentMap(List<String> paramNames, List<TSLClause> arguments) {
        Map<String, TSLClause> argumentMap = new HashMap<>();

        for (int i = 0; i < arguments.size(); i++) {
            String paramName = paramNames.get(i);
            TSLClause argument = arguments.get(i);
            argumentMap.put(paramName, argument);
        }

        return argumentMap;
    }

    public List<TSLClause> resolve() {
        for (TSLClause clause : this.capture.template) {
            if (clause.isToken())
                this.resolution.addAll(this.resolveToken(clause.asToken()));
            if (clause.isNest())
                this.resolution.add(this.resolveNest(clause.asNest()));
        }

        return resolution;
    }

    protected List<TSLClause> resolveToken(TSLToken token) {
        if (token instanceof TSLCaptureCall captureCall) {
            String captureName = captureCall.getId().getCaptureName();
            TSLCapture capture = this.captureCache.get(captureName);
            if (capture == null) throw new TSLPerformingException("Cannot find capture named ${}", captureName);
            TSLCaptureResolver captureResolver = new TSLCaptureResolver(this.captureCache, capture, captureCall.getArgs());
            return captureResolver.resolve();
        }

        if (token instanceof TSLPlaceholder placeholder) {
            String parameterName = placeholder.getParameterName();
            TSLClause argument = this.arguments.get(parameterName);
            return Collections.singletonList(argument);
        }

        if (token instanceof TSLGroup group) {
            List<TSLGroup.Token> resolvedGroupTokens = group.getArgs().stream().map(groupToken -> {
                if (groupToken instanceof TSLGroup.Expression expr) {
                    if (expr.getExpressionToken() instanceof TSLPlaceholder placeholder) {
                        TSLClause argument = arguments.get(placeholder.getParameterName());
                        return new TSLGroup.Expression(argument.expectToken());
                    }
                }

                return groupToken;
            }).toList();

            return Collections.singletonList(new TSLGroup(resolvedGroupTokens));
        }

        return Collections.singletonList(token);
    }

    protected TSLTokenNest resolveNest(TSLTokenNest nest) {
        TSLTokenNest.Builder builder = new TSLTokenNest.Builder();
        nest.clauses.forEach(builder::push);
        return builder.build();
    }

}
