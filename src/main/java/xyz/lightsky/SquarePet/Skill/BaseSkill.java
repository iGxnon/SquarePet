package xyz.lightsky.SquarePet.Skill;

import cn.nukkit.entity.Entity;
import xyz.lightsky.SquarePet.Pet.Attribute;
import xyz.lightsky.SquarePet.Pet.BaseSquarePet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseSkill {

    public static Map<String, BaseSkill> skillMap = new HashMap<>();

    public static void init() {
        // todo 注册所有ConfigSkill
    }

    public abstract Attribute getAttribute();

    // DLC Skill 需要去注册!
    public static void register(String name, BaseSkill skill) {
        skillMap.putIfAbsent(name, skill);
    }

    public static BaseSkill get(String name) {
        if(name == null || Objects.equals(name, "")) return null;
        return skillMap.get(name);
    }

    public abstract double getDamage();

    public abstract String getName();

    public abstract void work(BaseSquarePet damager, Entity target);

    public abstract int getSpCost();

    public abstract int get$Cost();

    @Override
    public String toString() {
        return getName();
    }
}
