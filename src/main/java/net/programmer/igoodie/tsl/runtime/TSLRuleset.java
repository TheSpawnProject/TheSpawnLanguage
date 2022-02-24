package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLDocSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
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

    protected Map<String, String> importedPlugins;
    protected List<String> importedRulesets;

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
        this.importedPlugins = new HashMap<>();
        this.importedRulesets = new LinkedList<>();
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

    public Map<String, String> getImportedPlugins() {
        return importedPlugins;
    }

    public List<String> getImportedRulesets() {
        return importedRulesets;
    }

    @Override
    public GoodieObject getAttributes() {
        return attributeList.getSquashedAttributes();
    }

    /* ----------------------------------------- */

    public void addTSLDoc(TSLDocSnippet tslDocSnippet) {
        addTSLDoc(tslDocSnippet, false);
    }

    public void addTSLDoc(TSLDocSnippet tslDocSnippet, boolean outsource) {
        if (!outsource) snippets.add(tslDocSnippet);
        tslDocs.put(tslDocSnippet.getBeginningLine(), tslDocSnippet);
    }

    public void addTag(TSLTagSnippet tagSnippet, TheSpawnLanguage language) {
        addTag(tagSnippet, language, false);
    }

    public void addTag(TSLTagSnippet tagSnippet, TheSpawnLanguage language, boolean outsource) {
        TSLTag tagDefinition = tagSnippet.getTagDefinition();
        TSLPlainWord nameToken = tagSnippet.getTagNameToken();
        List<TSLToken> arguments = tagSnippet.getTagArgTokens();

        if (!outsource) snippets.add(tagSnippet);

        this.attributeList.loadTag(new TSLContext(language),
                tagDefinition,
                nameToken,
                arguments
        );

        tagDefinition.onLoaded(language, this, tagSnippet);
    }

    public void addCapture(TSLCaptureSnippet captureSnippet) {
        addCapture(captureSnippet, false);
    }

    public void addCapture(TSLCaptureSnippet captureSnippet, boolean outsource) {
        if (!outsource) snippets.add(captureSnippet);

        if (captures.containsKey(captureSnippet.getName())) {
            throw new TSLSyntaxError("$" + captureSnippet.getName() + " is already defined", captureSnippet);
        }

        captures.put(captureSnippet.getName(), captureSnippet);
    }

    public void addRule(TSLRule rule) {
        addRule(rule, false);
    }

    public void addRule(TSLRule rule, boolean outsource) {
        if (!outsource) snippets.add(rule.getSnippet());
        rule.setAssociatedRuleset(this);
        this.rules.add(rule);
    }

    public void importRuleset(TSLRuleset otherRuleset, TheSpawnLanguage language) {
        for (TSLSnippet snippet : otherRuleset.getSnippets()) {
            if (snippet instanceof TSLDocSnippet) {
                addTSLDoc(((TSLDocSnippet) snippet), true);

            } else if (snippet instanceof TSLTagSnippet) {
                addTag(((TSLTagSnippet) snippet), language, true);

            } else if (snippet instanceof TSLCaptureSnippet) {
                addCapture(((TSLCaptureSnippet) snippet), true);

            }
        }

        for (TSLRule rule : otherRuleset.rules) {
            addRule(rule, true);
        }

        importedRulesets.add(otherRuleset.getFile().getAbsolutePath());
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
