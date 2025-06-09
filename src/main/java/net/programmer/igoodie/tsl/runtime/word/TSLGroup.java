package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TSLGroup extends TSLWord {

    protected final List<TSLGroup.Word> args;

    public TSLGroup(List<TSLGroup.Word> args) {
        this.args = args;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        return this.args.stream()
                .map(word -> word.evaluate(ctx))
                .collect(Collectors.joining());
    }

    public TSLGroup resolveFromCapture(Map<String, TSLWord> arguments) {
        List<Word> resolvedGroupWords = this.args.stream().map(word -> {
            if (word instanceof Expression expr) {
                if (expr.word instanceof TSLPlaceholder placeholder) {
                    TSLWord argument = arguments.get(placeholder.parameterName);
                    return new Expression(argument);
                }
            }

            return word;
        }).toList();

        return new TSLGroup(resolvedGroupWords);
    }

    public static abstract class Word extends TSLWord {
    }

    public static class Expression extends Word {

        protected final TSLWord word;

        public Expression(TSLWord word) {
            this.word = word;
        }

        @Override
        public String evaluate(TSLEventContext ctx) {
            return this.word.evaluate(ctx);
        }

    }

    public static class StringContent extends Word {

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
