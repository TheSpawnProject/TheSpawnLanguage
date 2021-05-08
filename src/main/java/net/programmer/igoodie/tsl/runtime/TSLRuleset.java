package net.programmer.igoodie.tsl.runtime;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.runtime.attribute.Attributable;
import net.programmer.igoodie.tsl.runtime.attribute.TSLAttributeList;
import net.programmer.igoodie.tsl.runtime.hook.HookList;

import java.io.File;
import java.util.*;

public class TSLRuleset implements Attributable {

    protected String name;
    protected File file;
    protected List<TSLSnippet> snippets;

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
        this.snippets = new LinkedList<>();
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

    public List<TSLSnippet> getSnippets() {
        return Collections.unmodifiableList(snippets);
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
        return Collections.unmodifiableMap(captures);
    }

    public TSLCaptureSnippet getCaptureSnippet(TSLCaptureCall captureCall) {
        return captures.get(captureCall.getCaptureName());
    }

    public List<TSLRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    @Override
    public JsonObject getAttributes() {
        return attributeList.getSquashedAttributes();
    }

    /* ----------------------------------------- */

    public void addTag(TSLTagSnippet tagSnippet) {
        snippets.add(tagSnippet);
        this.attributeList.addTag(tagSnippet.getTag(),
                tagSnippet.getTagName(),
                tagSnippet.getTagArguments());
    }

    public void addCapture(TSLCaptureSnippet captureSnippet) {
        snippets.add(captureSnippet);
        captures.put(captureSnippet.getName(), captureSnippet);
    }

    public void addRule(TSLRule rule) {
        rule.setRuleset(this);
        this.rules.add(rule);
    }

    /* ----------------------------------------- */

}
