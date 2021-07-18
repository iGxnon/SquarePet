package xyz.lightsky.SquarePet.skill;

import cn.nukkit.entity.Entity;
import cn.nukkit.utils.Config;
import xyz.lightsky.SquarePet.pet.Attribute;
import xyz.lightsky.SquarePet.pet.BaseSquarePet;

import java.util.ArrayList;
import java.util.List;

public class ConfigSkill extends BaseSkill {

    private Config config;

    private List<String> skillRaw = new ArrayList<>();

    public ConfigSkill(Config config) {
        this.config = config;
        this.skillRaw = config.getStringList("技能");
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.of(config.getString("属性"));
    }

    @Override
    public int getRange() {
        return config.getInt("范围");
    }

    @Override
    public float getDamage() {
        return (float) config.getDouble("伤害");
    }

    @Override
    public String getName() {
        return config.getString("名称");
    }

    @Override
    public void work(BaseSquarePet damager, Entity target) {
        skillRaw.forEach(s -> {

        });
    }

    @Override
    public int getSpCost() {
        return config.getInt("SP消耗");
    }


}
