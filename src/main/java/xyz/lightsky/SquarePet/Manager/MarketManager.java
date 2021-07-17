package xyz.lightsky.SquarePet.Manager;

import cn.nukkit.utils.Config;
import xyz.lightsky.SquarePet.Main;
import xyz.lightsky.SquarePet.Prop.BaseProp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MarketManager {

    private static Map<String, Integer> skillStonePrices = new HashMap<>();
    private static Map<String, Integer> petPrices = new HashMap<>();
    private static Map<String, Integer> propPrices = new HashMap<>();

    private static Config propConfig;
    private static Config petConfig;
    private static Config skillConfig;

    public static void init() {
        File base = new File(Main.getInstance().getDataFolder() + "/市场/");
        if(base.mkdirs()) {
            Main.info("正在创建市场文件夹");
        }
        propConfig = new Config(base + "基础道具市场");
        petConfig = new Config(base + "宠物市场");
        skillConfig = new Config( base + "技能石市场");
        registerAllGoods();
    }

    public static void registerAllGoods() {
        registerPet();
        registerProp();
        registerSkill();
    }

    public static int getBaseProp$Cost(int id) {
        return propPrices.get(BaseProp.getProp(id).getName());
    }

    public static int getSkillStone$Cost(String skillName) {
        return skillStonePrices.get(skillName);
    }

    public static int getPet$Cost(String type) {
        return petPrices.get(type);
    }

    public static void registerPet() {
        petConfig.getAll().forEach((k, v) -> {
            petPrices.put(k, (Integer) v);
        });
    }

    public static void registerProp() {
        propConfig.getAll().forEach((k, v) -> {
            propPrices.put(k, (Integer) v);
        });
    }

    public static void registerSkill() {
        skillConfig.getAll().forEach((k, v) -> {
            skillStonePrices.put(k, (Integer) v);
        });
    }

}
