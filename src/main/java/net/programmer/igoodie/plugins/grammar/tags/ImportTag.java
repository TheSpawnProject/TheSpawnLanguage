package net.programmer.igoodie.plugins.grammar.tags;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public class ImportTag extends TSLTag {

    public static final ImportTag INSTANCE = new ImportTag();

    private ImportTag() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "IMPORT");
    }

    @Override
    public @NotNull GoodieObject generateTagAttributes(TSLContext context, TSLPlainWord tagName, List<TSLToken> arguments) throws TSLRuntimeError {
        return new GoodieObject();
    }

    @Override
    public void onLoaded(TheSpawnLanguage language, @NotNull TSLRuleset ruleset, TSLTagSnippet snippet) {
        List<TSLToken> arguments = snippet.getTagArgTokens();

        if (arguments.size() == 1) {
            TSLToken target = arguments.get(0);
            loadTarget(language, ruleset, null, target);

        } else if (arguments.size() == 3) {
            TSLToken alias = arguments.get(0);
            TSLToken keywordFrom = arguments.get(1);
            TSLToken target = arguments.get(2);

            if (!(alias instanceof TSLPlainWord)) {
                throw new TSLSyntaxError("Alias token MUST be a plain word", alias);
            }

            if (!(keywordFrom instanceof TSLPlainWord) || !keywordFrom.getRaw().equalsIgnoreCase("FROM")) {
                throw new TSLSyntaxError("Unexpected token", keywordFrom);
            }

            loadTarget(language, ruleset, ((TSLPlainWord) alias), target);

        } else {
            throw new TSLSyntaxError("Unexpected amount of tokens", snippet);
        }
    }

    private void loadTarget(TheSpawnLanguage language, TSLRuleset ruleset, @Nullable TSLPlainWord aliasToken, TSLToken targetToken) {
        TSLContext context = new TSLContext(language);
        String alias = aliasToken == null ? null : aliasToken.evaluate(context);
        String target = targetToken.evaluate(context);

        if (target.endsWith(".tsl")) {
            // Check if target is absolute or relative path
            // Then include that ruleset
            Path path = IOUtils.resolvePath(ruleset.getFile().getParentFile().getPath(), target);
            if (path == null) {
                throw new TSLSyntaxError("Ruleset cannot be found", targetToken);
            }
            TSLRuleset otherRuleset = new TSLParser(language).parse(path.toFile());
            ruleset.importRuleset(otherRuleset, language);

        } else if (language.getPluginManager().LOADED_PLUGIN_IDS.contains(target)) {
            // It is a plugin, load it in
            ruleset.getImportedPlugins().put(alias == null ? target : alias, target);

        } else {
            throw new TSLSyntaxError("Invalid import format", targetToken);
        }
    }

}
