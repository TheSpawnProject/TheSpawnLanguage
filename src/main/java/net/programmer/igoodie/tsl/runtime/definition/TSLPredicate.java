package net.programmer.igoodie.tsl.runtime.definition;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

public abstract class TSLPredicate {

    public void checkEventCompatibility(TSLEvent event) {}

    public abstract boolean test(TSLEventContext ctx);

    // WITH ${event.field >= 999}
    public static class ExpressionPredicate extends TSLPredicate {

        protected final TSLExpression expression;

        public ExpressionPredicate(TSLExpression expression) {
            this.expression = expression;
        }

        @Override
        public boolean test(TSLEventContext ctx) {
            return this.expression.evaluate(ctx).equalsIgnoreCase("true");
        }

    }

    // WITH field IS %Something something%
    public static class BinaryOperationPredicate extends TSLPredicate {

        protected final String fieldName;
        protected final Operator operator;
        protected final TSLToken rightHand;

        public enum Operator {
            EQ("=", (a, b) -> {
                if (a instanceof Number)
                    return ((Number) a).doubleValue() == Double.parseDouble(b);
                return a.equals(b);
            }),
            GT(">", (a, b) -> {
                if (a instanceof Number)
                    return ((Number) a).doubleValue() > Double.parseDouble(b);
                return false;
            }),
            GTE(">=", (a, b) -> {
                if (a instanceof Number)
                    return ((Number) a).doubleValue() >= Double.parseDouble(b);
                return false;
            }),
            LT("<", (a, b) -> {
                if (a instanceof Number)
                    return ((Number) a).doubleValue() < Double.parseDouble(b);
                return false;
            }),
            LTE("<=", (a, b) -> {
                if (a instanceof Number)
                    return ((Number) a).doubleValue() <= Double.parseDouble(b);
                return false;
            }),
            IS("IS", (a, b) -> a.toString().equalsIgnoreCase(b)),
            PREFIX("PREFIX", (a, b) -> {
                if (a instanceof String) {
                    String a1 = ((String) a).trim().toLowerCase();
                    String b1 = ((String) b).trim().toLowerCase();
                    return a1.startsWith(b1);
                }
                return false;
            }),
            POSTFIX("POSTFIX", (a, b) -> {
                if (a instanceof String) {
                    String a1 = ((String) a).trim().toLowerCase();
                    String b1 = ((String) b).trim().toLowerCase();
                    return a1.endsWith(b1);
                }
                return false;
            }),
            CONTAINS("CONTAINS", (a, b) -> {
                if (a instanceof Set<?>) {
                    return ((Set<?>) a).contains(b);
                }

                if (a instanceof String) {
                    return ((String) a).contains(b);
                }

                return false;
            });

            final String symbol;
            final BiPredicate<Object, String> comparer;

            Operator(String symbol, BiPredicate<Object, String> comparer) {
                this.symbol = symbol;
                this.comparer = comparer;
            }

            public boolean compare(Object a, String b) {
                return this.comparer.test(a, b);
            }

            public static Optional<Operator> bySymbol(String symbol) {
                for (Operator operator : Operator.values()) {
                    if (operator.symbol.equalsIgnoreCase(symbol))
                        return Optional.of(operator);
                }
                return Optional.empty();
            }
        }

        public BinaryOperationPredicate(String fieldName, Operator operator, TSLToken rightHand) {
            this.fieldName = fieldName;
            this.operator = operator;
            this.rightHand = rightHand;
        }

        @Override
        public void checkEventCompatibility(TSLEvent event) {
            TSLEvent.Property<?> propertyType = event.getPropertyType(this.fieldName);

            if (propertyType == null) {
                throw new TSLSyntaxException("{} field is not compatible with event -> {}", this.fieldName, event.name);
            }
        }

        @Override
        public boolean test(TSLEventContext ctx) {
            TSLRule rule = ctx.getPerformingRule().orElseThrow();
            TSLEvent event = rule.getEvent();

            TSLEvent.Property<?> propertyType = event.getPropertyType(this.fieldName);
            Optional<?> eventArgOpt = propertyType.read(ctx.getEventArgs());
            if (eventArgOpt.isEmpty()) return false;

            Object fieldValue = eventArgOpt.get();
            String value = this.rightHand.evaluate(ctx);

            return this.operator.compare(fieldValue, value);
        }

    }

}
