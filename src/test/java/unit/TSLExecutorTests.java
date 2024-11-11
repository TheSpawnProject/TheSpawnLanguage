package unit;

import net.programmer.igoodie.tsl.runtime.executor.TSLExecutor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TSLExecutorTests {

    @Test
    public void shouldExecuteTask() {
        TSLExecutor executor = new TSLExecutor("iGoodie");

        await(executor.resolveProcedure(
                () -> {
                    Thread.sleep(2_000);
                    return 1;
                },
                () -> 2,
                () -> {
                    throw new RuntimeException("Wopsie");
                }
        ).whenComplete((result, e) -> {
            System.out.println("\nAccepting results: " + Thread.currentThread().getName());
            System.out.println("RESULT: " + result);
            System.out.println("EXCEPTION: " + e);
        }));
    }

    private void await(CompletableFuture<?> future) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException ignored) {}
    }

}
