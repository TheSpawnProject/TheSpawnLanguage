package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLDoc;

import java.util.List;

public class TSLCapture {

    protected TSLDoc tslDoc;

    protected final TSLCaptureId id;
    protected final List<String> paramNames;
    protected final List<TSLClause> template;

    public TSLCapture(TSLCaptureId id, List<String> paramNames, List<TSLClause> template) {
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

    public List<TSLClause> getTemplate() {
        return template;
    }

    public void attachDoc(TSLDoc tslDoc) {
        this.tslDoc = tslDoc;
    }

}
