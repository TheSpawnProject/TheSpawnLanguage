package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.tags.ImportTag;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLDocSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.attribute.ConstantAttributeGenerator;
import net.programmer.igoodie.tsl.runtime.hook.HookList;
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class TSLRuleset implements ConstantAttributeGenerator {

    protected @Nullable String name;
    protected @Nullable File file;
    protected List<TSLSnippet> snippets;

    protected TheSpawnLanguage tsl;

    protected GoodieObject tagAttributes;

    protected List<TSLRule> rules;
    protected List<TSLTagSnippet> tags;
    protected Map<Integer, TSLDocSnippet> tslDocs; // (begin_line_no --> tsl_doc)
    protected Map<String, TSLCaptureSnippet> captures; // (capture_name --> capture_snippet);

    protected Map<String, String> importedPlugins; // (alias_name --> plugin_id)
    protected List<TSLRuleset> importedRulesets;

    protected HookList hookList;

    public TSLRuleset(TheSpawnLanguage tsl) {
        this(tsl, null, null);
    }

    public TSLRuleset(TheSpawnLanguage tsl, @Nullable String name, @Nullable File file) {
        this.name = name;
        this.file = file;
        this.snippets = new LinkedList<>();
        this.tsl = tsl;
        this.tagAttributes = new GoodieObject();
        this.tags = new LinkedList<>();
        this.rules = new LinkedList<>();
        this.tslDocs = new HashMap<>();
        this.captures = new HashMap<>();
        this.importedPlugins = new HashMap<>();
        this.importedRulesets = new LinkedList<>();
        this.hookList = new HookList();
    }

    public @Nullable String getName() {
        return name;
    }

    public @Nullable File getFile() {
        return file;
    }

    public List<TSLSnippet> getSnippets() {
        return Collections.unmodifiableList(snippets);
    }

    public TheSpawnLanguage getTsl() {
        return tsl;
    }

    public HookList getHookList() {
        return hookList;
    }

    public List<TSLTagSnippet> getTags() {
        return tags;
    }

    public TSLDocSnippet getTSLDoc(int beginningLine) {
        return tslDocs.get(beginningLine);
    }

    public Map<String, TSLCaptureSnippet> getCaptures() {
        return Collections.unmodifiableMap(this.captures);
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
        return Collections.unmodifiableMap(this.importedPlugins);
    }

    public List<TSLRuleset> getImportedRulesets() {
        return Collections.unmodifiableList(this.importedRulesets);
    }

    /* ----------------------------------------- */

    @Override
    public @NotNull GoodieObject generateAttributes() {
        return this.tagAttributes;
    }

    /* ----------------------------------------- */

    public void addTSLDoc(TSLDocSnippet tslDocSnippet) {
        addTSLDoc(tslDocSnippet, false);
    }

    public void addTSLDoc(TSLDocSnippet tslDocSnippet, boolean outsource) {
        if (!outsource) snippets.add(tslDocSnippet);
        tslDocs.put(tslDocSnippet.getBeginningLine(), tslDocSnippet);
    }

    public void addTag(TSLTagSnippet tagSnippet) {
        addTag(tagSnippet, false);
    }

    public void addTag(TSLTagSnippet tagSnippet, boolean outsource) {
        TSLTag tagDefinition = tagSnippet.getTagDefinition();
        TSLPlainWord nameToken = tagSnippet.getTagNameToken();
        List<TSLToken> arguments = tagSnippet.getTagArgTokens();

        if (!outsource) snippets.add(tagSnippet);

        tags.add(tagSnippet);

        // Generate and merge tag attributes
        GoodieObject tagAttributes = tagDefinition.generateAttributes(new TSLContext(tsl), nameToken, arguments);
        this.tagAttributes = GoodieUtils.mergeOverriding(this.tagAttributes, tagAttributes);

        tagDefinition.onLoaded(this, tagSnippet);
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

    public void addPluginAlias(String alias, String pluginId) {
        importedPlugins.put(alias, pluginId);
    }

    public void importRuleset(TSLRuleset otherRuleset) {
        if (otherRuleset.getFile() == null) {
            throw new TSLRuntimeError("Cannot import ruleset without a file.");
        }

        for (TSLSnippet snippet : otherRuleset.getSnippets()) {
            if (snippet instanceof TSLDocSnippet) {
                addTSLDoc(((TSLDocSnippet) snippet), true);

            } else if (snippet instanceof TSLTagSnippet) {
                if (!(((TSLTagSnippet) snippet).getTagDefinition() instanceof ImportTag)) {
                    addTag(((TSLTagSnippet) snippet), true);
                }

            } else if (snippet instanceof TSLCaptureSnippet) {
                addCapture(((TSLCaptureSnippet) snippet), true);

            }
        }

        for (TSLRule rule : otherRuleset.rules) {
            addRule(rule, true);
        }

        importedRulesets.add(otherRuleset);
    }

    /* ----------------------------------------- */

    public boolean perform(TSLContext context) {
        if (context.getTsl() != this.tsl) {
            throw new TSLRuntimeError("Cannot perform() within different container");
        }

        context.setImportedPlugins(getImportedPlugins());
        context.setMessageToken(null);

        boolean performed = false;
        for (TSLRule rule : rules) {
            performed |= rule.perform(context);
        }
        return performed;
    }

}
