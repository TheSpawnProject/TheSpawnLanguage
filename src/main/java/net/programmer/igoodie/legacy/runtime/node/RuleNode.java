package net.programmer.igoodie.legacy.runtime.node;

import net.programmer.igoodie.tsl.context.TSLContext;

public interface RuleNode {

    RuleNode getNext();

    boolean proceed(TSLContext context);

}
