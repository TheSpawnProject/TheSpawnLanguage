package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TSLLexer {

    private List<String> lines;
    private List<TSLSnippet> snippets;

    private boolean canReadTags;
    private TSLTokenizer tokenizer;
    private int tokenBeginLine, tokenBeginChar;
    private StringBuilder characterBuffer;
    private TSLSnippet snippetCursor;

    public TSLLexer(String tsl) {
        this(Arrays.asList(tsl.split("\\r?\\n")));
    }

    public TSLLexer(List<String> lines) {
        this.lines = lines;
        this.canReadTags = true;
        this.snippets = new LinkedList<>();
        this.tokenizer = new TSLTokenizer();
        this.characterBuffer = new StringBuilder();
        this.snippetCursor = new TSLSnippet();
        this.tokenBeginLine = -1;
        this.tokenBeginChar = -1;
    }

    public List<TSLSnippet> getSnippets() {
        return snippets;
    }

    public void lex() {
        boolean inGroup = false;
        boolean inExpression = false;
        boolean inComment = false;
        boolean inNest = false; // TODO: <-- Implement Nesting! (?)
        boolean escaping = false;

        lineLoop:
        for (int lineNo = 0; lineNo < lines.size(); lineNo++) {
            String line = lines.get(lineNo);
            char[] chars = line.toCharArray();

            if (line.trim().isEmpty()) {
                pushSnippet();
                continue;
            }

            pushToken();

            for (int charNo = 0; charNo < chars.length; charNo++) {
                char previousCharacter = charNo == 0 ? 0 : chars[charNo - 1];
                char character = chars[charNo];
                char nextCharacter = charNo == chars.length - 1 ? 0 : chars[charNo + 1];

                if (character == '#') {
                    if (nextCharacter == '!') { // #!
                        if (characterBuffer.length() != 0) {
                            throw new TSLSyntaxError("Unexpected ruleset tag start", lineNo, charNo);

                        } else {
                            pushCharacters("#!", lineNo, charNo);
                            pushToken();
                            charNo++; // Skip next '!' character
                            continue;
                        }

                    } else if (nextCharacter == '*') { // #*
                        inComment = true;
                        continue;

                    } else if (previousCharacter == '*') { // *#
                        inComment = false;
                        continue;

                    } else { // #
                        if (!inGroup && !inExpression && !escaping) {
                            continue lineLoop;
                        }
                    }
                }

                if (inComment) continue;

                if (character == '\\') {
                    if (escaping) {
                        pushCharacters("\\\\", lineNo, charNo);
                        escaping = false;

                    } else if (!inExpression) {
                        escaping = true;
                    }

                    continue;
                }

                if (character == ' ') {
                    if (escaping) {
                        if (characterBuffer.length() != 0) {
                            pushCharacter(' ', lineNo, charNo);
                        }
                        escaping = false;
                        continue;

                    } else if (!inGroup && !inExpression) {
                        if (characterBuffer.length() != 0) {
                            pushToken();
                        }
                        continue;
                    }
                }

                if (character == '%') {
                    if (escaping) {
                        pushCharacter('%', lineNo, charNo);
                        escaping = false;
                        continue;

                    } else if (inGroup) {
                        pushCharacter(character, lineNo, charNo);
                        inGroup = false;
                        continue;

                    } else if (!inExpression) {
                        pushCharacter(character, lineNo, charNo);
                        inGroup = true;
                        continue;
                    }
                }

                if (character == '$') {
                    if (nextCharacter == '{') {
                        if (inExpression) {
                            throw new TSLSyntaxError("Unexpected expression start", lineNo, charNo);

                        } else if (!inGroup) {
                            pushCharacters("${", lineNo, charNo);
                            inExpression = true;
                            charNo++; // Skip '{' char
                            continue;
                        }
                    }
                }

                if (character == '}') {
                    if (inExpression && !inGroup) {
                        pushCharacter('}', lineNo, charNo);
                        pushToken();
                        inExpression = false;
                        continue;
                    }
                }

                pushCharacter(character, lineNo, charNo);
            }
        }
    }

    private void pushCharacter(char character, int line, int characterNo) {
        this.characterBuffer.append(character);
        if (tokenBeginLine == -1) this.tokenBeginLine = line;
        if (tokenBeginChar == -1) this.tokenBeginChar = characterNo;
    }

    private void pushCharacters(String sequence, int line, int characterNo) {
        char[] charArray = sequence.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char character = charArray[i];
            pushCharacter(character, line, characterNo + i);
        }
    }

    private void pushToken() {
        if (this.characterBuffer.length() != 0) {
            String text = this.characterBuffer.toString();
            TSLToken token = tokenizer.tokenize(text, tokenBeginLine, tokenBeginChar);

            if (snippetCursor.getTokens().size() == 0) { // Inserting the very first token
                if (token instanceof TSLSymbol && ((TSLSymbol) token).getType() == TSLSymbol.Type.RULESET_TAG_BEGIN) {
                    snippetCursor.setType(TSLSnippet.Type.TAG);
                }
            }

            if (snippetCursor.getTokens().size() == 1) { // Inserting the second token
                TSLToken firstToken = snippetCursor.getTokens().get(0);
                if (firstToken instanceof TSLCaptureCall) {
                    if (token instanceof TSLSymbol && ((TSLSymbol) token).getType() == TSLSymbol.Type.CAPTURE_DECLARATION) {
                        snippetCursor.setType(TSLSnippet.Type.CAPTURE);
                    }
                }
            }

            snippetCursor.pushToken(token);
            characterBuffer = new StringBuilder();

        }

        tokenBeginLine = -1;
        tokenBeginChar = -1;
    }

    private void pushSnippet() {
        if (characterBuffer.length() != 0) {
            pushToken();
        }
        if (snippetCursor.getType() == null) {
            snippetCursor.setType(TSLSnippet.Type.RULE);
        }
        snippets.add(snippetCursor);
        snippetCursor = new TSLSnippet();
    }

}
