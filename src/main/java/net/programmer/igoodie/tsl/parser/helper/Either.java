package net.programmer.igoodie.tsl.parser.helper;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public class Either<L, R> {

    protected @Nullable L left;
    protected @Nullable R right;

    private Either() {}

    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public Optional<L> left() {
        if (left == null) return Optional.empty();
        return Optional.of(left);
    }

    public Optional<R> right() {
        if (right == null) return Optional.empty();
        return Optional.of(right);
    }

    public Either<R, L> swap() {
        Either<R, L> either = new Either<>();
        either.right = left;
        either.left = right;
        return either;
    }

    public <T> T fold(Function<L, T> leftMapper, Function<R, T> rightMapper) {
        if (isLeft()) {
            return leftMapper.apply(left);
        } else {
            return rightMapper.apply(right);
        }
    }

    @Override
    public String toString() {
        if (isLeft())
            return "Left(" + left + ")";
        else
            return "Right(" + right + ")";
    }

    /* --------------------- */

    public static <L, R> Either<L, R> left(L left) {
        Either<L, R> either = new Either<>();
        either.left = left;
        return either;
    }

    public static <L, R> Either<L, R> right(R right) {
        Either<L, R> either = new Either<>();
        either.right = right;
        return either;
    }

}
