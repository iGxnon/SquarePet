package xyz.lightsky.squarepet.manager;

import cn.nukkit.Server;
import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.io.File;
import java.net.ServerSocket;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrainerManager {

    public static Map<String, Trainer> trainerMap = new LinkedHashMap<>();

    public static Trainer getTrainer(String name) {
        return trainerMap.get(name);
    }

    public static void init() {
        File path = new File(Main.getInstance().getDataFolder() + "/玩家数据/");
        if(path.mkdirs()) {
            Main.info(Lang.translate("%sys.trainer.dir.loaded%"));
        }
        trainerMap.clear();
        Server.getInstance().getOnlinePlayers().values().forEach(player -> trainerMap.put(player.getName(), new Trainer(player)));
    }

    public static void save() {
        trainerMap.values().forEach(Trainer::save);
    }

}
