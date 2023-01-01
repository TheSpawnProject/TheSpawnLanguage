package net.programmer.igoodie.tsl.eventqueue;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TSLEventBuffer extends ConcurrentLinkedQueue<TSLEventBuffer.Entry> {

    public static class Entry {

        protected String target;
        protected TSLEvent event;
        protected GoodieObject eventArguments;

        public Entry(String target, TSLEvent event, GoodieObject eventArguments) {
            this.target = target;
            this.event = event;
            this.eventArguments = eventArguments;
        }

        public String getTarget() {
            return target;
        }

        public TSLEvent getEvent() {
            return event;
        }

        public GoodieObject getEventArguments() {
            return eventArguments;
        }

    }

}
