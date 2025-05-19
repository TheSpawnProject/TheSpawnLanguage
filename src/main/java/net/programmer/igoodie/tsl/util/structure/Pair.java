package net.programmer.igoodie.tsl.util.structure;

public class Pair<L, R> {

    protected L left;
    protected R right;

    public Pair() {}

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public Pair<L, R> setLeft(L left) {
        this.left = left;
        return this;
    }

    public Pair<L, R> setRight(R right) {
        this.right = right;
        return this;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    public <E extends Exception> void using(PairConsumer<L, R, E> consumer) throws E {
        consumer.consume(this.left, this.right);
    }

    public interface PairConsumer<L, R, E extends Exception> {
        void consume(L left, R right) throws E;
    }

}
