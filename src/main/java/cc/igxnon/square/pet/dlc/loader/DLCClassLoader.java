package cc.igxnon.square.pet.dlc.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
/**
 * @author iGxnon
 * @date 2021/08/27
 */
@SuppressWarnings("unused")
public class DLCClassLoader extends URLClassLoader {

    private DLCLoader loader;

    public DLCClassLoader(DLCLoader loader, ClassLoader parent, File jar) throws MalformedURLException {
        super(new URL[]{jar.toURI().toURL()}, parent);
        this.loader = loader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(name.startsWith("cn.nukkit.") || name.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(name);
        }

        return super.findClass(name);
    }
}
