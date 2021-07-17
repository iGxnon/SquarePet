package xyz.lightsky.SquarePet.Skill;

import cn.nukkit.entity.Entity;
import cn.nukkit.utils.Config;
import xyz.lightsky.SquarePet.Pet.Attribute;
import xyz.lightsky.SquarePet.Pet.BaseSquarePet;

public class ConfigSkill extends BaseSkill {

    private Config config;

    public ConfigSkill(Config config) {
        this.config = config;
    }

    @Override
    public Attribute getAttribute() {
        return null;
    }

    @Override
    public double getDamage() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void work(BaseSquarePet damager, Entity target) {

    }

    @Override
    public int getSpCost() {
        return 0;
    }

    @Override
    public int get$Cost() {
        return 0;
    }

}
