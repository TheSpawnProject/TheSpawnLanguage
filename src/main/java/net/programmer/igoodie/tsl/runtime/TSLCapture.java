package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.*;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.*;

public class TSLCapture {

    protected TSLDoc tslDoc;

    protected final TSLCaptureId id;
    protected final List<String> paramNames;
    protected final List<Either<TSLWord, TSLAction>> contents;

    public TSLCapture(TSLCaptureId id, List<String> paramNames, List<Either<TSLWord, TSLAction>> contents) {
        this.id = id;
        this.paramNames = paramNames;
        this.contents = contents;
    }

    public TSLDoc getTslDoc() {
        return tslDoc;
    }

    public TSLCaptureId getId() {
        return id;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public List<Either<TSLWord, TSLAction>> getContents() {
        return contents;
    }

    public void attachDoc(TSLDoc tslDoc) {
        this.tslDoc = tslDoc;
    }

    public List<Either<TSLWord, TSLAction>> resolveContent(Map<String, TSLCapture> captureCache, List<TSLWord> arguments) {
        Map<String, TSLWord> argumentMap = new HashMap<>();

        for (int i = 0; i < arguments.size(); i++) {
            String paramName = this.paramNames.get(i);
            TSLWord argument = arguments.get(i);
            argumentMap.put(paramName, argument);
        }

        return this.resolveContent(captureCache, argumentMap);
    }

    public List<Either<TSLWord, TSLAction>> resolveContent(Map<String, TSLCapture> captureCache, Map<String, TSLWord> arguments) {
        List<Either<TSLWord, TSLAction>> resolved = new ArrayList<>();

        for (Either<TSLWord, TSLAction> content : this.contents) {
            content.consume(
                    word -> resolved.addAll(this.resolveWord(captureCache, arguments, word)),
                    action -> {
                        for (Either<TSLWord, TSLAction> sourceArgument : action.getSourceArguments()) {

                        }
                    }
            );
        }

        return resolved;
    }

    public List<Either<TSLWord, TSLAction>> resolveWord(Map<String, TSLCapture> captureCache, Map<String, TSLWord> arguments, TSLWord word) {
        if (word instanceof TSLCaptureCall captureCall) {
            String captureName = captureCall.getId().getCaptureName();
            TSLCapture capture = captureCache.get(captureName);
            if (capture == null) throw new TSLPerformingException("Cannot find capture named ${}", captureName);
            return capture.resolveContent(captureCache, captureCall.getArgs());
        }

        if (word instanceof TSLPlaceholder placeholder) {
            String parameterName = placeholder.getParameterName();
            TSLWord argument = arguments.get(parameterName);
            return Collections.singletonList(Either.left(argument));
        }

        if (word instanceof TSLGroup group) {
            return Collections.singletonList(Either.left(group.resolveFromCapture(arguments)));
        }

        return Collections.singletonList(Either.left(word));
    }

    public List<Either<TSLWord, TSLAction>> resolveAction(Map<String, TSLCapture> captureCache, Map<String, TSLWord> arguments, TSLAction action) {
        action.getSourceArguments()
    }

}
