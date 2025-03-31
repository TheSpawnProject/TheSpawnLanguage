package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.*;
import java.util.stream.Collectors;

// REFLECT %Target1, Target2, Target3% [ONLY]? <action>
// REFLECT <N> [ONLY]? <action>
// REFLECT * [ONLY]? <action>
public class ReflectAction extends TSLAction {

    protected static List<ReflectProvider> providers = new ArrayList<>();

    public static ReflectProvider registerProvider(ReflectProvider provider) {
        providers.add(provider);
        return provider;
    }

    protected boolean greedyTargets;
    protected int randomTargets;
    protected boolean targetsOnly;
    protected List<String> targets;
    protected TSLAction action;

    public ReflectAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);

        if (args.isEmpty()) {
            throw new TSLSyntaxException("Expected a target selector and an action.");
        }

        args = consumeTargetSelector(args);

        this.action = new TSLParser(platform, args).parseAction();
    }

    protected List<String> consumeTargetSelector(List<String> tokens) throws TSLSyntaxException {
        ListAccessor<String> tokenAccessor = ListAccessor.of(tokens);

        String targetExpr = tokenAccessor.get(0)
                .orElseThrow(() -> new TSLSyntaxException("Expected a target selector"));

        if (targetExpr.equals("*")) {
            this.greedyTargets = true;
        } else try {
            this.randomTargets = Integer.parseInt(targetExpr);
            if (this.randomTargets <= 0) {
                throw new TSLSyntaxException("Expected a positive integer, instead found -> {}", this.randomTargets);
            }
        } catch (NumberFormatException ignored) {
            this.targets = Arrays.asList(targetExpr.split(",\\s*"));
        }

        this.targetsOnly = tokenAccessor.get(1)
                .map(s -> s.equalsIgnoreCase("ONLY"))
                .orElse(false);

        return this.targetsOnly
                ? tokens.subList(2, tokens.size())
                : tokens.subList(1, tokens.size());
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        Set<String> targets;

        String originalTarget = ctx.getTarget();

        if (this.greedyTargets) {
            targets = providers.stream()
                    .map(provider -> provider.getAllEventTargets(originalTarget))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        } else if (this.randomTargets > 0) {
            targets = providers.stream()
                    .map(provider -> provider.getRandomEventTargets(originalTarget, this.randomTargets))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        } else {
            targets = providers.stream()
                    .map(provider -> provider.getEventTargets(originalTarget, this.targets))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }

        boolean success = false;

        if (!this.targetsOnly) {
            success = this.action.perform(ctx);
        }

        targets.forEach(target -> {
            TSLEventContext targetCtx = ctx.copy();
            targetCtx.setTarget(target);
            providers.forEach(provider -> provider.onEventReflection(
                    originalTarget,
                    this.action,
                    targetCtx));
        });

        return success;
    }

    public interface ReflectProvider {

        List<String> getEventTargets(String originalTarget, List<String> targets);

        List<String> getAllEventTargets(String originalTarget);

        List<String> getRandomEventTargets(String originalTarget, int count);

        void onEventReflection(String originalTarget, TSLAction action, TSLEventContext ctx);

        default void unsubscribe() {
            ReflectAction.providers.removeIf(provider -> provider == this);
        }

    }

}
