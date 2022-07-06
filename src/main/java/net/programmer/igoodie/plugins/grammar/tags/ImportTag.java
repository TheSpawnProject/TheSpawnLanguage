package net.programmer.igoodie.plugins.grammar.tags;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ImportTag extends TSLTag {

    public static final ImportTag INSTANCE = new ImportTag();

    private ImportTag() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "IMPORT");
    }

    @Override
    public @NotNull GoodieObject generateAttributes(TSLContext context, TSLPlainWord tagName, List<TSLToken> arguments) throws TSLRuntimeError {
        return new GoodieObject();
    }

    @Override
    public void onLoaded(@NotNull TSLRuleset ruleset, TSLTagSnippet snippet) {
        List<TSLToken> arguments = snippet.getTagArgTokens();

        if (arguments.size() == 1) {
            TSLToken target = arguments.get(0);
            loadTarget(ruleset.getTsl(), ruleset, null, target);

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

            loadTarget(ruleset.getTsl(), ruleset, ((TSLPlainWord) alias), target);

        } else {
            throw new TSLSyntaxError("Unexpected amount of tokens", snippet);
        }
    }

    private void loadTarget(TheSpawnLanguage tsl, TSLRuleset ruleset, @Nullable TSLPlainWord aliasToken, TSLToken targetToken) {
        TSLContext context = new TSLContext(tsl);
        String target = targetToken.evaluate(context);
        String alias = aliasToken == null ? target : aliasToken.evaluate(context);

        if (target.endsWith(".tsl")) {
            // Check if target is absolute or relative path
            // Then include that ruleset
            Path targetPath;

            if (IOUtils.isAbsolutePath(target)) {
                targetPath = IOUtils.getPath(target);

            } else {
                File rulesetFile = ruleset.getFile();
                if (rulesetFile == null) {
                    throw new TSLRuntimeError("Ruleset is not loaded from a file. Cannot IMPORT from a relative path.");
                }
                targetPath = IOUtils.resolvePath(rulesetFile.getParentFile().getPath(), target);
            }

            if (targetPath == null) {
                throw new TSLSyntaxError("Ruleset cannot be found", targetToken);
            }

            TSLRuleset otherRuleset = new TSLParser(tsl).parse(targetPath.toFile());
            ruleset.importRuleset(otherRuleset);

        } else if (tsl.getPluginManager().LOADED_PLUGIN_IDS.contains(target)) {
            ruleset.addPluginAlias(alias, target);

        } else {
            throw new TSLSyntaxError("Unknown plugin id -> " + targetToken.evaluate(context), targetToken);
        }
    }

}
