package net.programmer.igoodie.tsl.runtime.node_old;

import net.programmer.igoodie.tsl.context.TSLContext;

public interface RuleNode {

    RuleNode getNext();

    boolean proceed(TSLContext context);

}
