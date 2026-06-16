package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;
import java.util.stream.Collectors;

public class TSLGroup extends TSLToken {

    protected final List<Token> args;

    public TSLGroup(List<Token> args) {
        this.args = args;
    }

    public List<Token> getArgs() {
        return args;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        return this.args.stream()
                .map(word -> word.evaluate(ctx))
                .collect(Collectors.joining());
    }

    @Override
    public String toDebugString() {
        return args.stream().map(TSLClause::toDebugString)
                .collect(Collectors.joining("", "%", "%"));
    }

    public static abstract class Token extends TSLToken {}

    public static class Expression extends TSLGroup.Token {

        protected final TSLToken token;

        public Expression(TSLToken token) {
            this.token = token;
        }

        public TSLToken getExpressionToken() {
            return token;
        }

        @Override
        public String evaluate(TSLEventContext ctx) {
            return this.token.evaluate(ctx);
        }

        @Override
        public String toDebugString() {
            return "| " + this.token.toDebugString() + " |";
        }

    }

    public static class StringContent extends TSLGroup.Token {

        protected final String content;

        public StringContent(String content) {
            this.content = content;
        }

        @Override
        public String evaluate(TSLEventContext ctx) {
            return this.content;
        }

        @Override
        public String toDebugString() {
            return this.content;
        }

    }

}
