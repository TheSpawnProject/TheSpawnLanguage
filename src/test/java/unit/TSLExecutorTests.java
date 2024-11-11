package unit;

import net.programmer.igoodie.tsl.runtime.executor.TSLExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletionException;

public class TSLExecutorTests {

    @Test
    public void shouldExecuteTask() {
        TSLExecutor executor = new TSLExecutor("iGoodie");

        long t0 = System.currentTimeMillis();

        Assertions.assertThrows(CompletionException.class, () -> executor.resolveProcedure(
                () -> {
                    Thread.sleep(2_000);
                    return 1;
                },
                () -> 2,
                () -> {
                    throw new IllegalStateException("Wopsie");
                }
        ).whenComplete((result, e) -> {
            System.out.println("\nAccepting results: " + Thread.currentThread().getName());
            System.out.println("RESULT: " + result);
            System.out.println("EXCEPTION: " + e);
        }).join());

        long t1 = System.currentTimeMillis();

        Assertions.assertTrue(t1 - t0 >= 2000, "Took at least 2 seconds");
    }

}
