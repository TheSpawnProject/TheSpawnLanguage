package net.programmer.igoodie.tsl.util;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class AstUtils {

    public static List<TerminalNode> getTerminalNodes(ParseTree tree) {
        List<TerminalNode> terminals = new ArrayList<>();
        getTerminalNodesImpl(tree, terminals);
        return terminals;
    }

    private static void getTerminalNodesImpl(ParseTree tree, List<TerminalNode> terminals) {
        if (tree instanceof TerminalNode) {
            terminals.add((TerminalNode) tree);
            return;
        }

        for (int i = 0; i < tree.getChildCount(); i++) {
            getTerminalNodesImpl(tree.getChild(i), terminals);
        }
    }

}
