package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLDocSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
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
    protected Map<Integer, TSLDocSnippet> tslDocs;
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
        this.tslDocs = new HashMap<>();
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

    public TSLDocSnippet getTSLDoc(int beginningLine) {
        return tslDocs.get(beginningLine);
    }

    public Map<String, TSLCaptureSnippet> getCaptures() {
        return Collections.unmodifiableMap(captures);
    }

    public TSLCaptureSnippet getCaptureSnippet(TSLCaptureCall captureCall) {
        TSLCaptureSnippet referredCapture = getCaptureSnippet(captureCall.getCaptureName());
        if (referredCapture == null) {
            throw new TSLRuntimeError("No capture was defined with name -> " + captureCall.getCaptureName(), captureCall);
        }
        return referredCapture;
    }

    public TSLCaptureSnippet getCaptureSnippet(String captureName) {
        return captures.get(captureName);
    }

    public List<TSLRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    @Override
    public GoodieObject getAttributes() {
        return attributeList.getSquashedAttributes();
    }

    /* ----------------------------------------- */

    public void addTSLDoc(TSLDocSnippet tslDocSnippet) {
        snippets.add(tslDocSnippet);
        tslDocs.put(tslDocSnippet.getBeginningLine(), tslDocSnippet);
    }

    public void addTag(TSLTagSnippet tagSnippet) {
        snippets.add(tagSnippet);
        this.attributeList.addTag(tagSnippet.getTagDefinition(),
                tagSnippet.getTagNameToken(),
                tagSnippet.getTagArgTokens());
    }

    public void addCapture(TSLCaptureSnippet captureSnippet) {
        snippets.add(captureSnippet);
        captures.put(captureSnippet.getName(), captureSnippet);
    }

    public void addRule(TSLRule rule) {
        snippets.add(rule.getSnippet());
        rule.setAssociatedRuleset(this);
        this.rules.add(rule);
    }

    /* ----------------------------------------- */

    public boolean perform(TSLContext context) {
        context.setMessageToken(null);

        boolean performed = false;
        for (TSLRule rule : rules) {
            context.setRule(rule);
            performed |= rule.perform(context);
        }
        return performed;
    }

}
