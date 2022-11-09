package net.programmer.igoodie.tsl.eventqueue.task;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.eventqueue.EventQueueTask;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.jetbrains.annotations.NotNull;

public class EventDispatchTask extends EventQueueTask {

    @NotNull
    protected TheSpawnLanguage tsl;

    @NotNull
    protected TSLRuleset ruleset;

    @NotNull
    protected TSLEvent event;

    @NotNull
    protected GoodieObject eventArguments;

    public EventDispatchTask(@NotNull TheSpawnLanguage tsl,
                             @NotNull TSLRuleset ruleset,
                             @NotNull TSLEvent event,
                             @NotNull GoodieObject eventArguments) {
        this.tsl = tsl;
        this.ruleset = ruleset;
        this.event = event;
        this.eventArguments = eventArguments;
    }

    @Override
    public void run() {
        TSLContext context = new TSLContext(this.tsl);

        if (this.ruleset.getFile() != null) {
            context.setBaseDir(this.ruleset.getFile());
        }

        context.setEvent(this.event);
        context.setEventArguments(this.eventArguments);

        this.ruleset.perform(context);
    }

}
