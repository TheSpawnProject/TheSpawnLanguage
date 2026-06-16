package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;
import java.util.stream.Collectors;

public class TSLCaptureCall extends TSLToken {

    protected final TSLCaptureId id;
    protected final List<TSLClause> args;

    public TSLCaptureCall(TSLCaptureId id, List<TSLClause> args) {
        this.id = id;
        this.args = args;
    }

    public TSLCaptureId getId() {
        return id;
    }

    public List<TSLClause> getArgs() {
        return args;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        throw new IllegalStateException("CaptureCalls aren't meant for evaluation.");
    }

    @Override
    public String toDebugString() {
        StringBuilder sb = new StringBuilder("$");
        sb.append(this.id.captureName);

        if (!this.args.isEmpty()) {
            sb.append(this.args.stream()
                    .map(TSLClause::toDebugString)
                    .collect(Collectors.joining(",", "(", ")")));
        }

        return sb.toString();
    }

}
