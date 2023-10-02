package net.programmer.igoodie.legacy.parser.lexer;

@Deprecated
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

    public static LexResult merge(LexResult... results) {
        LexResult mergedResult = new LexResult();

        for (LexResult result : results) {
            mergedResult.skipLine |= result.skipLine;
            mergedResult.pushToken |= result.pushToken;
            if (result.changeMode != null) {
                mergedResult.changeMode = result.changeMode;
            }
        }

        return mergedResult;
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
