package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.AstUtils;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.stream.Stream;

public abstract class TSLToken implements TSLClause {

    protected List<Token> source;

    public TSLToken setSource(List<Token> source) {
        this.source = source;
        return this;
    }

    public TSLToken setSource(Stream<Token> source) {
        return this.setSource(source.toList());
    }

    public TSLToken setSource(ParseTree tree) {
        return this.setSource(AstUtils.getTerminalNodes(tree)
                .stream().map(TerminalNode::getSymbol)
                .toList());
    }

    public TSLToken setSource(Token... source) {
        return this.setSource(List.of(source));
    }

    public List<Token> getSource() {
        return source;
    }

    public abstract String evaluate(TSLEventContext ctx);

}
