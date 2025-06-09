package net.programmer.igoodie.tsl.util.structure;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Either<L, R> {

    private final L left;
    private final R right;
    private final boolean isRight;

    private Either(L left, R right, boolean isRight) {
        this.left = left;
        this.right = right;
        this.isRight = isRight;
    }

    public static <L, R> Either<L, R> left(L value) {
        return new Either<>(Objects.requireNonNull(value), null, false);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Either<>(null, Objects.requireNonNull(value), true);
    }

    public boolean isLeft() {
        return !isRight;
    }

    public boolean isRight() {
        return isRight;
    }

    public Optional<L> getLeft() {
        return isLeft() ? Optional.of(left) : Optional.empty();
    }

    public Optional<R> getRight() {
        return isRight() ? Optional.of(right) : Optional.empty();
    }

    public <T> T reduce(Function<L, T> reduceLeft, Function<R, T> reduceRight) {
        return this.isRight
                ? reduceRight.apply(this.right)
                : reduceLeft.apply(this.left);
    }

    public <NL, NR> Either<NL, NR> map(Function<L, NL> mapLeft, Function<R, NR> mapRight) {
        return this.isRight
                ? Either.right(mapRight.apply(this.right))
                : Either.left(mapLeft.apply(this.left));
    }

    public void consume(Consumer<L> consumeLeft, Consumer<R> consumeRight) {
        if (this.isRight) consumeRight.accept(this.right);
        else consumeLeft.accept(this.left);
    }

    public void ifLeft(Consumer<L> consumer) {
        if (isLeft()) consumer.accept(left);
    }

    public void ifRight(Consumer<R> consumer) {
        if (isRight()) consumer.accept(right);
    }

    @Override
    public String toString() {
        return isRight ? "Right(" + right + ")" : "Left(" + left + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Either<?, ?> other)) return false;
        return Objects.equals(this.left, other.left)
                && Objects.equals(this.right, other.right)
                && this.isRight == other.isRight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isRight);
    }
}
