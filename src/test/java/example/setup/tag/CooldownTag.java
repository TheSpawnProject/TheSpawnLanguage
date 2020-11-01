package example.setup.tag;

import net.programmer.igoodie.tsl.definition.TSLTag;

public class CooldownTag extends TSLTag {

    public static final CooldownTag INSTANCE = new CooldownTag();

    private CooldownTag() {
        super("COOLDOWN");
    }

    @Override
    public boolean validateSyntax(String[] args) {
        if (args.length != 1) return false;
        return isNumber(args[0]);
    }

}