package net.programmer.igoodie.tsl.runtime;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.parser.TSLCapture;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TSLRuleset {

    protected String name;
    protected File file;

    protected List<TSLTag> tags;
    protected Map<TSLTag, JsonObject> attributeMap;

    protected List<TSLRule> rules;
    protected Map<String, TSLCapture> captures;

    public TSLRuleset(String name, File file) {
        this.name = name;
        this.file = file;
        this.rules = new LinkedList<>();
        this.captures = new HashMap<>();
        this.tags = new LinkedList<>();
        this.attributeMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public List<TSLTag> getTags() {
        return tags;
    }

    public Map<TSLTag, JsonObject> getAttributeMap() {
        return attributeMap;
    }

    public JsonObject getSquashedAttributes() {
        JsonObject squashed = new JsonObject();

        for (TSLTag tag : tags) {
            TSLPlugin plugin = tag.getPlugin();
            JsonObject attributes = attributeMap.get(tag);
            for (String field : attributes.keySet()) {
                squashed.add(plugin.prependNamespace(field), attributes.get(field));
            }
        }

        return squashed;
    }

    public Map<String, TSLCapture> getCaptures() {
        return captures;
    }

    public List<TSLRule> getRules() {
        return rules;
    }

    public void addTag(TSLTag tag, TSLString tagName, List<TSLString> args) {
        this.tags.add(tag);
        this.attributeMap.put(tag, tag.evaluateAttributes(tagName, args));
    }

    public void addRule(TSLRule rule) {
        rule.setRuleset(this);
        this.rules.add(rule);
    }

}
