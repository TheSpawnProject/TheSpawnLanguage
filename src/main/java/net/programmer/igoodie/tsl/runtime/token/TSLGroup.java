package net.programmer.igoodie.tsl.runtime.word;

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

    public static abstract class Token extends TSLToken {}

    public static class Expression extends Token {

        protected final TSLToken word;

        public Expression(TSLToken word) {
            this.word = word;
        }

        public TSLToken getExpressionWord() {
            return word;
        }

        @Override
        public String evaluate(TSLEventContext ctx) {
            return this.word.evaluate(ctx);
        }

    }

    public static class StringContent extends Token {

        protected final String content;

        public StringContent(String content) {
            this.content = content;
        }

        @Override
        public String evaluate(TSLEventContext ctx) {
            return this.content;
        }

    }

}
