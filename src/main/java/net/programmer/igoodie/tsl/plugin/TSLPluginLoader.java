package net.programmer.igoodie.tsl.plugin;

import com.vdurmont.semver4j.SemverException;
import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.exception.TSLPluginLoadingException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class TSLPluginLoader {

    private static final Map<String, Class<?>> ALL_LOADED_CLASSES = new HashMap<>();

    public enum State {
        PRISTINE, LOADING, FAIL, SUCCESS;
    }

    private final URI uri;

    private final Set<Class<?>> loadedClasses = new HashSet<>();
    private State state = State.PRISTINE;
    private Throwable failCause;
    private TSLPluginManifest pluginManifest;
    private TSLPlugin loadedPlugin;

    public TSLPluginLoader(File file) {
        this(file.toURI());
    }

    public TSLPluginLoader(URI uri) {
        this.uri = uri;
    }

    public State getState() {
        return state;
    }

    public Throwable getFailCause() {
        return failCause;
    }

    public Set<Class<?>> getLoadedClasses() {
        return Collections.unmodifiableSet(loadedClasses);
    }

    public TSLPlugin getLoadedPlugin() {
        return loadedPlugin;
    }

    public TSLPluginManifest getPluginManifest() {
        return pluginManifest;
    }

    public void load() {
        try {
            state = State.LOADING;

            loadManifest();
            loadJAR();
            loadPluginClass();

            state = State.SUCCESS;

        } catch (TSLPluginLoadingException e) {
            failCause = e;
            state = State.FAIL;
        }
    }

    public void loadManifest() {
        JarFile jarFile = getJarFile();

        checkForConflicts(jarFile);
        checkManifestIntegrity(jarFile);

        pluginManifest = new TSLPluginManifest(getJarManifestAttrs(jarFile));
    }

    private void loadJAR() throws TSLPluginLoadingException {
        JarFile jarFile = getJarFile();

        Enumeration<JarEntry> entries = jarFile.entries();

        URLClassLoader classLoader = getURLClassLoader();

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();

            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                continue;
            }

            try {
                String className = jarEntry.getName()
                        .substring(0, jarEntry.getName().length() - 6)
                        .replace('/', '.');
                Class<?> loadedClass = classLoader.loadClass(className);
                ALL_LOADED_CLASSES.put(className, loadedClass);
                loadedClasses.add(loadedClass);

            } catch (ClassNotFoundException e) {
                throw new InternalError();
            }
        }
    }

    private void loadPluginClass() throws TSLPluginLoadingException {
        List<Class<? extends TSLPlugin>> pluginClasses = getPluginClasses();

        if (pluginClasses.size() == 0) {
            throw new TSLPluginLoadingException("JAR does not contain a plugin.").withFilePath(uri.getPath());
        }

        if (pluginClasses.size() > 1) {
            throw new TSLPluginLoadingException("Plugin JAR MUST NOT contain multiple plugin classes").withFilePath(uri.getPath());
        }

        checkTargetVersionIntegrity(pluginManifest);

        try {
            Class<? extends TSLPlugin> pluginClass = pluginClasses.get(0);
            TSLPlugin plugin = ReflectionUtilities.createNullaryInstance(pluginClass);
            Field manifestField = TSLPlugin.class.getDeclaredField("manifest");
            ReflectionUtilities.setValue(plugin, manifestField, pluginManifest);
            this.loadedPlugin = plugin;

        } catch (InstantiationException e) {
            throw new TSLPluginLoadingException("Failed to instantiate the Plugin", e).withFilePath(uri.getPath());

        } catch (IllegalAccessException e) {
            throw new TSLPluginLoadingException("Plugin's nullary constructor MUST be accessible").withFilePath(uri.getPath());

        } catch (NoSuchFieldException e) {
            throw new InternalError();
        }
    }

    /* ------------------------ */

    private JarFile getJarFile() {
        try {
            return new JarFile(new File(uri));

        } catch (IOException e) {
            throw new TSLPluginLoadingException("IOException raised", e).withFilePath(uri.getPath());
        }
    }

    private URLClassLoader getURLClassLoader() {
        try {
            URL[] urls = new URL[]{new URL("jar:file:" + uri.toString() + "!/")};
            ClassLoader currentClassLoader = this.getClass().getClassLoader();
            return URLClassLoader.newInstance(urls, currentClassLoader);

        } catch (MalformedURLException e) {
            throw new TSLPluginLoadingException("Malformed URL", e).withFilePath(uri.getPath());
        }
    }

    private Attributes getJarManifestAttrs(JarFile jarFile) {
        try {
            Manifest manifest = jarFile.getManifest();
            return manifest.getMainAttributes();

        } catch (IOException e) {
            throw new TSLPluginLoadingException("", e).withFilePath(uri.getPath());
        }
    }

    private List<Class<? extends TSLPlugin>> getPluginClasses() {
        List<Class<? extends TSLPlugin>> classes = new LinkedList<>();
        for (Class<?> loadedClass : loadedClasses) {
            if (TSLPlugin.class.isAssignableFrom(loadedClass)) {
                @SuppressWarnings("unchecked")
                Class<? extends TSLPlugin> loadedPluginClass = (Class<? extends TSLPlugin>) loadedClass;
                classes.add(loadedPluginClass);
            }
        }
        return classes;
    }

    private void checkForConflicts(JarFile jarFile) {
        traverseClassNames(jarFile, className -> {
            if (ALL_LOADED_CLASSES.containsKey(className)) {
                throw new TSLPluginLoadingException("Confliction detected! Class already loaded in -> " + className).withFilePath(jarFile.getName());
            }
        });
    }

    private void checkManifestIntegrity(JarFile jarFile) {
        Attributes manifestAttrs = getJarManifestAttrs(jarFile);

        if (manifestAttrs.getValue(TSLPluginManifest.ATTR_PLUGIN_ID) == null) {
            throw new TSLPluginLoadingException("Plugin manifest MUST have " + TSLPluginManifest.ATTR_PLUGIN_ID).withFilePath(uri.getPath());
        }
        if (manifestAttrs.getValue(TSLPluginManifest.ATTR_PLUGIN_NAME) == null) {
            throw new TSLPluginLoadingException("Plugin manifest MUST have " + TSLPluginManifest.ATTR_PLUGIN_NAME).withFilePath(uri.getPath());
        }
        if (manifestAttrs.getValue(TSLPluginManifest.ATTR_PLUGIN_VERSION) == null) {
            throw new TSLPluginLoadingException("Plugin manifest MUST have " + TSLPluginManifest.ATTR_PLUGIN_VERSION).withFilePath(uri.getPath());
        }
        if (manifestAttrs.getValue(TSLPluginManifest.ATTR_VERSION_TARGET) == null) {
            throw new TSLPluginLoadingException("Plugin manifest MUST have " + TSLPluginManifest.ATTR_VERSION_TARGET).withFilePath(uri.getPath());
        }
    }

    private void checkTargetVersionIntegrity(TSLPluginManifest manifest) {
        try {
            if (!TheSpawnLanguage.TSL_SEMVER.satisfies(manifest.getTargetVersion())) {
                String message = String.format("Plugin does not fit this version on TSL. (TSL Version: %s, Plugin Target: %s)",
                        TheSpawnLanguage.TSL_VERSION, manifest.getTargetVersion());
                throw new TSLPluginLoadingException(message).withFilePath(uri.getPath());
            }

        } catch (SemverException e) {
            throw new TSLPluginLoadingException("Malformed TSL version target -> " + manifest.getTargetVersion(), e).withFilePath(uri.getPath());
        }
    }

    private void traverseClassNames(JarFile jarFile, Consumer<String> consumer) {
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();

            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                continue;
            }

            String className = jarEntry.getName()
                    .substring(0, jarEntry.getName().length() - 6)
                    .replace('/', '.');
            consumer.accept(className);
        }
    }

}
