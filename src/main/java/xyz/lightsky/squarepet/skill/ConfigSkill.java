package xyz.lightsky.squarepet.skill;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import xyz.lightsky.squarepet.pet.Attribute;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.utils.CoreFocus;
import xyz.lightsky.squarepet.utils.Tools;

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
            String key = s.split("-")[0];
            switch (key) {
                case "集中":
                    Tools.generatePos(target).forEach(v->{
                        CompoundTag tag = Tools.createProjectileTag(v, Integer.parseInt(s.split("-")[1]));
                        CoreFocus focus = new CoreFocus(target.chunk, tag, target);
                        focus.spawnToAll();
                    });
                    DestroyBlockParticle particle = new DestroyBlockParticle(target.add(0, target.getEyeHeight()), Block.get(Block.REDSTONE_BLOCK));
                    damager.getLevel().addParticle(particle);
                    target.getLevel().addLevelSoundEvent(target, 47);
                    target.getLevel().addLevelSoundEvent(target, 48);
                    break;
                case "加血量":
                    int value = Integer.parseInt(s.split("-")[1]);
                    damager.healHP(value);
                    break;
                case "加暴击率":
                    int delayTick1 = Integer.parseInt(s.split("-")[2]) * 20;
                    damager.setCritRateAdd(Double.parseDouble(s.split("-")[1]));
                    Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                        @Override
                        public void onRun(int i) {
                            if(damager.isClosed() || !damager.isAlive()) return;
                            damager.setCritRateAdd(0D);
                        }
                    }, delayTick1);
                    break;
                case "加暴击倍率":
                    int delayTick2 = Integer.parseInt(s.split("-")[2]) * 20;
                    damager.setCritTimeRateAdd(Double.parseDouble(s.split("-")[1]));
                    Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                        @Override
                        public void onRun(int i) {
                            if(damager.isClosed() || !damager.isAlive()) return;
                            damager.setCritTimeRateAdd(0D);
                        }
                    }, delayTick2);
                    break;
                case "加防御":
                    int delayTick3 = Integer.parseInt(s.split("-")[2]) * 20;
                    damager.setDefenceRateAdd(Double.parseDouble(s.split("-")[1]));
                    Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                        @Override
                        public void onRun(int i) {
                            if(damager.isClosed() || !damager.isAlive()) return;
                            damager.setDefenceRateAdd(0D);
                        }
                    }, delayTick3);
                    break;
                case "加攻击":
                    int delayTick4 = Integer.parseInt(s.split("-")[2]) * 20;
                    damager.setAttackAdd(Integer.parseInt(s.split("-")[1]));
                    Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                        @Override
                        public void onRun(int i) {
                            if(damager.isClosed() || !damager.isAlive()) return;
                            damager.setAttackAdd(0);
                        }
                    }, delayTick4);
                    break;
                case "雷击":
                    EntityLightning lightning = new EntityLightning(target.chunk, Entity.getDefaultNBT(target.add(0, -8,0)));
                    lightning.spawnToAll();
                    break;
                case "效果":
                    int id = Integer.parseInt(s.split("-")[1]);
                    int level = Integer.parseInt(s.split("-")[2]);
                    int duration = Integer.parseInt(s.split("-")[3]) * 20;
                    target.addEffect(Effect.getEffect(id).setAmplifier(level).setDuration(duration));
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public int getSpCost() {
        return config.getInt("SP消耗");
    }


}
