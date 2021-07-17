package xyz.lightsky.SquarePet.DLC;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.PluginException;
import cn.nukkit.utils.Utils;
import xyz.lightsky.SquarePet.Exception.DLCLoadException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DLCLoader {

    private final Server server;

    private final Map<String, Class> classes = new HashMap<>();
    private final Map<String, DLCClassLoader> classLoaders = new HashMap<>();

    public DLCLoader(Server server) {
        this.server = server;
    }

    public BaseDLC loadDLC(File file) {
        try {
            PluginDescription description = this.getPluginDescription(file);
            if (description != null) {
                String className = description.getMain();
                DLCClassLoader classLoader;
                try {
                    classLoader = new DLCClassLoader(this, this.getClass().getClassLoader(), file);
                } catch (MalformedURLException e) {
                    throw new DLCLoadException(description.getName(), "ClassLoader get failed!");
                }
                this.classLoaders.put(description.getName(), classLoader);
                BaseDLC dlc;
                try {
                    Class javaClass = classLoader.loadClass(className);
                    if (!BaseDLC.class.isAssignableFrom(javaClass)) {
                        throw new PluginException("Main class `" + description.getMain() + "' does not extend BaseDLC");
                    }
                    try {
                        Class<?> pluginClass = javaClass.asSubclass(BaseDLC.class);
                        dlc = (BaseDLC) pluginClass.newInstance();
                        this.initDLC(dlc, description, file);
                        return dlc;
                    } catch (ClassCastException e) {
                        throw new DLCLoadException(description.getName(), "Error whilst initializing main class");
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new DLCLoadException(description.getName(), "unknow reason");
                    }
                } catch (ClassNotFoundException e) {
                    throw new DLCLoadException(description.getName(), "main class not found");
                }
            } else {
                throw new DLCLoadException(file.getName(), "dlc.yml not found!");
            }
        }catch (DLCLoadException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PluginDescription getPluginDescription(File file) {
        try (JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry("dlc.yml");
            if (entry == null) {
                return null;
            }
            try (InputStream stream = jar.getInputStream(entry)) {
                return new PluginDescription(Utils.readFile(stream));
            }
        } catch (IOException e) {
            return null;
        }
    }

    private void initDLC(BaseDLC dlc, PluginDescription description, File file) {
        dlc.init(this.server, description, file);
    }


    public static void enableDLC(BaseDLC dlc) {
        if (dlc != null && !dlc.isEnabled()) {
            dlc.setEnabled(true);
            dlc.onEnable();
        }
    }


    public static void disableDLC(BaseDLC dlc) {
        if (dlc != null && dlc.isEnabled()) {
            dlc.setEnabled(false);
            dlc.onDisable();
        }
    }

    Class<?> getClassByName(final String name) {
        Class<?> cachedClass = classes.get(name);
        if (cachedClass != null) {
            return cachedClass;
        } else {
            for (DLCClassLoader loader : this.classLoaders.values()) {
                try {
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException e) {
                    //ignore
                }
                if (cachedClass != null) {
                    return cachedClass;
                }
            }
        }
        return null;
    }

    void setClass(final String name, final Class<?> clazz) {
        if (!classes.containsKey(name)) {
            classes.put(name, clazz);
        }
    }

}
