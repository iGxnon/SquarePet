package xyz.lightsky.SquarePet.manager;


import cn.nukkit.Server;
import xyz.lightsky.SquarePet.dlc.BaseDLC;
import xyz.lightsky.SquarePet.dlc.DLCLoader;
import xyz.lightsky.SquarePet.Main;

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
            Main.info("正在创建DLC文件夹");
        }
        registerDLCs();
    }

    private static void registerDLCs() {
        long start = System.currentTimeMillis();
        Main.info("正在加载DLC列表");
        File dir = new File(Main.getInstance().getDataFolder() + "/DLC/");
        if (dir.listFiles() != null) {
            File[] files = Objects.requireNonNull(dir.listFiles());
            for (File v : files) {
                if (v.getName().endsWith(".jar")) {
                    loadDLC(v);
                }
            }
            long end = System.currentTimeMillis();
            Main.info("DLC加载完毕,用时: " + (end - start) + " ms");
        }
    }

    public static void uninstallDLCs() {
        dlcMap.keySet().forEach(DLCManager::uninstallDLC);
    }

    public static void uninstallDLC(String name) {
        if(dlcMap.get(name) == null) {
            Main.warning("找不到DLC: " + name);
            return;
        }
        DLCLoader.disableDLC(dlcMap.get(name));
    }

    public static void registerDLC(String name) {
        File jar = new File(Main.getInstance().getDataFolder() + "/DLC/" + name + ".jar");
        if (!jar.exists()) {
            Main.getInstance().getLogger().warning("找不到DLC: " + name);
            return;
        }
        loadDLC(jar);
    }

    private static void loadDLC(File jar) {
        BaseDLC dlc = new DLCLoader(Server.getInstance()).loadDLC(jar);
        Main.getInstance().getLogger().info(dlc.getName() + " 加载成功!");
        DLCLoader.enableDLC(dlc);
        dlcMap.put(dlc.getName(), dlc);
    }


}
