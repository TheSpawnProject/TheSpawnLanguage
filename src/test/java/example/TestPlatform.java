package example;

import example.action.PrintAction;
import net.programmer.igoodie.TSL;
import net.programmer.igoodie.event.TSLEventContext;
import net.programmer.igoodie.exception.TSLSyntaxException;
import net.programmer.igoodie.parser.TSLTokenizer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestPlatform {

    public static final TSL tsl = new TSL("TestPlatform");

    @BeforeAll()
    public static void registerEverything() {
        tsl.registerAction("PRINT", PrintAction::new);
    }

    @Test
    public void shouldPerformAction() throws TSLSyntaxException {
        TSLEventContext ctx = new TSLEventContext("Donation");
        ctx.getEventArgs().put("actor", "TestActor");
        ctx.getEventArgs().put("amount", 100);
        ctx.getEventArgs().put("currency", "USD");

        String actionScript = "PRINT Hey %There, ${actor}!% %How are you?%\n" +
                " DISPLAYING %Display this!%";

        List<String> actionArgs = TSLTokenizer.tokenizeWords(actionScript);

        PrintAction printAction = new PrintAction(actionArgs);

        printAction.perform(ctx);

        System.out.println("Done! Message: " + printAction.getMessage());
    }

}
