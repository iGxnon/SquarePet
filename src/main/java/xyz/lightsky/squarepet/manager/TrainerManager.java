package xyz.lightsky.squarepet.manager;

import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrainerManager {

    public static Map<String, Trainer> trainerMap = new LinkedHashMap<>();

    public static Trainer getTrainer(String name) {
        return trainerMap.get(name);
    }

    public static void init() {
        File path = new File(Main.getInstance().getDataFolder() + "/玩家数据/");
        if(path.mkdirs()){
            Main.info(Lang.translate("%sys.trainer.dir.loaded%"));
        }
        trainerMap.clear();
    }

    public static void save() {
        trainerMap.values().forEach(Trainer::save);
    }

}
