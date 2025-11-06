package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLDoc;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.List;

public class TSLCapture {

    protected TSLDoc tslDoc;

    protected final TSLCaptureId id;
    protected final List<String> paramNames;
    protected final List<Either<TSLWord, TSLAction>> template;

    public TSLCapture(TSLCaptureId id, List<String> paramNames, List<Either<TSLWord, TSLAction>> template) {
        this.id = id;
        this.paramNames = paramNames;
        this.template = template;
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

    public List<Either<TSLWord, TSLAction>> getTemplate() {
        return template;
    }

    public void attachDoc(TSLDoc tslDoc) {
        this.tslDoc = tslDoc;
    }

}
