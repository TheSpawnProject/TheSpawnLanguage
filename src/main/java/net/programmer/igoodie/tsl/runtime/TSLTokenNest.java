package net.programmer.igoodie.tsl.runtime;

import java.util.ArrayList;
import java.util.List;

public class TSLTokenNest implements TSLClause {

    protected List<TSLClause> clauses;

    public TSLTokenNest(List<TSLClause> clauses) {
        this.clauses = clauses;
    }

    public List<TSLClause> getClauses() {
        return clauses;
    }

    public static class Builder {

        protected List<TSLClause> clauses = new ArrayList<>();

        public Builder push(TSLClause clause) {
            this.clauses.add(clause);
            return this;
        }

        public TSLTokenNest build() {
            return new TSLTokenNest(this.clauses);
        }

    }

}
