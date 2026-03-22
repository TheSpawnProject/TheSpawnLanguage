package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.word.TSLWord;

import java.util.ArrayList;
import java.util.List;

public class TSLWordNest implements TSLClause {

    protected List<TSLWord> words;

    public TSLWordNest(List<TSLWord> words) {
        this.words = words;
    }

    public List<TSLWord> getWords() {
        return words;
    }

    public static class Builder {

        protected List<TSLWord> words = new ArrayList<>();

        public Builder push(TSLWord word) {
            this.words.add(word);
            return this;
        }

        public TSLWordNest build() {
            return new TSLWordNest(this.words);
        }

    }

}
