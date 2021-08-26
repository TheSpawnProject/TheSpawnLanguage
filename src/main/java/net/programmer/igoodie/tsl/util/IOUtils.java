package net.programmer.igoodie.tsl.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

public final class IOUtils {

    private IOUtils() {}

    public static String readString(String path) {
        return readString(new File(path));
    }

    public static String readString(File file) {
        StringBuilder builder = new StringBuilder();

        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(s -> builder.append(s).append("\n"));

        } catch (IOException e) {
            return null;
        }

        return builder.toString();
    }

}
