package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
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
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class TSLRuleset implements ConstantAttributeGenerator {

    protected @Nullable String name;
    protected @Nullable File file;
    protected @NotNull final TheSpawnLanguage tsl;
    protected List<TSLSnippet> snippets;

    protected GoodieObject tagAttributes;

    protected List<TSLRule> rules;
    protected List<TSLTagSnippet> tagSnippets;
    protected Map<Integer, TSLDocSnippet> tslDocSnippets; // (begin_line_no --> tsl_doc)
    protected Map<String, TSLCaptureSnippet> captureSnippets; // (capture_name --> capture_snippet);

    protected Map<String, String> importedPlugins; // (alias_name --> plugin_id)
    protected List<TSLRuleset> importedRulesets;

    public TSLRuleset(@NotNull TheSpawnLanguage tsl) {
        this(tsl, null, null);
    }

    public TSLRuleset(@NotNull TheSpawnLanguage tsl, @Nullable String name, @Nullable File file) {
        this.name = name;
        this.file = file;
        this.tsl = tsl;
        this.snippets = new LinkedList<>();

        this.tagAttributes = new GoodieObject();

        this.rules = new LinkedList<>();
        this.tagSnippets = new LinkedList<>();
        this.tslDocSnippets = new HashMap<>();
        this.captureSnippets = new HashMap<>();

        this.importedPlugins = new HashMap<>();
        this.importedRulesets = new LinkedList<>();
    }

    /* ------------------------ */

    public @Nullable String getName() {
        return name;
    }

    public @Nullable File getFile() {
        return file;
    }

    public @NotNull TheSpawnLanguage getTsl() {
        return tsl;
    }

    public List<TSLSnippet> getSnippets() {
        return Collections.unmodifiableList(snippets);
    }

    public Map<String, String> getImportedPlugins() {
        return Collections.unmodifiableMap(this.importedPlugins);
    }

    public List<TSLRuleset> getImportedRulesets() {
        return Collections.unmodifiableList(this.importedRulesets);
    }

    /* ------------------------ */

    public Map<Integer, TSLDocSnippet> getTslDocSnippets() {
        return Collections.unmodifiableMap(tslDocSnippets);
    }

    public TSLDocSnippet getTSLDocSnippet(int beginningLine) {
        return tslDocSnippets.get(beginningLine);
    }

    /* ------------------------ */

    public List<TSLTagSnippet> getTagSnippets() {
        return Collections.unmodifiableList(tagSnippets);
    }

    /* ------------------------ */

    public Map<String, TSLCaptureSnippet> getCaptureSnippets() {
        HashMap<String, TSLCaptureSnippet> captureSnippets = new HashMap<>(this.captureSnippets);
        if (!importedRulesets.isEmpty()) {
            importedRulesets.stream()
                    .map(TSLRuleset::getCaptureSnippets)
                    .forEach(captureSnippets::putAll);
        }
        return Collections.unmodifiableMap(captureSnippets);
    }

    public Map<String, TSLCaptureSnippet> getNonImportedCaptureSnippets() {
        return Collections.unmodifiableMap(this.captureSnippets);
    }

    public TSLCaptureSnippet getCaptureSnippet(TSLCaptureCall captureCall) {
        return getCaptureSnippet(captureCall, true);
    }

    public TSLCaptureSnippet getCaptureSnippet(TSLCaptureCall captureCall, boolean silently) {
        TSLCaptureSnippet referredCapture = getCaptureSnippet(captureCall.getCaptureName());

        if (!silently && referredCapture == null) {
            throw new TSLRuntimeError("No capture was defined with name -> " + captureCall.getCaptureName(), captureCall);
        }

        return referredCapture;
    }

    public TSLCaptureSnippet getCaptureSnippet(String captureName) {
        return getCaptureSnippets().get(captureName);
    }

    /* ------------------------ */

    public List<TSLRule> getRules() {
        LinkedList<TSLRule> rules = new LinkedList<>(this.rules);
        if (!importedRulesets.isEmpty()) {
            importedRulesets.stream()
                    .map(TSLRuleset::getRules)
                    .forEach(rules::addAll);
        }
        return Collections.unmodifiableList(rules);
    }

    public List<TSLRule> getNonImportedRules() {
        return Collections.unmodifiableList(this.rules);
    }

    /* ------------------------ */

    @Override
    public @NotNull GoodieObject generateAttributes() {
        return tagAttributes;
    }

    /* ------------------------ */

    public void addTSLDoc(TSLDocSnippet tslDocSnippet) {
        tslDocSnippets.put(tslDocSnippet.getBeginningLine(), tslDocSnippet);
        this.snippets.add(tslDocSnippet);
    }

    public void addTag(TSLTagSnippet tagSnippet) {
        TSLTag tagDefinition = tagSnippet.getTagDefinition();
        TSLPlainWord nameToken = tagSnippet.getTagNameToken();
        List<TSLToken> arguments = tagSnippet.getTagArgTokens();

        tagSnippets.add(tagSnippet);

        // Generate and merge tag attributes
        GoodieObject tagAttributes = tagDefinition.generateAttributes(new TSLContext(tsl), nameToken, arguments);
        this.tagAttributes = GoodieUtils.mergeOverriding(this.tagAttributes, tagAttributes);

        this.snippets.add(tagSnippet);

        tagDefinition.onLoaded(this, tagSnippet);
    }

    public void addCapture(TSLCaptureSnippet captureSnippet) {
        String captureName = captureSnippet.getName();
        Map<String, TSLCaptureSnippet> captureSnippets = getCaptureSnippets();

        if (captureSnippets.containsKey(captureName)) {
            throw new TSLSyntaxError(captureSnippet.getHeaderToken() + " is already defined", captureSnippet);
        }

        for (TSLToken capturedToken : captureSnippet.getCapturedTokens()) {
            if (capturedToken instanceof TSLCaptureCall) {
                if (!captureSnippets.containsKey(((TSLCaptureCall) capturedToken).getCaptureName())) {
                    throw new TSLSyntaxError(captureSnippet.getHeaderToken() + " is not defined.", capturedToken);
                }
            }
        }

        this.captureSnippets.put(captureName, captureSnippet);
        this.snippets.add(captureSnippet);
    }

    public void addRule(TSLRule rule) {
        rule.setAssociatedRuleset(this);
        this.rules.add(rule);
        this.snippets.add(rule.getSnippet());
    }

    public void addPluginAlias(String alias, String pluginId) {
        importedPlugins.put(alias, pluginId);
    }

    public void importRuleset(TSLRuleset ruleset) {
        if (ruleset.getFile() == null) {
            // This might not be necessary (?)
            throw new TSLRuntimeError("Cannot import ruleset without a file.");
        }

        importedRulesets.add(ruleset);
    }

    /* ------------------------ */

    public boolean perform(TSLContext context) {
        if (context.getTsl() != this.tsl) {
            throw new TSLRuntimeError("Cannot perform() within different container");
        }

        context.setImportedPlugins(getImportedPlugins());
        context.setCaptureSnippets(getCaptureSnippets());
        context.setMessageToken(null); // Reset Message token

        boolean performed = false;

        // Perform on imported rulesets first
        for (TSLRuleset importedRuleset : importedRulesets) {
            performed |= importedRuleset.perform(context);
        }

        // Perform on rules on this ruleset
        for (TSLRule rule : rules) {
            performed |= rule.perform(context);
        }

        return performed;
    }

}
