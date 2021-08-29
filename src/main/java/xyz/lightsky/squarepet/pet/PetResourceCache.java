package xyz.lightsky.squarepet.pet;


import cn.nukkit.math.Vector3f;
import cn.nukkit.utils.Config;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xyz.lightsky.squarepet.manager.PetManager;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = {"owner"})
public class PetResourceCache {

    private Trainer owner;
    private String ownerName;
    private String name;
    private List<String> skills = new ArrayList<>();
    private int sp;
    private int lv;
    private int hp;
    private int maxHP;
    private int exp;
    private int maxSP;
    private int maxExp;
    private double defenceRate;
    private int cd;
    private String modelName;
    private String type;
    private Attribute attribute;
    private String attributeStr;
    private double attack;
    private double attackSpeed;
    private double critRate;
    private double critTimeRate;
    private double scale;
    private int spRecoverRate;
    private double spLossRate;
    private Vector3f mountedOffSet;

    private boolean preDead;


    private boolean isInLineup;

    private int maxLv;

    private List<String> foods = new ArrayList<>();

    private boolean autoSkill = false;

    public void save() {
        Config petConf = new Config(getOwner().getPlayerFolder() + "/宠物/" + type + ".yml", Config.YAML);
        petConf.set("名称", name);
        petConf.set("等级", lv);
        petConf.set("经验", exp);
        petConf.set("血量", hp);
        petConf.set("最大血量", maxHP);
        petConf.set("攻击", attack);
        petConf.set("防御", defenceRate);
        petConf.set("暴击率", critRate);
        petConf.set("暴击倍率", critTimeRate);
        petConf.set("SP", sp);
        petConf.set("最大SP", maxSP);
        petConf.set("大小", scale);
        petConf.set("SP恢复速率", spRecoverRate);
        petConf.set("SP损耗率", spLossRate);
        petConf.set("自动释放技能", autoSkill);
        petConf.set("食物", PetManager.getFoods(type));
        getSkills().removeIf(Objects::isNull);
        petConf.set("技能", getSkills());
        petConf.save();
    }

}
