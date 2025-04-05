package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;

import java.util.List;

public class TSLCapture {

    protected final TSLCaptureId id;
    protected final List<String> params;
    protected final List<TSLWord> contents;

    public TSLCapture(TSLCaptureId id, List<String> params, List<TSLWord> contents) {
        this.id = id;
        this.params = params;
        this.contents = contents;
    }

}
