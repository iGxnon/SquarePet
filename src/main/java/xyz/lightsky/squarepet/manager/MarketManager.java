package xyz.lightsky.squarepet.manager;

import cn.nukkit.utils.Config;
import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.prop.BaseProp;
import xyz.lightsky.squarepet.prop.SkillStoneProp;
import xyz.lightsky.squarepet.skill.BaseSkill;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MarketManager {

    public static Map<String, Integer> skillStonePrices = new HashMap<>();
    public static Map<String, Integer> petPrices = new HashMap<>();
    public static Map<String, Integer> propPrices = new HashMap<>();

    private static Config propConfig;
    private static Config petConfig;
    private static Config skillConfig;

    public static SkillStoneProp getSkillStone(String skillName) {
        SkillStoneProp prop = new SkillStoneProp();
        prop.setSkill(BaseSkill.get(skillName));
        return prop;
    }

    public static void init() {
        File base = new File(Main.getInstance().getDataFolder() + "/市场/");
        if(base.mkdirs()) {
            Main.info(Lang.translate("%sys.market.dir.loaded%"));
        }
        propConfig = new Config(base + "/基础道具市场.yml");
        petConfig = new Config(base + "/宠物市场.yml");
        skillConfig = new Config( base + "/技能石市场.yml");
        registerAllGoods();
    }

    public static void registerAllGoods() {
        registerPet();
        registerProp();
        registerSkill();
    }

    public static int getBaseProp$Cost(int id) {
        return propPrices.get(Objects.requireNonNull(BaseProp.getProp(id)).getName());
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

    public static void uploadPet(String type, int price) {
        petPrices.put(type, price);
    }

    public static void uploadSkillStone(String name, int price) {
        skillStonePrices.put(name, price);
    }

    public  static void uploadProp(String name, int price) {
        propPrices.put(name, price);
    }

    public static void save() {
        petPrices.forEach((k, v) -> {
            petConfig.set(k, v);
        });
        petConfig.save();

        propPrices.forEach((k, v) -> {
            propConfig.set(k, v);
        });
        propConfig.save();

        skillStonePrices.forEach((k, v) -> {
            skillConfig.set(k, v);
        });
        skillConfig.save();
    }

}
