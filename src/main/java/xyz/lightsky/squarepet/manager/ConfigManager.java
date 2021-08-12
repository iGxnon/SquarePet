package xyz.lightsky.squarepet.manager;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.trainer.Trainer;
import xyz.lightsky.squarepet.utils.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {

    private static Config config;
    private static List<Integer> petContainsArray = new ArrayList<>();
    private static int trainerMaxLv;
    private static String petPrefixBase = "";

    public static void init() {
        config = Main.getInstance().getConfig();
        registerPetContains();
        registerPlayerMaxLv();
        registerPetPrefix();
        Main.info(Lang.translate("%sys.config.loaded%"));
    }

    public static String getLanguage() {
        return config.getString("language");
    }

    // Frequently visit, register directly
    private static void registerPlayerMaxLv() {
        trainerMaxLv = config.getInt("训练师最高等级");
    }

    private static void registerPetPrefix() {
        petPrefixBase = config.getString("宠物头衔显示格式");
    }

    private static void registerPetContains() {
        String[] str = config.getString("训练师拥有宠物数量").split(",");
        if (str.length != 10) {
            petContainsArray = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
            return;
        }
        for (String v : str) {
            try {
                petContainsArray.add(Integer.parseInt(v));
            } catch (NumberFormatException var6) {
                petContainsArray = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
                return;
            }
        }
    }



    public static String getPetPrefixBase() {
        return petPrefixBase;
    }

    private static List<Integer> getPetContainsArray() {
        return petContainsArray;
    }

    public static int getPetContains(Trainer trainer) {
        int level = trainer.getLevel();
        if ((double)level > (double)getTrainerMaxLv() * 0.9D) {
            return (Integer)getPetContainsArray().get(9);
        } else if ((double)level > (double)getTrainerMaxLv() * 0.8D) {
            return (Integer)getPetContainsArray().get(8);
        } else if ((double)level > (double)getTrainerMaxLv() * 0.7D) {
            return (Integer)getPetContainsArray().get(7);
        } else if ((double)level > (double)getTrainerMaxLv() * 0.6D) {
            return (Integer)getPetContainsArray().get(6);
        } else if ((double)level > (double)getTrainerMaxLv() * 0.5D) {
            return (Integer)getPetContainsArray().get(5);
        } else if ((double)level > (double)getTrainerMaxLv() * 0.4D) {
            return (Integer)getPetContainsArray().get(4);
        } else if ((double)level > (double)getTrainerMaxLv() * 0.3D) {
            return (Integer)getPetContainsArray().get(3);
        } else if ((double)level > (double)getTrainerMaxLv() * 0.2D) {
            return (Integer)getPetContainsArray().get(2);
        } else {
            return (double)level > (double)getTrainerMaxLv() * 0.1D ? getPetContainsArray().get(1) : getPetContainsArray().get(0);
        }
    }

    public static String getCurrencyUnit() {
        return config.getString("货币单位");
    }

    public static int getTrainerNeedExp(int lv) {
        return (int) (config.getDouble("初始升级经验值") * Math.pow(lv, config.getDouble("等级经验倍率")));
    }

    public static String getPetPrefix(BaseSquarePet pet) {
        String base = getPetPrefixBase();
        int hp = (int)pet.getHealth();
        int maxHP = pet.getMaxHealth();
        int lv = pet.getLv();
        int sp = pet.getSp();
        int maxSP = pet.getMaxSP();
        int exp = pet.getExp();
        int maxExp = pet.getMaxExp();
        String owner = pet.getOwner().getName();
        String name = pet.getName();
        String spBar = Tools.generateBar(sp, maxSP, 10);
        String hpBar = Tools.generateBar(hp, maxHP, 10);
        String expBar = Tools.generateBar(exp, maxExp, 10);
        String result = base.replace("{hp}", String.valueOf(hp))
                .replace("{lv}", String.valueOf(lv))
                .replace("{name}", name)
                .replace("{owner}", owner)
                .replace("{maxHP}", String.valueOf(maxHP))
                .replace("{sp}", String.valueOf(sp))
                .replace("{maxSP}", String.valueOf(maxSP))
                .replace("{exp}", String.valueOf(exp))
                .replace("{maxExp}", String.valueOf(maxExp))
                .replace("{spBar}", spBar)
                .replace("{hpBar}", hpBar)
                .replace("{expBar}", expBar)
                .replace("{n}", "\n");
        if (pet.isInLineup()) {
            return result.replace("{lineup}", "阵容+");
        }else {
            return result.replace("{lineup}", "");
        }
    }

    public static int getPetNameMaxLength() {
        return config.getInt("宠物名称最大长度") * 2;
    }

    public static boolean showAttack() {
        return config.getBoolean("开启伤害显示");
    }

    public static double getMaxLuckilyUpRate() {
        return config.getDouble("幸运值最大暴击加成");
    }

    public static int getTrainerMaxLv() {
        return trainerMaxLv;
    }

    public static Config getConfig() {
        return config;
    }

    public static boolean isOP(Player player) {
        return config.getStringList("管理人员").contains(player.getName()) && player.isOp();
    }

}
