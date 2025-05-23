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
    protected final List<String> params;
    protected final List<Either<TSLWord, TSLAction>> contents;

    public TSLCapture(TSLCaptureId id, List<String> params, List<Either<TSLWord, TSLAction>> contents) {
        this.id = id;
        this.params = params;
        this.contents = contents;
    }

    public TSLDoc getTslDoc() {
        return tslDoc;
    }

    public TSLCaptureId getId() {
        return id;
    }

    public void attachDoc(TSLDoc tslDoc) {
        this.tslDoc = tslDoc;
    }

}
