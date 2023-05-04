package net.programmer.igoodie.tsl.parser.helper;

import net.programmer.igoodie.tsl.util.TSLReflectionUtils;

public class TextPosition {

    /**
     * 0-indexed value
     */
    protected int line, col;

    public TextPosition(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return TSLReflectionUtils.castToClass(TextPosition.class, obj)
                .filter(that -> that.line == this.line)
                .filter(that -> that.col == this.col)
                .isPresent();
    }

    public boolean comesBefore(TextPosition target) {
        if (this.line > target.line) return false;
        return this.line < target.line
                || this.col < target.col;
    }

    public boolean comesAfter(TextPosition target) {
        if (this.line < target.line) return false;
        return this.line > target.line
                || this.col > target.col;
    }

    @Override
    public String toString() {
        return String.format("Line %d, col %d",
                line + 1, col + 1);
    }

}
