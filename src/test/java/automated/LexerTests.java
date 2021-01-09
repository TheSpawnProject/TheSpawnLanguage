package automated;

import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLSnippet;
import org.junit.jupiter.api.Test;

public class LexerTests {

    @Test
    public void foo() {
        TSLLexer lexer = new TSLLexer("#! COOLDOWN 1000\n" +
                "\n" +
                "$name = diamond\n" +
                "\n" +
                "PRINT %Hey dude! This message is evaluated @ ${_currentUnix()}%\n" +
                " ON Alert Event\n" +
                " WITH time = 12345\n" +
                "\n" +
                "@suppressNotifications\n" +
                "PRINT ${_maximumOf(10,20) + Math.random()}\n" +
                " ON Alert Event\n" +
                "\n" +
                "# Eyyy, a comment here!\n" +
                "\n" +
                "#*\n" +
                " Eyyyy, yet another comment here!\n" +
                "*#\n" +
                "\n" +
                "@notificationVolume(2f,3f)\n" +
                "EITHER\n" +
                " DROP $name\n" +
                " OR\n" +
                " (EITHER DROP apple OR DROP string)\n" +
                " OR\n" +
                " DROP stick\n" +
                " ON Alert Event");

        lexer.lex();

        for (TSLSnippet snippet : lexer.getSnippets()) {
            System.out.println(snippet);
        }
    }

}
