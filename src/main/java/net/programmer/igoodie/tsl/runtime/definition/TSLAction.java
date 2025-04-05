package net.programmer.igoodie.tsl.runtime.definition;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.List;

public abstract class TSLAction {

    public TSLAction(TSLPlatform platform, List<Either<TSLWord, TSLAction>> args) {}

    public abstract void perform(TSLEventContext ctx) throws TSLPerformingException;

}
