package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.tsl.function.scope.JSScope;
import org.mozilla.javascript.*;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public final class JSContextManager {

    private static final Map<Thread, Context> THREAD_CONTEXTS = new WeakHashMap<>();
    private static final Map<Thread, Object> THREAD_LOCKS = new WeakHashMap<>();

    private JSContextManager() {}

    public static Context getThreadContext() {
        return THREAD_CONTEXTS.computeIfAbsent(Thread.currentThread(), thread -> {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            context.setMaximumInterpreterStackDepth(255);
            context.setLanguageVersion(Context.VERSION_ES6);
            Object lock = THREAD_LOCKS.computeIfAbsent(thread, t -> new Object());
            context.seal(lock);
            return context;
        });
    }

    private static void useUnsafeThreadContext(Consumer<Context> contextConsumer) {
        Context threadContext = getThreadContext();
        Object lock = THREAD_LOCKS.get(Thread.currentThread());
        threadContext.unseal(lock);
        contextConsumer.accept(threadContext);
        threadContext.seal(lock);
    }

    public static JSScope createGlobalScope(JSEngine jsEngine) {
        JSScope[] globalScope = {null};
        useUnsafeThreadContext(context -> {
            globalScope[0] = (JSScope) context.initSafeStandardObjects(
                    new JSScope(jsEngine), true);
        });
        return globalScope[0];
    }

    public static <T extends JSScope> JSScope cloneScope(T scope) {
        JSScope newScope = new JSScope(scope.getJsEngine());
        ScriptRuntime.setBuiltinProtoAndParent(newScope, scope, TopLevel.Builtins.Object);
        newScope.setPrototype(scope);
        newScope.setParentScope(null);
        newScope.defineProperty("exports", new NativeObject(), ScriptableObject.PERMANENT);
        return newScope;
    }

}
