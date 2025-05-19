package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImplBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

public abstract class TSLInterpreter<T, C extends ParseTree> extends TSLParserImplBaseVisitor<T> {

    public final T interpret(C tree){
        this.visit(tree);
        return this.yieldValue(tree);
    }

    public abstract T yieldValue(C tree);

}
