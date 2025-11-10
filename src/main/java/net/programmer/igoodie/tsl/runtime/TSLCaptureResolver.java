package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureCall;
import net.programmer.igoodie.tsl.runtime.word.TSLGroup;
import net.programmer.igoodie.tsl.runtime.word.TSLPlaceholder;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.*;

public class TSLCaptureResolver {

    protected final Map<String, TSLCapture> captureCache;
    protected final TSLCapture capture;
    protected final Map<String, TSLWord> arguments;

    protected List<TSLClause> resolution = new ArrayList<>();

    public TSLCaptureResolver(Map<String, TSLCapture> captureCache, TSLCapture capture, Map<String, TSLWord> arguments) {
        this.captureCache = captureCache;
        this.capture = capture;
        this.arguments = arguments;
    }

    public TSLCaptureResolver(Map<String, TSLCapture> captureCache, TSLCapture capture, List<TSLWord> arguments) {
        this(captureCache, capture, createArgumentMap(capture.paramNames, arguments));
    }

    protected static Map<String, TSLWord> createArgumentMap(List<String> paramNames, List<TSLWord> arguments) {
        Map<String, TSLWord> argumentMap = new HashMap<>();

        for (int i = 0; i < arguments.size(); i++) {
            String paramName = paramNames.get(i);
            TSLWord argument = arguments.get(i);
            argumentMap.put(paramName, argument);
        }

        return argumentMap;
    }

    public List<TSLClause> resolve() {
        for (TSLClause clause : this.capture.template) {
            if (clause.isWord())
                this.resolution.addAll(this.resolveWord(clause.asWord()));
            if (clause.isAction())
                this.resolution.add(this.resolveAction(clause.asAction()));

        }

        return resolution;
    }

    protected List<TSLClause> resolveWord(TSLWord word) {
        if (word instanceof TSLCaptureCall captureCall) {
            String captureName = captureCall.getId().getCaptureName();
            TSLCapture capture = this.captureCache.get(captureName);
            if (capture == null) throw new TSLPerformingException("Cannot find capture named ${}", captureName);
            TSLCaptureResolver captureResolver = new TSLCaptureResolver(this.captureCache, capture, captureCall.getArgs());
            return captureResolver.resolve();
        }

        if (word instanceof TSLPlaceholder placeholder) {
            String parameterName = placeholder.getParameterName();
            TSLWord argument = this.arguments.get(parameterName);
            return Collections.singletonList(argument);
        }

        if (word instanceof TSLGroup group) {
            List<TSLGroup.Word> resolvedGroupWords = group.getArgs().stream().map(groupWord -> {
                if (groupWord instanceof TSLGroup.Expression expr) {
                    if (expr.getExpressionWord() instanceof TSLPlaceholder placeholder) {
                        TSLWord argument = arguments.get(placeholder.getParameterName());
                        return new TSLGroup.Expression(argument);
                    }
                }

                return groupWord;
            }).toList();

            return Collections.singletonList(new TSLGroup(resolvedGroupWords));
        }

        return Collections.singletonList(word);
    }

    protected TSLAction resolveAction(TSLAction action) {
        // TODO: How do we resolve actions with this much coupling?
        return action;
    }

}
