package xyz.lightsky.SquarePet.manager;

import xyz.lightsky.SquarePet.Main;
import xyz.lightsky.SquarePet.trainer.Trainer;

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
            Main.info("玩家数据初始化...");
        }
        trainerMap.clear();
    }

    public static void save() {
        trainerMap.values().forEach(Trainer::save);
    }

}