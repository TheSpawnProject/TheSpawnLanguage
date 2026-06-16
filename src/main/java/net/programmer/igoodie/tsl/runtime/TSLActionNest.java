package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;

public class TSLActionNest implements TSLClause {

    protected final TSLAction.Deferred deferredAction;

    public TSLActionNest(TSLAction.Deferred deferredAction) {
        this.deferredAction = deferredAction;
    }

    public TSLAction.Deferred getDeferredAction() {
        return deferredAction;
    }

    public TSLAction resolveAction(TSLPlatform platform) {
        return deferredAction.resolve(platform);
    }

}
