package xyz.lightsky.squarepet.form;

import cn.nukkit.form.element.ElementSlider;
import me.onebone.economyapi.EconomyAPI;
import xyz.lightsky.squarepet.form.api.window.FormCustom;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.manager.ConfigManager;
import xyz.lightsky.squarepet.manager.MarketManager;
import xyz.lightsky.squarepet.manager.PetManager;
import xyz.lightsky.squarepet.prop.BaseProp;
import xyz.lightsky.squarepet.prop.SkillStoneProp;
import xyz.lightsky.squarepet.skill.BaseSkill;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//todo Market ui System
public class Market {

    public static void SWITCH(Trainer trainer) {
        Menu.CONFIRM(trainer.getPlayer(), s->{
            if(s) {
                MARKET_PET(trainer);
            }else {
                MARKET_PROP_SWITCH(trainer);
            }
        }, "市场", "请选择市场", "宠物市场", "道具市场");
    }

    public static void MARKET_PET(Trainer trainer) {
        String content = "";
        if(trainer.getPetTypes().size() >= ConfigManager.getPetContains(trainer)) {
            content = "温馨提示: 你的宠物已经满了哦，请升级扩大容量再进行购买";
        }
        FormSimple form = new FormSimple("宠物市场", content);
        List<String> petList = new ArrayList<>(MarketManager.petPrices.keySet());
        petList.forEach(form::addButton);
        trainer.getPlayer().showFormWindow(form.onClick(s->{
            String type = petList.get(s);
            int cost = MarketManager.getPet$Cost(type);
            MARKET_PET_INFO(type,cost,trainer);
        }));
    }

    public static void MARKET_PET_INFO(String type, int cost, Trainer trainer) {
        String description = PetManager.getDescription(type);
        String info = "介绍: " + description + "\n"
                + "价格: " + cost + " " + ConfigManager.getCurrencyUnit();
        FormModal form = new FormModal(type, info, "购买", "返回");
        trainer.getPlayer().showFormWindow(form.onResponse(s->{
            if(s) {
                Menu.CONFIRM(trainer.getPlayer(), bool->{
                    if(bool) {
                        if(EconomyAPI.getInstance().myMoney(trainer.getPlayer()) < cost) {
                            trainer.sendMessage("你的"+ConfigManager.getCurrencyUnit()+"好像不够哦");
                            return;
                        }
                        EconomyAPI.getInstance().reduceMoney(trainer.getPlayer(), cost);
                        if(trainer.addPet(type)) {
                            trainer.sendMessage("购买成功!");
                        }
                    }else {
                        MARKET_PET_INFO(type, cost, trainer);
                    }
                }, "", "确定要购买吗?");
            }else {
                MARKET_PET(trainer);
            }
        }));
    }

    public static void MARKET_PROP_SWITCH(Trainer trainer) {
        Menu.CONFIRM(trainer.getPlayer(), s->{
            if(s) {
                MARKET_SKILL_STONE(trainer);
            }else {
                MARKET_BASE_PROP(trainer);
            }
        }, "", "请选择市场", "技能石市场", "基础道具市场");
    }

    public static void MARKET_SKILL_STONE(Trainer trainer) {
        List<String> skillStones = new ArrayList<>(MarketManager.skillStonePrices.keySet());
        FormSimple form = new FormSimple("技能石市场", "");
        skillStones.forEach(form::addButton);
        trainer.getPlayer().showFormWindow(form.onClick( s -> {
            String skillName = skillStones.get(s);
            int cost = MarketManager.getSkillStone$Cost(skillName);
            MARKET_SKILL_STONE_INFO(skillName, cost, trainer);
        }));
    }

    public static void MARKET_SKILL_STONE_INFO(String skillName, int cost, Trainer trainer) {
        float damage = BaseSkill.get(skillName).getDamage();
        int range = BaseSkill.get(skillName).getRange();
        int spCost = BaseSkill.get(skillName).getSpCost();
        String attributeStr = BaseSkill.get(skillName).getAttribute().toString();
        String info = "技能: " + skillName + "\n"
                + "价格: " + cost + " " + ConfigManager.getCurrencyUnit() + "\n"
                + "属性: " + attributeStr + "\n"
                + "伤害: " + damage + "\n"
                + "范围: " + range + "\n"
                + "SP消耗: " + spCost;
        FormModal form = new FormModal(skillName, info, "购买", "返回");
        trainer.getPlayer().showFormWindow(form.onResponse(bool->{
            if(bool) {
                Menu.CONFIRM(trainer.getPlayer(), i->{
                    if(i) {
                        if(EconomyAPI.getInstance().myMoney(trainer.getPlayer()) < cost) {
                            trainer.sendMessage("你的"+ConfigManager.getCurrencyUnit()+"好像不够哦");
                            return;
                        }
                        SkillStoneProp prop = MarketManager.getSkillStone(skillName);
                        Pet.PET_LIST(trainer, pet->{
                            if(pet == null || pet.equals("")) {
                                return;
                            }
                            EconomyAPI.getInstance().reduceMoney(trainer.getPlayer(), cost);
                            prop.onUseToPet(trainer, pet);
                        }, "请选择你的宠物添加此技能");
                    }else {
                        MARKET_SKILL_STONE_INFO(skillName, cost, trainer);
                    }
                }, "", "确认要购买吗?");
            }else {
                MARKET_SKILL_STONE(trainer);
            }
        }));
    }

    public static void MARKET_BASE_PROP(Trainer trainer) {
        List<String> basePropList = new ArrayList<>(MarketManager.propPrices.keySet());
        FormSimple form = new FormSimple("基础道具市场", "");
        basePropList.forEach(form::addButton);
        trainer.getPlayer().showFormWindow(form.onClick(s->{
            String propName = basePropList.get(s);
            int propID = BaseProp.getID(propName);
            int cost = MarketManager.getBaseProp$Cost(propID);
            MARKET_BASE_PROP_INFO(propID, cost, trainer);
        }));
    }

    public static void MARKET_BASE_PROP_INFO(int propID, int cost, Trainer trainer) {
        String info = Objects.requireNonNull(BaseProp.getProp(propID)).getInfo();
        info = "单价: " + cost + " " + ConfigManager.getCurrencyUnit() + "\n" + info;
        FormModal form = new FormModal(Objects.requireNonNull(BaseProp.getProp(propID)).getName(), info, "购买", "返回");
        trainer.getPlayer().showFormWindow(form.onResponse(bool->{
            if(bool) {
                Menu.CONFIRM(trainer.getPlayer(), s->{
                    if(s) {
                        FormCustom form1 = new FormCustom("选择数量");
                        form1.addElement(new ElementSlider("数量", 1, 50, 1));
                        trainer.getPlayer().showFormWindow(form1.onResponse(res->{
                            int count = (int) res.getSliderResponse(0);
                            if(EconomyAPI.getInstance().myMoney(trainer.getPlayer()) < (cost * count)) {
                                trainer.sendMessage("你的"+ConfigManager.getCurrencyUnit()+"好像不够哦");
                                return;
                            }
                            EconomyAPI.getInstance().reduceMoney(trainer.getPlayer(), (cost * count));
                            trainer.getBag().put(propID, count);
                            trainer.getBag().save();
                            trainer.sendMessage("购买成功!");
                        }));
                    }else {
                        MARKET_BASE_PROP_INFO(propID, cost, trainer);
                    }
                }, "", "确认要购买吗?");
            }else {
                MARKET_BASE_PROP(trainer);
            }
        }));
    }

}
