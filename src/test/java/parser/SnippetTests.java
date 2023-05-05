package parser;

import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureParameter;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SnippetTests {

    @Test
    @DisplayName("Snippets should be able to fill capture parameters with given arguments")
    public void testSnippetsFillingCaptureParameters() {
        TSLUnparsedSnippet snippet = new TSLUnparsedSnippet(
                new TSLSnippet.EntryListBuilder(ArrayList::new)
                        .addElement(Either.left(new TSLPlainWord(
                                new TextPosition(0, 0),
                                new TextPosition(0, 0),
                                "Hey")))
                        .addElement(Either.left(new TSLPlainWord(
                                new TextPosition(0, 0),
                                new TextPosition(0, 0),
                                "There")))
                        .addElement(Either.left(new TSLCaptureParameter(
                                new TextPosition(0, 0),
                                new TextPosition(0, 0),
                                "name")))
                        .build()
        );

        Assertions.assertTrue(snippet.getSnippetEntries().get(snippet.getSnippetEntries().size() - 1).left()
                .filter(t -> t instanceof TSLCaptureParameter)
                .filter(t -> ((TSLCaptureParameter) t).getParameterName().equals("name"))
                .isPresent());

        Map<String, TSLToken> arguments = new HashMap<>();
        arguments.put("name", new TSLPlainWord(
                new TextPosition(0, 0),
                new TextPosition(0, 0),
                "iGoodie"));

        snippet = snippet.fillCaptureParameters(arguments);

        Assertions.assertTrue(snippet.getSnippetEntries().get(snippet.getSnippetEntries().size() - 1).left()
                .filter(t -> t instanceof TSLPlainWord)
                .filter(t -> ((TSLPlainWord) t).getValue().equals("iGoodie"))
                .isPresent());
    }

}
