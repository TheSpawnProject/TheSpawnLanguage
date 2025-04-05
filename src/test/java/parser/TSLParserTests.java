package parser;

import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

public class TSLParserTests {

    @Test
    public void shouldTokenizeSimple() {
        String script = "DROP %minecraft:|${\"diamond\"}|% ${10 * Math.random()} $smth";

        TSLParser parser = TSLParser.fromScript(script);

        for (TSLWord word : parser.parseWords()) {
            System.out.println(word);
        }

        System.out.println();

        System.out.println("TOKENS");
        for (Token token : parser.getTokens()) {
            System.out.println(token);
        }


//        TSLLexer lexer = new TSLLexer(CharStreams.fromString(script));
//        TSLParserImpl parserImpl = new TSLParserImpl(new CommonTokenStream(lexer));
//        ParseTree ruleAST = parserImpl.reactionRule();


//        TSLParserImplBaseVisitor<Object> visitor = new TSLParserImplBaseVisitor<>() {
//            @Override
//            public Object visitAction(TSLParserImpl.ActionContext ctx) {
//                System.out.println(ctx);
//                return super.visitAction(ctx);
//            }
//        };
//
//        visitor.visit(ruleAST);

//        System.out.println(ruleAST.toStringTree(parserImpl));
//
//        ruleAST.accept(new TSLParserImplBaseVisitor<>() {
//            @Override
//            public Object visit(ParseTree tree) {
//                System.out.println("Visiting " + tree);
//                return super.visit(tree);
//            }
//        });

//        System.out.println(ruleAST);
//
//        for (Token token : lexer.getAllTokens()) {
//            System.out.println(TSLLexer.VOCABULARY.getDisplayName(token.getType()) + " " + token);
//        }
    }

}
