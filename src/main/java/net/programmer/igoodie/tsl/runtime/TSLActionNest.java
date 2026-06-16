package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.token.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;

import java.util.stream.Collectors;

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

    @Override
    public String toDebugString() {
        StringBuilder sb = new StringBuilder(this.deferredAction.getName());

        if (!this.deferredAction.getSourceArguments().isEmpty()) {
            sb.append(" ");
            sb.append(this.deferredAction.getSourceArguments().stream()
                    .map(TSLClause::toDebugString)
                    .collect(Collectors.joining(" ")));
        }

        if (this.deferredAction.getYieldConsumer() != null) {
            sb.append(" ");
            sb.append(this.deferredAction.getYieldConsumer().map(
                    TSLCaptureId::toDebugString,
                    TSLExpression::toDebugString
            ));
        }

        if (this.deferredAction.getDisplaying() != null) {
            sb.append(" ");
            sb.append(this.deferredAction.getDisplaying().toDebugString());
        }

        return sb.toString();
    }

}
