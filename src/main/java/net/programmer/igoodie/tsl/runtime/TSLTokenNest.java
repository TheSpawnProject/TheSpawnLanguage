package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.word.TSLToken;

import java.util.ArrayList;
import java.util.List;

public class TSLWordNest implements TSLClause {

    protected List<TSLToken> words;

    public TSLWordNest(List<TSLToken> words) {
        this.words = words;
    }

    public List<TSLToken> getWords() {
        return words;
    }

    public static class Builder {

        protected List<TSLToken> words = new ArrayList<>();

        public Builder push(TSLToken word) {
            this.words.add(word);
            return this;
        }

        public TSLWordNest build() {
            return new TSLWordNest(this.words);
        }

    }

}
