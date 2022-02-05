package net.programmer.igoodie.tsl.parser.lexer;

class LexResult {

    private boolean skipLine = false;
    private boolean pushToken = false;
    private LexerMode changeMode = null;

    public LexResult revertChangeMode() {
        this.changeMode = null;
        return this;
    }

    public LexResult revertPushToken() {
        this.pushToken = false;
        return this;
    }

    public boolean shouldSkipLine() {
        return skipLine;
    }

    public boolean shouldPushToken() {
        return pushToken;
    }

    public LexerMode getChangeMode() {
        return changeMode;
    }

    /* -------------------- */

    public static LexResult merge(LexResult a, LexResult b) {
        LexResult result = new LexResult();
        result.skipLine = a.skipLine || b.skipLine;
        result.pushToken = a.pushToken || b.pushToken;
        result.changeMode = a.changeMode != null ? a.changeMode : b.changeMode;
        return result;
    }

    public static LexResult nothing() {
        return new LexResult();
    }

    public static LexResult skipLine() {
        LexResult result = new LexResult();
        result.skipLine = true;
        return result;
    }

    public static LexResult pushToken() {
        LexResult result = new LexResult();
        result.pushToken = true;
        return result;
    }

    public static LexResult changeMode(LexerMode mode) {
        LexResult result = new LexResult();
        result.changeMode = mode;
        return result;
    }

}
