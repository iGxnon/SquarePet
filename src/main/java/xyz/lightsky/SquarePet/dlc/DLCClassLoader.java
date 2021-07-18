package xyz.lightsky.SquarePet.dlc;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class DLCClassLoader extends URLClassLoader {

    private final DLCLoader loader;

    private final Map<String, Class> classes = new HashMap<>();

    public DLCClassLoader(DLCLoader loader, ClassLoader parent, File file) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);
        this.loader = loader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return this.findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("cn.nukkit.") || name.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);
        if (result == null) {
            if (checkGlobal) {
                result = loader.getClassByName(name);
            }
            if (result == null) {
                result = super.findClass(name);
                if (result != null) {
                    loader.setClass(name, result);
                }
            }
            classes.put(name, result);
        }
        return result;
    }

}