package net.programmer.igoodie.tsl.parser.helper;

import net.programmer.igoodie.tsl.util.TSLReflectionUtils;

public class TextRange {

    protected final TextPosition beginPos;
    protected final TextPosition endPos;

    public TextRange(int beginLine, int beginCol, int endLine, int endCol) {
        this(new TextPosition(beginLine, beginCol), new TextPosition(endLine, endCol));
    }

    public TextRange(TextPosition beginPos, TextPosition endPos) {
        this.beginPos = beginPos;
        this.endPos = endPos;
    }

    public TextPosition getBeginPos() {
        return beginPos;
    }

    public TextPosition getEndPos() {
        return endPos;
    }

    public boolean containsPosition(TextPosition pos) {
        return !pos.comesBefore(beginPos) && !pos.comesAfter(endPos);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return TSLReflectionUtils.castToClass(TextRange.class, obj)
                .filter(that -> that.beginPos.equals(this.beginPos))
                .filter(that -> that.endPos.equals(this.endPos))
                .isPresent();
    }

    @Override
    public String toString() {
        return "TextRange{" +
                "beginPos=" + beginPos +
                ", endPos=" + endPos +
                '}';
    }

}
