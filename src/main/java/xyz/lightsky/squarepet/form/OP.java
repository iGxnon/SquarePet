package xyz.lightsky.squarepet.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementSlider;
import xyz.lightsky.squarepet.form.api.window.FormCustom;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.manager.ConfigManager;
import xyz.lightsky.squarepet.manager.MarketManager;
import xyz.lightsky.squarepet.manager.PetManager;
import xyz.lightsky.squarepet.manager.TrainerManager;
import xyz.lightsky.squarepet.prop.BaseProp;
import xyz.lightsky.squarepet.skill.BaseSkill;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OP {

    public static void OP_SETTING(Player player) {
        FormModal form = new FormModal(Lang.translate("%ui.op.title%"), "",
                Lang.translate("%ui.op.edit.player%"),
                Lang.translate("%ui.op.edit.market%"));
        player.showFormWindow(form.onResponse(s -> {
            if(s) {
                OP_PLAYER_EDIT(player);
            }else {
                OP_MARKET_EDIT(player);
            }
        }));
    }

    public static void OP_PLAYER_EDIT(Player player) {
        List<String> players = Server.getInstance().getOnlinePlayers().values()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        FormCustom form = new FormCustom();
        form.addElement(new ElementDropdown(Lang.translate("%ui.op.edit.player.choose%"), players));
        player.showFormWindow(form.onResponse(s -> {
            String target = s.getDropdownResponse(0).getElementContent();
            OP_PLAYER_EDIT_SWITCH(player, target);
        }));
    }

    public static void OP_PLAYER_EDIT_SWITCH(Player op, String target) {
        FormSimple form = new FormSimple(target, "");
        form.addButton(Lang.translate("%ui.op.switch.addpet%"));
        form.addButton(Lang.translate("%ui.op.switch.delpet%"));
        form.addButton(Lang.translate("%ui.op.switch.addprop%"));
        form.addButton(Lang.translate("%ui.op.switch.addskill%"));
        form.addButton(Lang.translate("%ui.op.switch.editprefix%"));
        form.addButton(Lang.translate("%ui.op.switch.editlevel%"));
        Trainer trainer = TrainerManager.getTrainer(target);
        if(trainer == null) {
            op.sendMessage("该玩家已经离线!");
            return;
        }
        op.showFormWindow(form.onClick(s->{
           switch (s) {
               case 0:
                   OP_PLAYER_EDIT_ADD_PET(op, trainer);
                   break;
               case 1:
                   OP_PLAYER_EDIT_REMOVE_PET(op, trainer);
                   break;
               case 2:
                   OP_PLAYER_EDIT_ADD_PROP(op, trainer);
                   break;
               case 3:
                   OP_PLAYER_EDIT_ADD_SKILL(op, trainer);
                   break;
               case 4:
                   OP_PLAYER_EDIT_PREFIX(op, trainer);
                   break;
               case 5:
                   OP_PLAYER_EDIT_LEVEL(op, trainer);
                   break;
               default:
                   break;
           }
        }));
    }

    public static void OP_PLAYER_EDIT_ADD_PET(Player op, Trainer target) {
        List<String> petTypes = new ArrayList<>(PetManager.TYPES.keySet());
        FormSimple form = new FormSimple(Lang.translate("%ui.op.switch.addpet%"), Lang.translate("%ui.op.addpet.choose%").replace("{trainer}", target.getName()));
        petTypes.forEach(form::addButton);
        op.showFormWindow(form.onClick(s->{
            String type = petTypes.get(s);
            if(target.addPet(type)) {
                op.sendMessage("成给 "+target.getName()+" 添加了宠物 "+type);
            }else {
                op.sendMessage("添加失败,请检查对方使用拥有该类型宠物或者宠物已满");
            }
        }));
    }

    public static void OP_PLAYER_EDIT_REMOVE_PET(Player op, Trainer target) {
        List<String> petTypes = target.getPetTypes();
        FormSimple form = new FormSimple(Lang.translate("%ui.op.switch.delpet%"), Lang.translate("%ui.op.delpet.choose%").replace("{trainer}", target.getName()));
        petTypes.forEach(form::addButton);
        op.showFormWindow(form.onClick(s->{
            String type = petTypes.get(s);
            target.removePet(type);
            op.sendMessage("删除成功!");
        }));
    }

    public static void OP_PLAYER_EDIT_ADD_PROP(Player op, Trainer target) {
        List<String> propTypes = BaseProp.propMap.values().stream()
                .map(BaseProp::getName)
                .collect(Collectors.toList());
        FormCustom form = new FormCustom(Lang.translate("%ui.op.switch.addprop%"));
        form.addElement(new ElementDropdown(Lang.translate("%ui.op.addprop.choose%").replace("{trainer}", target.getName()), propTypes));
        form.addElement(new ElementSlider(Lang.translate("%ui.market.baseprop.count%"), 1F, 20F, 1));
        op.showFormWindow(form.onResponse(s -> {
            String type = s.getDropdownResponse(0).getElementContent();
            int amount = (int) s.getSliderResponse(2);
            target.getBag().put(BaseProp.getID(type), amount);
            op.sendMessage("添加成功");
        }));
    }

    public static void OP_PLAYER_EDIT_ADD_SKILL(Player op, Trainer target) {
        List<String> skillTypes = new ArrayList<>(BaseSkill.skillMap.keySet());
        FormSimple form = new FormSimple(Lang.translate("%ui.op.switch.addskill%"), Lang.translate("%ui.op.addskill.choose%").replace("{trainer}", target.getName()));
        skillTypes.forEach(form::addButton);
        op.showFormWindow(form.onClick(s->{
            String type = skillTypes.get(s);
            Pet.PET_LIST(target, pet -> {
                if(pet != null && !pet.equals("")) {
                    List<String> ownSkills = target.getPetMap().get(pet).getSkills();
                    if(ownSkills.size() >= 3) {
                        op.sendMessage("对方选择的宠物技能已经满了!");
                        target.sendMessage("该宠物技能已经满了!");
                        return;
                    }
                    if(target.getSpawnedPets().get(pet) != null) {
                        target.getSpawnedPets().get(pet).addSkill(BaseSkill.get(type));
                        target.getSpawnedPets().get(pet).save();
                        target.sendMessage("添加成功");
                        op.sendMessage("添加成功");
                    }else {
                        ownSkills.add(type);
                        target.getPetMap().get(pet).setSkills(ownSkills);
                        target.getPetMap().get(pet).save();
                        op.sendMessage("添加成功");
                        target.sendMessage("添加成功");
                    }
                }else {
                    op.sendMessage("添加失败");
                }
            }, Lang.translate("%ui.op.addskill.target.content%").replace("{skillName}", type));
        }));
    }

    public static void OP_PLAYER_EDIT_PREFIX(Player op, Trainer target) {
        FormCustom form = new FormCustom(Lang.translate("%ui.op.switch.editprefix%"));
        form.addElement(new ElementInput(target.getName(), Lang.translate("%ui.op.editprefix.content%")));
        op.showFormWindow(form.onResponse(s->{
            String newPrefix = s.getInputResponse(0);
            target.setPrefix(newPrefix);
            op.sendMessage("设置成功");
            target.sendMessage("你获得了新称号"+newPrefix);
        }));
    }

    public static void OP_PLAYER_EDIT_LEVEL(Player op, Trainer target) {
        float maxLv = ConfigManager.getTrainerMaxLv();
        FormCustom form = new FormCustom(Lang.translate("%ui.op.switch.editlevel%"));
        form.addElement(new ElementSlider(Lang.translate("%ui.op.editlevel.content%"), 1, maxLv, 1));
        op.showFormWindow(form.onResponse(s->{
            int newLv = (int) s.getSliderResponse(0);
            target.setLevel(newLv);
            op.sendMessage("设置成功");
            target.sendMessage("你的等级被设置成了" + newLv);
        }));
    }

    public static void OP_MARKET_EDIT(Player player) {
        FormSimple form = new FormSimple(Lang.translate("%ui.op.edit.market%"), "");
        form.addButton(Lang.translate("%ui.market.pet%"));
        form.addButton(Lang.translate("%ui.market.skillstone%"));
        form.addButton(Lang.translate("%ui.market.baseprop%"));
        form.addButton(Lang.translate("%ui.market.back%"));
        player.showFormWindow(form.onClick(s->{
            switch (s) {
                case 0:
                    OP_MARKET_PET_EDIT(player);
                    break;
                case 1:
                    OP_MARKET_SKILL_STONE_EDIT(player);
                    break;
                case 2:
                    OP_MARKET_BASE_PROP_EDIT(player);
                    break;
                case 3:
                    OP_SETTING(player);
                    break;
                default:
                    break;
            }
        }));
    }

    public static void OP_MARKET_PET_EDIT(Player player) {
        List<String> list = new ArrayList<>(MarketManager.petPrices.keySet());
        FormSimple form = new FormSimple(Lang.translate("%ui.op.edit.market.pet%"), "");
        list.forEach(form::addButton);
        form.addButton(Lang.translate("%ui.op.edit.market.addmore%"));
        player.showFormWindow(form.onClick(s->{
            if(s == list.size()) {
                List<String> extra = PetManager.TYPES.keySet().stream()
                        .filter(e-> !list.contains(e))
                        .collect(Collectors.toList());
                FormSimple formSimple = new FormSimple(Lang.translate("%ui.op.edit.market.pet.choose%"), "");
                extra.forEach(formSimple::addButton);
                player.showFormWindow(formSimple.onClick(b->{
                    String type = extra.get(b);
                    OP_MARKET_PET_EDIT_ADD(player, type);
                }));
            }else {
                Menu.CONFIRM(player, bool->{
                    if(bool) {
                        MarketManager.petPrices.remove(list.get(s));
                        MarketManager.save();
                    }else {
                        OP_MARKET_PET_EDIT_ADD_REMAKE(player, list.get(s));
                    }
                }, "", Lang.translate("%ui.op.edit.market.del.content%"),
                        Lang.translate("%ui.menu.confirm.true%"),
                        Lang.translate("%ui.op.edit.market.editprice%"));
            }
        }));
    }

    public static void OP_MARKET_PET_EDIT_ADD_REMAKE(Player player, String type) {
        FormCustom form = new FormCustom(type);
        form.addElement(new ElementInput(Lang.translate("%ui.op.edit.market.editprice%"),
                Lang.translate("%ui.op.edit.market.editprice.placeholder%")));
        player.showFormWindow(form.onResponse(s->{
            String input = s.getInputResponse(0);
            int price = 0;
            try {
                price = Integer.parseInt(input);
            }catch (NumberFormatException e) {
                player.sendMessage("请输入整数!");
            }
            MarketManager.petPrices.put(type, price);
            MarketManager.save();
            player.sendMessage("设置成功!");
        }));
    }

    public static void OP_MARKET_PET_EDIT_ADD(Player player, String type) {
        OP_MARKET_PET_EDIT_ADD_REMAKE(player, type);
    }

    public static void OP_MARKET_SKILL_STONE_EDIT(Player player) {
        List<String> list = new ArrayList<>(MarketManager.skillStonePrices.keySet());
        FormSimple form = new FormSimple(Lang.translate("%ui.market.skillstone%"), "");
        list.forEach(form::addButton);
        form.addButton(Lang.translate("%ui.op.edit.market.addmore%"));
        player.showFormWindow(form.onClick(s->{
            if(s == list.size()) {
                List<String> extra = BaseSkill.skillMap.keySet().stream()
                        .filter(e-> !list.contains(e))
                        .collect(Collectors.toList());
                FormSimple formSimple = new FormSimple(Lang.translate("%ui.op.edit.market.skill.choose%"), "");
                extra.forEach(formSimple::addButton);
                player.showFormWindow(formSimple.onClick(b -> {
                    String type = extra.get(b);
                    OP_MARKET_SKILL_STONE_EDIT_ADD(player, type);
                }));
            }else {
                Menu.CONFIRM(player, bool->{
                    if(bool) {
                        MarketManager.skillStonePrices.remove(list.get(s));
                        MarketManager.save();
                    }else {
                        OP_MARKET_SKILL_STONE_EDIT_ADD_REMAKE(player, list.get(s));
                    }
                }, "", Lang.translate("%ui.op.edit.market.del.content%"),
                        Lang.translate("%ui.menu.confirm.true%"),
                        Lang.translate("%ui.op.edit.market.editprice%"));
            }
        }));
    }

    public static void OP_MARKET_SKILL_STONE_EDIT_ADD_REMAKE(Player player, String type) {
        FormCustom form = new FormCustom(type);
        form.addElement(new ElementInput(Lang.translate("%ui.op.edit.market.editprice%"),
                Lang.translate("%ui.op.edit.market.editprice.placeholder%")));
        player.showFormWindow(form.onResponse(s->{
            String input = s.getInputResponse(0);
            int price = 0;
            try {
                price = Integer.parseInt(input);
            }catch (NumberFormatException e) {
                player.sendMessage("请输入整数!");
            }
            MarketManager.skillStonePrices.put(type, price);
            MarketManager.save();
            player.sendMessage("设置成功!");
        }));
    }

    public static void OP_MARKET_SKILL_STONE_EDIT_ADD(Player player, String type) {
        OP_MARKET_SKILL_STONE_EDIT_ADD_REMAKE(player, type);
    }

    public static void OP_MARKET_BASE_PROP_EDIT(Player player) {
        List<String> list = new ArrayList<>(MarketManager.propPrices.keySet());
        FormSimple form = new FormSimple(Lang.translate("%ui.market.baseprop%"), "");
        list.forEach(form::addButton);
        form.addButton(Lang.translate("%ui.op.edit.market.addmore%"));
        player.showFormWindow(form.onClick(s->{
            if(s == list.size()) {
                List<String> extra = BaseProp.name2Id.keySet().stream()
                        .filter(e-> !list.contains(e))
                        .collect(Collectors.toList());
                FormSimple formSimple = new FormSimple(Lang.translate("%ui.op.edit.market.prop.choose%"), "");
                extra.forEach(formSimple::addButton);
                player.showFormWindow(formSimple.onClick(b->{
                    String type = extra.get(b);
                    OP_MARKET_BASE_PROP_EDIT_ADD(player, type);
                }));
            }else {
                Menu.CONFIRM(player, bool->{
                    if(bool) {
                        MarketManager.propPrices.remove(list.get(s));
                        MarketManager.save();
                    }else {
                        OP_MARKET_BASE_PROP_EDIT_ADD_REMAKE(player, list.get(s));
                    }
                }, "", Lang.translate("%ui.op.edit.market.del.content%"),
                        Lang.translate("%ui.menu.confirm.true%"),
                        Lang.translate("%ui.op.edit.market.editprice%"));
            }
        }));
    }

    public static void OP_MARKET_BASE_PROP_EDIT_ADD(Player player, String type) {
        OP_MARKET_BASE_PROP_EDIT_ADD_REMAKE(player, type);
    }

    public static void OP_MARKET_BASE_PROP_EDIT_ADD_REMAKE(Player player, String type) {
        FormCustom form = new FormCustom(type);
        form.addElement(new ElementInput(Lang.translate("%ui.op.edit.market.editprice%"),
                Lang.translate("%ui.op.edit.market.editprice.placeholder%")));
        player.showFormWindow(form.onResponse(s->{
            String input = s.getInputResponse(0);
            int price = 0;
            try {
                price = Integer.parseInt(input);
            }catch (NumberFormatException e) {
                player.sendMessage("请输入整数!");
            }
            MarketManager.propPrices.put(type, price);
            MarketManager.save();
            player.sendMessage("设置成功!");
        }));
    }

}
