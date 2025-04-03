package net.programmer.igoodie.tslsp.capability;

import org.eclipse.lsp4j.SemanticTokenTypes;
import org.eclipse.lsp4j.SemanticTokensLegend;
import org.eclipse.lsp4j.SemanticTokensWithRegistrationOptions;

import java.util.Arrays;

public class TSLSemanticTokenCapability extends Capability<SemanticTokensWithRegistrationOptions> {

    public enum TokenType {
        COMMENT(0, SemanticTokenTypes.Comment),
        FUNCTION(1, SemanticTokenTypes.Function),
        VARIABLE(2, SemanticTokenTypes.Variable),
        KEYWORD(3, SemanticTokenTypes.Keyword),
        ;

        public final int id;
        public final String value;

        TokenType(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    public enum TokenTypeModifier {
//        ABSTRACT(1, SemanticTokenModifiers.Abstract),
//        READONLY(1 << 1, SemanticTokenModifiers.Readonly),
//        DOCUMENTATION(1 << 2, SemanticTokenModifiers.Documentation)
        ;

        public final int id;
        public final String value;

        TokenTypeModifier(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    @Override
    public SemanticTokensWithRegistrationOptions buildOptions() {
        SemanticTokensLegend legend = new SemanticTokensLegend();

        legend.setTokenTypes(Arrays.stream(TokenType.values()).map(t -> t.value).toList());
        legend.setTokenModifiers(Arrays.stream(TokenTypeModifier.values()).map(t -> t.value).toList());

        SemanticTokensWithRegistrationOptions options = new SemanticTokensWithRegistrationOptions();
        options.setFull(true);
        options.setLegend(legend);
        return options;
    }

}
