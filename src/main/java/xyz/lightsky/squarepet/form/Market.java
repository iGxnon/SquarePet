package xyz.lightsky.squarepet.form;

import cn.nukkit.form.element.ElementSlider;
import me.onebone.economyapi.EconomyAPI;
import xyz.lightsky.squarepet.form.api.window.FormCustom;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.language.Lang;
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

public class Market {

    public static void SWITCH(Trainer trainer) {
        Menu.CONFIRM(trainer.getPlayer(), s->{
            if(s) {
                MARKET_PET(trainer);
            }else {
                MARKET_PROP_SWITCH(trainer);
            }
        }, Lang.translate("%ui.market%"),
                Lang.translate("%ui.market.switch.content%"),
                Lang.translate("%ui.market.switch.pet%"),
                Lang.translate("%ui.market.switch.prop%"));
    }

    public static void MARKET_PET(Trainer trainer) {
        String content = "";
        if(trainer.getPetTypes().size() >= ConfigManager.getPetContains(trainer)) {
            content = Lang.translate("%ui.market.pet.content.tips%");
        }
        FormSimple form = new FormSimple(Lang.translate("%ui.market.pet%"), content);
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
        String info = Lang.translate("%ui.market.pet.info%")
                .replace("{type}", type)
                .replace("{description}", description)
                .replace("{n}", "\n")
                .replace("{price}", String.valueOf(cost))
                .replace("{currencyUnit}", ConfigManager.getCurrencyUnit());
        FormModal form = new FormModal(type, info, Lang.translate("%ui.market.buy%"), Lang.translate("%ui.market.back%"));
        trainer.getPlayer().showFormWindow(form.onResponse(s->{
            if(s) {
                Menu.CONFIRM(trainer.getPlayer(), bool->{
                    if(bool) {
                        if(EconomyAPI.getInstance().myMoney(trainer.getPlayer()) < cost) {
                            trainer.sendMessage(Lang.translate("%user.money.less%").replace("{CurrencyUnit}", ConfigManager.getCurrencyUnit()));
                            return;
                        }
                        if(trainer.addPet(type)) {
                            EconomyAPI.getInstance().reduceMoney(trainer.getPlayer(), cost);
                            trainer.sendMessage(Lang.translate("%user.buy.success%"));
                        }
                    }else {
                        MARKET_PET_INFO(type, cost, trainer);
                    }
                }, "", Lang.translate("%ui.market.buy.confirm%"));
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
        }, "", Lang.translate("%ui.market.prop.switch.content%"),
                Lang.translate("%ui.market.skillstone%"),
                Lang.translate("%ui.market.baseprop%"));
    }

    public static void MARKET_SKILL_STONE(Trainer trainer) {
        List<String> skillStones = new ArrayList<>(MarketManager.skillStonePrices.keySet());
        FormSimple form = new FormSimple(Lang.translate("%ui.market.skillstone%"), "");
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
        String info = Lang.translate("%ui.market.skillstone.info%")
                .replace("{skillName}", skillName)
                .replace("{n}", "\n")
                .replace("{price}", String.valueOf(cost))
                .replace("{currencyUnit}", ConfigManager.getCurrencyUnit())
                .replace("{attribute}", attributeStr)
                .replace("{damage}", String.valueOf(damage))
                .replace("{range}", String.valueOf(range))
                .replace("{spCost}", String.valueOf(spCost));
        FormModal form = new FormModal(skillName, info, Lang.translate("%ui.market.buy%"), Lang.translate("%ui.market.back%"));
        trainer.getPlayer().showFormWindow(form.onResponse(bool->{
            if(bool) {
                Menu.CONFIRM(trainer.getPlayer(), i->{
                    if(i) {
                        if(EconomyAPI.getInstance().myMoney(trainer.getPlayer()) < cost) {
                            trainer.sendMessage(Lang.translate("%user.money.less%").replace("{CurrencyUnit}", ConfigManager.getCurrencyUnit()));
                            return;
                        }
                        SkillStoneProp prop = MarketManager.getSkillStone(skillName);
                        Pet.PET_LIST(trainer, pet->{
                            if(pet == null || pet.equals("")) {
                                return;
                            }
                            if(prop.onUseToPet(trainer, pet)) {
                                EconomyAPI.getInstance().reduceMoney(trainer.getPlayer(), cost);
                            }
                        }, Lang.translate("%ui.market.skillstone.content%"));
                    }else {
                        MARKET_SKILL_STONE_INFO(skillName, cost, trainer);
                    }
                }, "", Lang.translate("%ui.market.buy.confirm%"));
            }else {
                MARKET_SKILL_STONE(trainer);
            }
        }));
    }

    public static void MARKET_BASE_PROP(Trainer trainer) {
        List<String> basePropList = new ArrayList<>(MarketManager.propPrices.keySet());
        FormSimple form = new FormSimple(Lang.translate("%ui.market.baseprop%"), "");
        basePropList.forEach(form::addButton);
        trainer.getPlayer().showFormWindow(form.onClick(s->{
            String propName = basePropList.get(s);
            int propID = BaseProp.getID(propName);
            int cost = MarketManager.getBaseProp$Cost(propID);
            MARKET_BASE_PROP_INFO(propID, cost, trainer);
        }));
    }

    public static void MARKET_BASE_PROP_INFO(int propID, int cost, Trainer trainer) {
        String info = Lang.translate("%ui.market.baseprop.info%")
                .replace("{n}", "\n")
                .replace("{propName}", Objects.requireNonNull(BaseProp.getProp(propID)).getName())
                .replace("{price}", String.valueOf(cost))
                .replace("{description}", Objects.requireNonNull(BaseProp.getProp(propID)).getInfo());
        FormModal form = new FormModal(Objects.requireNonNull(BaseProp.getProp(propID)).getName(), info, Lang.translate("%ui.market.buy%"), Lang.translate("%ui.market.back%"));
        trainer.getPlayer().showFormWindow(form.onResponse(bool->{
            if(bool) {
                Menu.CONFIRM(trainer.getPlayer(), s->{
                    if(s) {
                        FormCustom form1 = new FormCustom();
                        form1.addElement(new ElementSlider(Lang.translate("%ui.market.baseprop.count%"), 1, 50, 1));
                        trainer.getPlayer().showFormWindow(form1.onResponse(res->{
                            int count = (int) res.getSliderResponse(0);
                            if(EconomyAPI.getInstance().myMoney(trainer.getPlayer()) < (cost * count)) {
                                trainer.sendMessage(Lang.translate("%user.money.less%").replace("{CurrencyUnit}", ConfigManager.getCurrencyUnit()));
                                return;
                            }
                            EconomyAPI.getInstance().reduceMoney(trainer.getPlayer(), (cost * count));
                            trainer.getBag().put(propID, count);
                            trainer.getBag().save();
                            trainer.sendMessage(Lang.translate("%user.buy.success%"));
                        }));
                    }else {
                        MARKET_BASE_PROP_INFO(propID, cost, trainer);
                    }
                }, "", Lang.translate("%ui.market.buy.confirm%"));
            }else {
                MARKET_BASE_PROP(trainer);
            }
        }));
    }

}
