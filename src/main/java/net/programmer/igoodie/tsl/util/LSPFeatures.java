package net.programmer.igoodie.tsl.util;

import net.programmer.igoodie.goodies.util.Couple;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface LSPFeatures {

    @Nullable
    default List<Couple<String, String>> getCompletionSnippets() {
        return null;
    }

}
