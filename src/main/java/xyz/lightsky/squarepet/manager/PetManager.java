package xyz.lightsky.squarepet.manager;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.utils.Config;
import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.pet.Attribute;
import xyz.lightsky.squarepet.pet.PetResourceAche;

import java.io.File;
import java.util.*;

public class PetManager {

    private static File petPath = new File(Main.getInstance().getDataFolder(), "宠物图鉴");

    public static final Map<String, Config> TYPES = new HashMap<>();

    public static void init() {
        if(petPath.mkdirs()) {
            Main.getInstance().getLogger().info("正在创建宠物图鉴文件夹");
        }
        registerAllPetMaps();
    }

    /* 将宠物图鉴注册 */
    private static void registerAllPetMaps() {
        for (File map : Objects.requireNonNull(petPath.listFiles())) {
            if(map.getName().endsWith(".yml")) {
                String type = map.getName().split("\\.")[0];
                TYPES.put(type, new Config(map));
                Main.info(type + " 注册完毕");
            }
        }
    }

    public static Config getBaseConfig(String type) {
        return TYPES.get(type);
    }

    public static double getMinSize(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("基础大小");
    }

    public static int getUltimateAttack(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("攻击上限");
    }

    public static double getMaxSize(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("最大大小");
    }

    public static Vector3f getBaseMountedOffSet(String type) {
        if(getBaseConfig(type) == null) return new Vector3f();
        String[] conf = getBaseConfig(type).getString("座位位置").split("-");
        return new Vector3f(Float.parseFloat(conf[0]), Float.parseFloat(conf[1]), Float.parseFloat(conf[2]));
    }

    public static int getMaxLv(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("最高等级");
    }

    public static boolean canSeat(String type) {
        if(getBaseConfig(type) == null) return false;
        return getBaseConfig(type).getBoolean("能否骑乘");
    }

    public static String getDescription(String type) {
        if(getBaseConfig(type) == null) return "";
        return getBaseConfig(type).getString("介绍");
    }

    /**
     * 返回模型 String
     * Return model String
     * @param type
     * @return
     */
    public static String getModel(String type) {
        if(getBaseConfig(type) == null) return "";
        return getBaseConfig(type).getString("模型");
    }

    /**
     * 将属性作为 String 返回
     * Return the attribute as a String
     * @param type
     * @return
     */
    public static String getAttributeAsString(String type) {
        if(getBaseConfig(type) == null) return "";
        return getBaseConfig(type).getString("属性");
    }

    /**
     * 将属性作为 Attribute 返回
     * Return the attribute as `Attribute`
     * @param type
     * @return
     */
    public static Attribute getAttribute(String type) {
        return Attribute.of(getAttributeAsString(type));
    }

    /**
     * 返回基础HP
     * Return base HP
     * @param type
     * @return
     */
    public static int getBaseHp(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("基础血量");
    }

    /**
     * 返回目前等级升级所需Exp
     * Return the Exp required for the current level upgrade
     * @param type
     * @param lv
     * @return
     */
    public static int getPetNeedExp(String type, int lv) {
        if(getBaseConfig(type) == null) return -1;
        return (int) (getBaseConfig(type).getInt("初始升级经验") + Math.pow(lv, getBaseConfig(type).getInt("等级经验倍率")));
    }

    /**
     * 返回目前等级对应的HP
     * Returns the HP corresponding to the current level
     * @param type
     * @param lv
     * @return
     */
    public static int getPetCurrentMaxHP(String type, int lv) {
        if(getBaseConfig(type) == null) return -1;
        int base = getBaseHp(type);
        int max = getUltimateHp(type);
        double rate = ((double) lv) / ((double) getMaxLv(type));
        return (int) (base + ((max - base) * rate));
    }

    /**
     * 返回目前等级对应的SP
     * Return the SP corresponding to the current level
     * @param type
     * @param lv
     * @return
     */
    public static int getPetCurrentMaxSP(String type, int lv) {
        if(getBaseConfig(type) == null) return -1;
        int base = getBaseMaxSP(type);
        int max = getUltimateSp(type);
        double rate = ((double) lv) / ((double) getMaxLv(type));
        return (int) (base + ((max - base) * rate));
    }


    /**
     * 返回目前等级对应的伤害
     * Returns the damage corresponding to the current level
     * @param type
     * @param lv
     * @return
     */
    public static int getPetCurrentAttack(String type, int lv) {
        if(getBaseConfig(type) == null) return -1;
        int base = getBaseAttack(type);
        int max = getUltimateAttack(type);
        double rate = ((double) lv) / ((double) getMaxLv(type));
        return (int) (base + ((max - base) * rate));
    }

    /**
     * 返回仇恨范围
     * Return to the hatred area
     * @param type
     * @return
     */
    public static int getHatredRange(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("仇恨范围");
    }

    /**
     * 返回攻击范围
     * Return to attack range
     * @param type
     * @return
     */
    public static double getAttackRange(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("攻击范围");
    }

    //todo
    public static double getBaseSpeed(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("基础速度");
    }
    //todo
    public static double getUltimateSpeed(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("速度上限");
    }

    public static int getBaseMaxSP(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("基础MaxSP");
    }

    public static int getBaseAttack(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("基础攻击");
    }

    public static double getBaseAttackSpeed(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("基础攻速");
    }

    public static double getCritRate(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("基础暴击率");
    }

    public static double getCritTimeRate(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("基础暴击倍率");
    }

    public static double getSpRecoverRate(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("基础SP恢复速率");
    }

    public static double getSpLossRate(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("施加技能SP损耗率");
    }

    public static List<String> getFoods(String type) {
        if(getBaseConfig(type) == null) return new ArrayList<>();
        return getBaseConfig(type).getStringList("食物");
    }

    public static double getBaseDefence(String type) {
        if(getBaseConfig(type) == null) return -1D;
        return getBaseConfig(type).getDouble("基础防御");
    }

    public static int getBaseCD(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("基础CD");
    }

    public static List<String> getLineupAdd(String type) {
        if(getBaseConfig(type) == null) return new ArrayList<>();
        return getBaseConfig(type).getStringList("阵容加成");
    }

    public static int getUltimateHp(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("血量上限");
    }

    public static int getUltimateSp(String type) {
        if(getBaseConfig(type) == null) return -1;
        return getBaseConfig(type).getInt("SP上限");
    }

    /** @deprecated */
    @Deprecated
    public static CompoundTag createTag(Vector3 pos, Config cfg) {
        CompoundTag base = Entity.getDefaultNBT(pos);

        CompoundTag squareData = new CompoundTag();

        squareData.putString("主人", cfg.getString("主人"));
        squareData.putString("名称", cfg.getString("名称"));
        squareData.putString("类型", cfg.getString("类型"));
        squareData.putString("属性", cfg.getString("属性"));
        squareData.putInt("等级", cfg.getInt("等级"));
        squareData.putInt("经验", cfg.getInt("经验"));
        squareData.putInt("血量", cfg.getInt("血量"));
        squareData.putInt("最大血量", cfg.getInt("最大血量"));
        squareData.putDouble("攻击", cfg.getInt("攻击"));
        squareData.putDouble("攻速", cfg.getDouble("攻速"));
        squareData.putDouble("防御", cfg.getDouble("防御"));
        squareData.putDouble("暴击率", cfg.getDouble("暴击率"));
        squareData.putDouble("暴击倍率", cfg.getDouble("暴击倍率"));
        squareData.putInt("SP", cfg.getInt("SP"));
        squareData.putInt("最大SP", cfg.getInt("最大SP"));
        squareData.putInt("CD", cfg.getInt("CD"));
        squareData.putDouble("大小", cfg.getDouble("大小"));
        squareData.putInt("SP恢复速率", cfg.getInt("SP恢复速率"));
        squareData.putDouble("SP损耗率", cfg.getDouble("SP损耗率"));
        ListTag<StringTag> skill = new ListTag<>("技能");
        ListTag<StringTag> food = new ListTag<>("食物");
        cfg.getStringList("技能").forEach(s -> skill.add(new StringTag("", s)));
        cfg.getStringList("食物").forEach(s -> food.add(new StringTag("", s)));

        squareData.putList(skill);
        squareData.putList(food);

        base.putCompound("Skin", new CompoundTag());

        squareData.putString("模型", cfg.getString("模型"));

        base.putCompound("SquareDATA", squareData);

        return base;
    }

    public static CompoundTag createTag(Vector3 pos, PetResourceAche ache) {

        CompoundTag base = Entity.getDefaultNBT(pos);

        CompoundTag squareData = new CompoundTag();

        squareData.putString("主人", ache.getOwnerName());
        squareData.putString("名称", ache.getName());
        squareData.putString("类型", ache.getType());
        squareData.putString("属性", ache.getAttributeStr());
        squareData.putInt("等级", ache.getLv());
        squareData.putInt("经验", ache.getExp());
        squareData.putInt("血量", ache.getHp());
        squareData.putInt("最大血量", ache.getMaxHP());
        squareData.putDouble("攻击", ache.getAttack());
        squareData.putDouble("攻速", ache.getAttackSpeed());
        squareData.putDouble("防御", ache.getDefenceRate());
        squareData.putDouble("暴击率", ache.getCritRate());
        squareData.putDouble("暴击倍率", ache.getCritTimeRate());
        squareData.putInt("SP", ache.getSp());
        squareData.putInt("最大SP", ache.getMaxSP());
        squareData.putInt("CD", ache.getCd());
        squareData.putDouble("大小", ache.getScale());
        squareData.putInt("SP恢复速率", ache.getSpRecoverRate());
        squareData.putDouble("SP损耗率", ache.getSpLossRate());
        ListTag<StringTag> skill = new ListTag<>("技能");
        ListTag<StringTag> food = new ListTag<>("食物");
        ache.getSkills().forEach(s -> skill.add(new StringTag("", s)));
        ache.getFoods().forEach(s -> food.add(new StringTag("", s)));

        squareData.putList(skill);
        squareData.putList(food);

        base.putCompound("Skin", new CompoundTag());

        squareData.putString("模型", ache.getModelName());

        base.putCompound("SquareDATA", squareData);

        return base;
    }

}
