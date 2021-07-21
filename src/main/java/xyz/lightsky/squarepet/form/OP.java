package xyz.lightsky.squarepet.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementSlider;
import xyz.lightsky.squarepet.form.api.window.FormCustom;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
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
        FormModal form = new FormModal("OP设置", "", "编辑玩家", "编辑市场");
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
        form.addElement(new ElementDropdown("选择编辑的玩家", players));
        player.showFormWindow(form.onResponse(s -> {
            String target = s.getDropdownResponse(0).getElementContent();
            OP_PLAYER_EDIT_SWITCH(player, target);
        }));
    }

    public static void OP_PLAYER_EDIT_SWITCH(Player op, String target) {
        FormSimple form = new FormSimple(target, "");
        form.addButton("添加宠物");
        form.addButton("删除宠物");
        form.addButton("添加道具");
        form.addButton("添加技能");
        form.addButton("修改称号");
        form.addButton("修改等级");
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
        FormSimple form = new FormSimple("添加宠物", "选择宠物给" + target.getName() + "添加");
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
        FormSimple form = new FormSimple("删除宠物", "选择将" + target.getName() + "的宠物删除");
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
        FormCustom form = new FormCustom("添加道具给" + target.getName());
        form.addElement(new ElementDropdown("选择道具", propTypes));
        form.addElement(new ElementSlider("数量", 1F, 20F, 1));
        op.showFormWindow(form.onResponse(s -> {
            String type = s.getDropdownResponse(0).getElementContent();
            int amount = (int) s.getSliderResponse(2);
            target.getBag().put(BaseProp.getID(type), amount);
            op.sendMessage("添加成功");
        }));
    }

    public static void OP_PLAYER_EDIT_ADD_SKILL(Player op, Trainer target) {
        List<String> skillTypes = new ArrayList<>(BaseSkill.skillMap.keySet());
        FormSimple form = new FormSimple("添加技能", "选择技能给" + target.getName() + "的宠物添加");
        skillTypes.forEach(form::addButton);
        op.showFormWindow(form.onClick(s->{
            String type = skillTypes.get(s);
            Pet.PET_LIST(target, pet->{
                if(pet != null && !pet.equals("")) {
                    if(target.getSpawnedPets().get(pet) != null) {
                        target.getSpawnedPets().get(pet).addSkill(BaseSkill.get(type));
                        target.getSpawnedPets().get(pet).save();
                        target.sendMessage("添加成功!");
                        op.sendMessage("添加成功!");
                    }else {
                        List<String> ownSkills = target.getPetMap().get(pet).getSkills();
                        if(ownSkills.size() >= 3) {
                            op.sendMessage("对方选择的宠物技能已经满了!");
                            target.sendMessage("该宠物技能已经满了!");
                            return;
                        }
                        ownSkills.add(type);
                        target.getPetMap().get(pet).setSkills(ownSkills);
                        target.getPetMap().get(pet).save();
                        op.sendMessage("添加成功!");
                        target.sendMessage("添加成功!");
                    }
                }else {
                    op.sendMessage("添加失败!");
                }
            }, "管理员准备给你技能 " + type + " 请选择宠物添加!");
        }));
    }

    public static void OP_PLAYER_EDIT_PREFIX(Player op, Trainer target) {
        FormCustom form = new FormCustom("修改称号");
        form.addElement(new ElementInput(target.getName(), "输入新的称号"));
        op.showFormWindow(form.onResponse(s->{
            String newPrefix = s.getInputResponse(0);
            target.setPrefix(newPrefix);
            op.sendMessage("设置成功");
            target.sendMessage("你获得了新称号"+newPrefix);
        }));
    }

    public static void OP_PLAYER_EDIT_LEVEL(Player op, Trainer target) {
        float maxLv = ConfigManager.getTrainerMaxLv();
        FormCustom form = new FormCustom("设置等级");
        form.addElement(new ElementSlider("等级", 1, maxLv, 1));
        op.showFormWindow(form.onResponse(s->{
            int newLv = (int) s.getSliderResponse(0);
            target.setLevel(newLv);
            op.sendMessage("设置成功");
            target.sendMessage("你的等级被设置成了" + newLv);
        }));
    }

    public static void OP_MARKET_EDIT(Player player) {
        FormSimple form = new FormSimple("选择需要编辑的市场", "");
        form.addButton("宠物市场");
        form.addButton("技能石市场");
        form.addButton("基础道具市场");
        form.addButton("返回");
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
        FormSimple form = new FormSimple("宠物市场编辑", "");
        list.forEach(form::addButton);
        form.addButton("添加更多");
        player.showFormWindow(form.onClick(s->{
            if(s == list.size()) {
                List<String> extra = PetManager.TYPES.keySet().stream()
                        .filter(e-> !list.contains(e))
                        .collect(Collectors.toList());
                FormSimple formSimple = new FormSimple("选择宠物", "");
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
                }, "", "是否删除该商品?", "是", "编辑价格");
            }
        }));
    }

    public static void OP_MARKET_PET_EDIT_ADD_REMAKE(Player player, String type) {
        FormCustom form = new FormCustom(type);
        form.addElement(new ElementInput("设置价格", "填入整数"));
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
        FormSimple form = new FormSimple("技能石市场编辑", "");
        list.forEach(form::addButton);
        form.addButton("添加更多");
        player.showFormWindow(form.onClick(s->{
            if(s == list.size()) {
                List<String> extra = BaseSkill.skillMap.keySet().stream()
                        .filter(e-> !list.contains(e))
                        .collect(Collectors.toList());
                FormSimple formSimple = new FormSimple("选择技能", "");
                extra.forEach(formSimple::addButton);
                player.showFormWindow(formSimple.onClick(b->{
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
                }, "", "是否删除该商品?", "是", "编辑价格");
            }
        }));
    }

    public static void OP_MARKET_SKILL_STONE_EDIT_ADD_REMAKE(Player player, String type) {
        FormCustom form = new FormCustom(type);
        form.addElement(new ElementInput("设置价格", "填入整数"));
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
        FormSimple form = new FormSimple("基本道具市场编辑", "");
        list.forEach(form::addButton);
        form.addButton("添加更多");
        player.showFormWindow(form.onClick(s->{
            if(s == list.size()) {
                List<String> extra = BaseProp.name2Id.keySet().stream()
                        .filter(e-> !list.contains(e))
                        .collect(Collectors.toList());
                FormSimple formSimple = new FormSimple("选择道具", "");
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
                }, "", "是否删除该商品?", "是", "编辑价格");
            }
        }));
    }

    public static void OP_MARKET_BASE_PROP_EDIT_ADD(Player player, String type) {
        OP_MARKET_BASE_PROP_EDIT_ADD_REMAKE(player, type);
    }

    public static void OP_MARKET_BASE_PROP_EDIT_ADD_REMAKE(Player player, String type) {
        FormCustom form = new FormCustom(type);
        form.addElement(new ElementInput("设置价格", "填入整数"));
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
