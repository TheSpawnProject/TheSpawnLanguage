package net.programmer.igoodie.tsl.runtime;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.runtime.attribute.Attributable;
import net.programmer.igoodie.tsl.runtime.attribute.TSLAttributeList;
import net.programmer.igoodie.tsl.runtime.hook.HookList;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TSLRuleset implements Attributable {

    protected String name;
    protected File file;

    protected TSLAttributeList attributeList;

    protected List<TSLRule> rules;
    protected Map<String, TSLCaptureSnippet> captures;

    protected HookList hookList;

    public TSLRuleset() {
        this(null, null);
    }

    public TSLRuleset(String name, File file) {
        this.name = name;
        this.file = file;
        this.rules = new LinkedList<>();
        this.captures = new HashMap<>();
        this.attributeList = new TSLAttributeList();
        this.hookList = new HookList();
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public HookList getHookList() {
        return hookList;
    }

    public List<TSLTag> getTags() {
        return attributeList.getTags();
    }

    public TSLAttributeList getAttributeList() {
        return attributeList;
    }

    public Map<String, TSLCaptureSnippet> getCaptures() {
        return captures;
    }

    public TSLCaptureSnippet getCaptureSnippet(TSLCaptureCall captureCall) {
        return captures.get(captureCall.getCaptureName());
    }

    public List<TSLRule> getRules() {
        return rules;
    }

    @Override
    public JsonObject getAttributes() {
        return attributeList.getSquashedAttributes();
    }

    /* ----------------------------------------- */

    public void addTag(TSLTag tagDefinition, TSLString tagName, List<TSLString> tagArgs) {
        this.attributeList.addTag(tagDefinition, tagName, tagArgs);
    }

    public void addRule(TSLRule rule) {
        rule.setRuleset(this);
        this.rules.add(rule);
    }

    /* ----------------------------------------- */

}
