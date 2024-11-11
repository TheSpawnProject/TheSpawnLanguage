package example;

import example.action.PrintAction;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.CharStream;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import net.programmer.igoodie.tsl.std.comparator.GtComparator;
import net.programmer.igoodie.tsl.std.comparator.GteComparator;
import net.programmer.igoodie.tsl.std.comparator.InRangeComparator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TwitchSpawnTest {

    @Test
    public void shouldParseDefaultRuleset() throws TSLSyntaxException, IOException {
        String script = "# Example TSL (TwitchSpawn Language) Rule Set\n" +
                "# Feel free to edit this one, or create your own rule set!\n" +
                "# For full documentation about TSL Language\n" +
                "# See https://igoodie.gitbook.io/twitchspawn/\n" +
                "\n" +
                "# Donation Rules\n" +
                "DROP minecraft:stick 2\n" +
                " ON Donation\n" +
                " WITH amount IN RANGE [0,20]\n" +
                "\n" +
                "DROP minecraft:diamond_block 1\n" +
                " ON Donation\n" +
                " WITH amount IN RANGE [21,999]\n" +
                "\n" +
                "EXECUTE %/gamerule keepInventory true%\n" +
                " ON Donation\n" +
                " WITH amount >= 1000\n" +
                "\n" +
                "# Follow Rules\n" +
                "DROP %diamond{display:{Name:\"\\\"99.99\\% Percent Real Diamond\\\"\"}}% 2\n" +
                " ON Twitch Follow\n" +
                "\n" +
                "# Host Rules\n" +
                "EXECUTE %/give @s stick 1%\n" +
                " ON Twitch Host\n" +
                " WITH viewers >= 10\n" +
                "\n" +
                "# Raid Rules\n" +
                "EXECUTE %/weather thunder%\n" +
                " ON Twitch Raid\n" +
                " WITH raiders > 100\n";

        TSLPlatform platform = new TSLPlatform("Test Platform", 1.0f);

        platform.registerAction("DROP", PrintAction::new);
        platform.registerAction("EXECUTE", PrintAction::new);

        platform.registerEvent(new TSLEvent("Donation")
                .addPropertyType(TSLEvent.PropertyBuilder.DOUBLE.create("amount"))
        );
        platform.registerEvent(new TSLEvent("Twitch Follow"));
        platform.registerEvent(new TSLEvent("Twitch Host")
                .addPropertyType(TSLEvent.PropertyBuilder.INT.create("viewers"))
        );
        platform.registerEvent(new TSLEvent("Twitch Raid")
                .addPropertyType(TSLEvent.PropertyBuilder.INT.create("raiders"))
        );

        platform.registerComparator("IN RANGE", InRangeComparator::new);
        platform.registerComparator(">=", GteComparator::new);
        platform.registerComparator(">", GtComparator::new);

        TSLLexer lexer = new TSLLexer(CharStream.fromString(script));
        TSLParser parser = new TSLParser(platform, "Player:iGoodie", lexer.tokenize());
        Assertions.assertDoesNotThrow(parser::parse);
    }

}
