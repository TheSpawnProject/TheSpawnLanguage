package net.programmer.igoodie.tsl.definition;

public abstract class TSLTag extends TSLDefinition {

    public TSLTag(String name) {
        super(name);
    }

    public abstract boolean validateSyntax(String[] args);

    protected boolean isBoolean(String arg) {
        String argLowercase = arg.toLowerCase();
        return argLowercase.equals("true") || argLowercase.equals("false");
    }

    protected boolean isNumber(String arg) {
        try {
            Double.parseDouble(arg);
            return true;
        } catch (NumberFormatException e) { return false; }
    }

}
