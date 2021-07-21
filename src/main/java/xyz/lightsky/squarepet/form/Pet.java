package xyz.lightsky.squarepet.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import xyz.lightsky.squarepet.form.api.window.FormCustom;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.manager.ConfigManager;
import xyz.lightsky.squarepet.manager.PetManager;
import xyz.lightsky.squarepet.manager.TrainerManager;
import xyz.lightsky.squarepet.pet.Attribute;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.trainer.Lineup;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Pet {

    public static void PET_LIST(Trainer trainer, Consumer<String> consumer, String content) {
        FormSimple form = new FormSimple();
        form.setTitle(trainer.getName() + "的宠物列表");
        form.setContent(content);
        trainer.getPetTypes().forEach(form::addButton);
        trainer.getPlayer().showFormWindow(form.onClick(id -> {
            String type = trainer.getPetTypes().get(id);
            if(type != null) {
                consumer.accept(type);
            }
        }));
    }

    public static void PET_INFO(String petType, Trainer trainer) {
        String state = trainer.getSpawnedPets().containsKey(petType) ? "收回" : "召唤";
        FormModal form = new FormModal(petType, trainer.getPetInfo(petType), state, "返回");
        trainer.getPlayer().showFormWindow(form.onResponse(result -> {
            if(result) {
                if(trainer.getSpawnedPets().containsKey(petType)) {
                    trainer.closePet(petType);
                }else {
                    trainer.spawnPet(petType);
                }
            }else {
                Pet.PET_LIST(trainer, s -> Pet.PET_INFO(s, trainer), "");
            }
        }));
    }

    public static void PET_SNAPSHOT(BaseSquarePet pet) {
        if(pet == null) return;
        FormModal form = null;
        if(pet.isCanSeat()){
            form = new FormModal(pet.getName(), pet.getInfo(), "骑乘", "收回");
        }else {
            form = new FormModal(pet.getName(), pet.getInfo(), "简介","收回");
        }
        pet.getOwner().getPlayer().showFormWindow(form.onResponse(s->{
            if(s) {
                if(pet.isCanSeat()){
                    pet.mountEntity(pet.getOwner().getPlayer());
                }else {
                    PET_INFO(pet.getType(), pet.getOwner());
                }
            }else {
                pet.getOwner().closePet(pet.getType());
            }
        }));
    }

    public static void PET_LINEUP(Lineup lineup, Trainer trainer) {
        FormSimple form = new FormSimple(trainer.getName() + " 的宠物阵容", lineup.getInfo());
        form.addButton(lineup.getLand() == null ? "陆属性: 无(点击添加)" : "陆属性: " + lineup.getLand());
        form.addButton(lineup.getSwim() == null ? "水属性: 无(点击添加)" : "水属性: " + lineup.getSwim());
        form.addButton(lineup.getFly() == null ? "空属性: 无(点击添加)" : "空属性: " + lineup.getFly());
        form.addButton("召唤全部");
        trainer.getPlayer().showFormWindow(form.onClick(i->{
            switch (i) {
                case 0:
                    if(lineup.getLand() != null) {
                        Menu.CONFIRM(trainer.getPlayer(), s->{
                            if(s) {
                                lineup.remove(Attribute.LAND);
                                trainer.sendMessage("移除成功");
                            }else {
                                PET_LINEUP(lineup, trainer);
                            }
                        }, "","是否从阵容中移除该宠物?");
                    }else {
                        PET_LIST(trainer, s->{
                            if(PetManager.getAttribute(s).equals(Attribute.LAND)) {
                                Menu.CONFIRM(trainer.getPlayer(), bool->{
                                    if(bool) {
                                        lineup.set(Attribute.LAND, s);
                                        lineup.save();
                                        trainer.sendMessage("设置成功");
                                    }else {
                                        PET_LINEUP(lineup, trainer);
                                    }
                                }, "修改阵容", "你确定要将 "+s+" 加入你的阵容吗?");
                            }else {
                                trainer.sendMessage("请选择陆属性宠物!");
                            }
                        }, "请选择陆属性宠物");
                    }
                    break;
                case 1:
                    if(lineup.getSwim() != null) {
                        Menu.CONFIRM(trainer.getPlayer(), s->{
                            if(s) {
                                lineup.remove(Attribute.SWIM);
                                trainer.sendMessage("移除成功");
                            }else {
                                PET_LINEUP(lineup, trainer);
                            }
                        }, "","是否从阵容中移除该宠物?");
                    }else {
                        PET_LIST(trainer, s->{
                            if(PetManager.getAttribute(s).equals(Attribute.SWIM)) {
                                Menu.CONFIRM(trainer.getPlayer(), bool->{
                                    if(bool) {
                                        lineup.set(Attribute.SWIM, s);
                                        lineup.save();
                                        trainer.sendMessage("设置成功");
                                    }else {
                                        PET_LINEUP(lineup, trainer);
                                    }
                                }, "修改阵容", "你确定要将 "+s+" 加入你的阵容吗?");
                            }else {
                                trainer.sendMessage("请选择水属性宠物!");
                            }
                        }, "请选择水属性宠物");
                    }
                    break;
                case 2:
                    if(lineup.getFly() != null) {
                        Menu.CONFIRM(trainer.getPlayer(), s->{
                            if(s) {
                                lineup.remove(Attribute.FLY);
                                trainer.sendMessage("移除成功");
                            }else {
                                PET_LINEUP(lineup, trainer);
                            }
                        }, "","是否从阵容中移除该宠物?");
                    }else {
                        PET_LIST(trainer, s->{
                            if(PetManager.getAttribute(s).equals(Attribute.FLY)) {
                                Menu.CONFIRM(trainer.getPlayer(), bool->{
                                    if(bool) {
                                        lineup.set(Attribute.FLY, s);
                                        lineup.save();
                                        trainer.sendMessage("设置成功");
                                    }else {
                                        PET_LINEUP(lineup, trainer);
                                    }
                                }, "修改阵容", "你确定要将 "+s+" 加入你的阵容吗?");
                            }else {
                                trainer.sendMessage("请选择空属性宠物!");
                            }
                        }, "请选择空属性宠物");
                    }
                    break;
                case 3:
                    lineup.spawnAll();
                    break;
                default:
                    break;
            }
        }));
    }

    public static void PET_GIVE(Trainer trainer, String type) {
        trainer.closeAllPets();
        FormCustom form = new FormCustom("宠物赠送: " + type);
        List<String> playerList = Server.getInstance().getOnlinePlayers().values()
                .stream().map(Player::getName)
                .filter(s-> !s.equals(trainer.getName()))
                .collect(Collectors.toList());
        playerList.add("请选择一个玩家");
        form.addElement(new ElementDropdown("选择赠送的玩家", playerList));
        trainer.getPlayer().showFormWindow(form.onResponse(s->{
            Menu.CONFIRM(trainer.getPlayer(), bool->{
                if(bool) {
                    String name = s.getDropdownResponse(0).getElementContent();
                    Trainer target = TrainerManager.getTrainer(name);
                    if(name.equals("请选择一个玩家")) return;
                    if(target == null) {
                        trainer.sendMessage("该玩家离线了!");
                        return;
                    }
                    if(target.getPetTypes().size() >= ConfigManager.getPetContains(target)) {
                        trainer.sendMessage("对方宠物容量已经满了!");
                        return;
                    }
                    target.receivePet(trainer, trainer.getPetMap().get(type));
                    trainer.sendMessage("成功将 "+type+" 赠送给了"+target.getName());
                }else {
                    PET_GIVE(trainer, type);
                }
            });
        }));
    }

    public static void PET_EDIT(Trainer trainer, String type) {
        FormCustom form = new FormCustom("宠物编辑: " + type);
        form.addElement(new ElementInput("修改名称", "", trainer.getPetMap().get(type).getName()));
        form.addElement(new ElementToggle("自动释放技能", trainer.getPetMap().get(type).isAutoSkill()));
        trainer.getPlayer().showFormWindow(form.onResponse(s-> Menu.CONFIRM(trainer.getPlayer(), bool->{
            if(bool) {
                String newName = s.getInputResponse(0);
                boolean newAuto = s.getToggleResponse(1);
                if(newName.length() >= ConfigManager.getPetNameMaxLength() * 2) {
                    trainer.sendMessage("宠物名字过长!");
                    return;
                }
                if(trainer.getSpawnedPets().get(type) != null) {
                    trainer.getSpawnedPets().get(type).setName(s.getInputResponse(0));
                    trainer.getSpawnedPets().get(type).setAutoSkill(newAuto);
                    trainer.getSpawnedPets().get(type).save();
                    trainer.sendMessage("设置成功!");
                }else {
                    trainer.getPetMap().get(type).setName(s.getInputResponse(0));
                    trainer.getPetMap().get(type).setAutoSkill(newAuto);
                    trainer.getPetMap().get(type).save();
                    trainer.sendMessage("设置成功!");
                }
            }else {
                PET_EDIT(trainer, type);
            }
        }, "", "确定要修改吗?")));
    }

}
