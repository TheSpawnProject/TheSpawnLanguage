package net.programmer.igoodie.runtime.predicate;

import net.programmer.igoodie.runtime.event.TSLEventContext;

import java.util.function.Predicate;

// TODO
public class TSLPredicate implements Predicate<TSLEventContext> {

    @Override
    public boolean test(TSLEventContext ctx) {
        return true; // <- TODO
    }

}
