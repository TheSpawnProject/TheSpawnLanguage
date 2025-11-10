package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.AstUtils;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.stream.Stream;

public abstract class TSLWord implements TSLClause {

    protected List<Token> source;

    public TSLWord setSource(List<Token> source) {
        this.source = source;
        return this;
    }

    public TSLWord setSource(Stream<Token> source) {
        return this.setSource(source.toList());
    }

    public TSLWord setSource(ParseTree tree) {
        return this.setSource(AstUtils.getTerminalNodes(tree)
                .stream().map(TerminalNode::getSymbol)
                .toList());
    }

    public TSLWord setSource(Token... source) {
        return this.setSource(List.of(source));
    }

    public List<Token> getSource() {
        return source;
    }

    public abstract String evaluate(TSLEventContext ctx);

}
