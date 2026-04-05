package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImplBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

public abstract class TSLInterpreter<T, C extends ParseTree> extends TSLParserImplBaseVisitor<T> {

    protected C rootTree;

    public final T interpret(C tree) {
        this.rootTree = tree;
        this.visit(tree);
        return this.yieldValue(tree);
    }

    protected abstract T yieldValue(C tree);

}
