package xyz.lightsky.squarepet.skill;

import cn.nukkit.entity.Entity;
import cn.nukkit.utils.Config;
import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.pet.Attribute;
import xyz.lightsky.squarepet.pet.BaseSquarePet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class BaseSkill {

    public static Map<String, BaseSkill> skillMap = new HashMap<>();

    public static void init() {
        File path = new File(Main.getInstance().getDataFolder(), "技能图鉴");
        if(path.mkdirs()) {
            Main.info("正在生成技能图鉴文件夹");
        }
        Stream.of(Objects.requireNonNull(path.listFiles()))
                .filter(s -> s.getName().endsWith(".yml"))
                .forEach(s-> register(s.getName().split("\\.")[0], new Config(s)));
    }

    public static void register(String name, Config config) {
        skillMap.putIfAbsent(name, new ConfigSkill(config));
        Main.info("已经加载技能: " + name);
    }

    public abstract Attribute getAttribute();

    /**
     * DLC skills need to register!
      */
    public static void register(String name, BaseSkill skill) {
        skillMap.putIfAbsent(name, skill);
    }

    public static BaseSkill get(String name) {
        if(name == null || Objects.equals(name, "")) return null;
        return skillMap.get(name);
    }

    public abstract int getRange();

    public abstract float getDamage();

    public abstract String getName();

    public abstract void work(BaseSquarePet damager, Entity target);

    public abstract int getSpCost();

    @Override
    public String toString() {
        return getName();
    }
}
