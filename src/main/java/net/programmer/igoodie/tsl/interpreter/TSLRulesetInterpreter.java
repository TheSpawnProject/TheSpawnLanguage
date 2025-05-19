package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

public class TSLRulesetInterpreter extends TSLInterpreter<TSLRuleset, TSLParserImpl.TslRulesetContext>{

    @Override
    public TSLRuleset yieldValue(TSLParserImpl.TslRulesetContext tree) {
        return null;
    }

}
