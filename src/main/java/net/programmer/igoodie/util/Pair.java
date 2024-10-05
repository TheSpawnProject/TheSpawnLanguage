package net.programmer.igoodie.util;

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

}
