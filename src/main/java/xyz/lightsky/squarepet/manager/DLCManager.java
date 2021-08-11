package xyz.lightsky.squarepet.manager;


import cn.nukkit.Server;
import xyz.lightsky.squarepet.dlc.BaseDLC;
import xyz.lightsky.squarepet.dlc.DLCLoader;
import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.language.Lang;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DLCManager {

    public static Map<String, BaseDLC> dlcMap = new LinkedHashMap<>();

    public static void init() {
        dlcMap.clear();
        File dlc = new File(Main.getInstance().getDataFolder() + "/DLC/");
        if (dlc.mkdirs()) {
            Main.info(Lang.translate("%sys.dlc.dir.load%"));
        }
        registerDLCs();
    }

    private static void registerDLCs() {
        long start = System.currentTimeMillis();
        Main.info(Lang.translate("%sys.dlc.list.loading%"));
        File dir = new File(Main.getInstance().getDataFolder() + "/DLC/");
        if (dir.listFiles() != null) {
            File[] files = Objects.requireNonNull(dir.listFiles());
            for (File v : files) {
                if (v.getName().endsWith(".jar")) {
                    loadDLC(v);
                }
            }
            long end = System.currentTimeMillis();
            Main.info(Lang.translate("%sys.dlc.load.timing%").replace("{time}", String.valueOf(end - start)));
        }
    }

    public static void uninstallDLCs() {
        dlcMap.keySet().forEach(DLCManager::uninstallDLC);
    }

    public static void uninstallDLC(String name) {
        if(dlcMap.get(name) == null) {
            Main.warning(Lang.translate("%sys.dlc.notfind%").replace("{dlcName}", name));
            return;
        }
        DLCLoader.disableDLC(dlcMap.get(name));
    }

    public static void registerDLC(String name) {
        File jar = new File(Main.getInstance().getDataFolder() + "/DLC/" + name + ".jar");
        if (!jar.exists()) {
            Main.getInstance().getLogger().warning(Lang.translate("%sys.dlc.notfind%").replace("{dlcName}", name));
            return;
        }
        if(dlcMap.containsKey(name)) {
            Main.getInstance().getLogger().warning(Lang.translate("%sys.dlc.loaded%").replace("{dlcName}", name));
            return;
        }
        loadDLC(jar);
    }

    private static void loadDLC(File jar) {
        BaseDLC dlc = new DLCLoader(Server.getInstance()).loadDLC(jar);
        Main.getInstance().getLogger().info(Lang.translate("%sys.dlc.load.success%").replace("{dlcName}", dlc.getName()));
        DLCLoader.enableDLC(dlc);
        dlcMap.put(dlc.getName(), dlc);
    }


}
