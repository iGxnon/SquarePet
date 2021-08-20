package xyz.lightsky.squarepet.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import xyz.lightsky.squarepet.form.api.window.FormCustom;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.language.Lang;
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
        form.setTitle(Lang.translate("%ui.pet.list.title%").replace("{trainer}", trainer.getName()));
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
        String state = trainer.getSpawnedPets().containsKey(petType) ? Lang.translate("%ui.pet.takeback%") : Lang.translate("%ui.pet.spawn%");
        FormModal form = new FormModal(petType, trainer.getPetInfo(petType), state, Lang.translate("%ui.pet.back%"));
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
            form = new FormModal(pet.getName(), pet.getInfo(), Lang.translate("%ui.pet.ride%"), Lang.translate("%ui.pet.takeback%"));
        }else {
            form = new FormModal(pet.getName(), pet.getInfo(), Lang.translate("%ui.pet.info%"),Lang.translate("%ui.pet.takeback%"));
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

        FormSimple form = new FormSimple(Lang.translate("%ui.pet.lineup.title%")
                .replace("{trainer}", trainer.getName()),
                lineup.getInfo());
        form.addButton(lineup.getLand() == null ? Lang.translate("%ui.pet.lineup.land.null%") : Lang.translate("%ui.pet.lineup.land%") + lineup.getLand());
        form.addButton(lineup.getSwim() == null ? Lang.translate("%ui.pet.lineup.swim.null%") : Lang.translate("%ui.pet.lineup.swim%") + lineup.getSwim());
        form.addButton(lineup.getFly() == null ?  Lang.translate("%ui.pet.lineup.fly.null%") : Lang.translate("%ui.pet.lineup.fly%") + lineup.getFly());
        form.addButton(Lang.translate("%ui.pet.lineup.spawnall%"));
        trainer.getPlayer().showFormWindow(form.onClick(i->{
            switch (i) {
                case 0:
                    if(lineup.getLand() != null) {
                        Menu.CONFIRM(trainer.getPlayer(), s->{
                            if(s) {
                                lineup.remove(Attribute.LAND);
                                trainer.sendMessage(Lang.translate("%user.remove.success%"));
                            }else {
                                PET_LINEUP(lineup, trainer);
                            }
                        }, "",Lang.translate("%ui.pet.lineup.del.confirm%"));
                    }else {
                        PET_LIST(trainer, s->{
                            if(PetManager.getAttribute(s).equals(Attribute.LAND)) {
                                Menu.CONFIRM(trainer.getPlayer(), bool->{
                                    if(bool) {
                                        lineup.set(Attribute.LAND, s);
                                        lineup.save();
                                        trainer.sendMessage(Lang.translate("%user.set.success%"));
                                    }else {
                                        PET_LINEUP(lineup, trainer);
                                    }
                                }, "", Lang.translate("%ui.pet.lineup.add.confirm%").replace("{type}", s));
                            }else {
                                trainer.sendMessage(Lang.translate("%user.pet.attribute.choose.wrong%"));
                            }
                        }, Lang.translate("%ui.pet.lineup.add.land%"));
                    }
                    break;
                case 1:
                    if(lineup.getSwim() != null) {
                        Menu.CONFIRM(trainer.getPlayer(), s->{
                            if(s) {
                                lineup.remove(Attribute.SWIM);
                                trainer.sendMessage(Lang.translate("%user.remove.success%"));
                            }else {
                                PET_LINEUP(lineup, trainer);
                            }
                        }, "",Lang.translate("%ui.pet.lineup.del.confirm%"));
                    }else {
                        PET_LIST(trainer, s->{
                            if(PetManager.getAttribute(s).equals(Attribute.SWIM)) {
                                Menu.CONFIRM(trainer.getPlayer(), bool->{
                                    if(bool) {
                                        lineup.set(Attribute.SWIM, s);
                                        lineup.save();
                                        trainer.sendMessage(Lang.translate("%user.set.success%"));
                                    }else {
                                        PET_LINEUP(lineup, trainer);
                                    }
                                }, "", Lang.translate("%ui.pet.lineup.add.confirm%").replace("{type}", s));
                            }else {
                                trainer.sendMessage(Lang.translate("%user.pet.attribute.choose.wrong%"));
                            }
                        }, Lang.translate("%ui.pet.lineup.add.swim%"));
                    }
                    break;
                case 2:
                    if(lineup.getFly() != null) {
                        Menu.CONFIRM(trainer.getPlayer(), s->{
                            if(s) {
                                lineup.remove(Attribute.FLY);
                                trainer.sendMessage(Lang.translate("%user.remove.success%"));
                            }else {
                                PET_LINEUP(lineup, trainer);
                            }
                        }, "",Lang.translate("%ui.pet.lineup.del.confirm%"));
                    }else {
                        PET_LIST(trainer, s->{
                            if(PetManager.getAttribute(s).equals(Attribute.FLY)) {
                                Menu.CONFIRM(trainer.getPlayer(), bool->{
                                    if(bool) {
                                        lineup.set(Attribute.FLY, s);
                                        lineup.save();
                                        trainer.sendMessage(Lang.translate("%user.set.success%"));
                                    }else {
                                        PET_LINEUP(lineup, trainer);
                                    }
                                }, "", Lang.translate("%ui.pet.lineup.add.confirm%").replace("{type}", s));
                            }else {
                                trainer.sendMessage(Lang.translate("%user.pet.attribute.choose.wrong%"));
                            }
                        }, Lang.translate("%ui.pet.lineup.add.fly%"));
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
        FormCustom form = new FormCustom();
        List<String> playerList = Server.getInstance().getOnlinePlayers().values()
                .stream().map(Player::getName)
                .filter(s-> !s.equals(trainer.getName()))
                .collect(Collectors.toList());
        playerList.add(Lang.translate("%ui.pet.givepet.dropdown%"));
        form.addElement(new ElementDropdown(Lang.translate("%ui.pet.givepet.dropdown%"), playerList));
        trainer.getPlayer().showFormWindow(form.onResponse(s->{
            Menu.CONFIRM(trainer.getPlayer(), bool->{
                if(bool) {
                    String name = s.getDropdownResponse(0).getElementContent();
                    Trainer target = TrainerManager.getTrainer(name);
                    if(name.equals(Lang.translate("%ui.pet.givepet.dropdown%"))) return;
                    if(target == null) {
                        trainer.sendMessage(Lang.translate("%user.choose.player.offline%"));
                        return;
                    }
                    if(target.getPetTypes().size() >= ConfigManager.getPetContains(target)) {
                        trainer.sendMessage(Lang.translate("%user.choose.player.pet.full%"));
                        return;
                    }
                    target.receivePet(trainer, trainer.getPetMap().get(type));
                    trainer.sendMessage(Lang.translate("%user.givepet.tips%").replace("{type}", type).replace("{target}", target.getName()));
                }else {
                    PET_GIVE(trainer, type);
                }
            });
        }));
    }

    public static void PET_EDIT(Trainer trainer, String type) {
        FormCustom form = new FormCustom();
        form.addElement(new ElementInput(Lang.translate("%ui.pet.edit.name%"), "", trainer.getPetMap().get(type).getName()));
        form.addElement(new ElementToggle(Lang.translate("%ui.pet.edit.autoskill%"), trainer.getPetMap().get(type).isAutoSkill()));
        trainer.getPlayer().showFormWindow(form.onResponse(s-> Menu.CONFIRM(trainer.getPlayer(), bool->{
            if(bool) {
                String newName = s.getInputResponse(0);
                boolean newAuto = s.getToggleResponse(1);
                if(newName.length() >= ConfigManager.getPetNameMaxLength() * 2) {
                    trainer.sendMessage(Lang.translate("%user.pet.name.2long%"));
                    return;
                }
                if(trainer.getSpawnedPets().get(type) != null) {
                    trainer.getSpawnedPets().get(type).setName(s.getInputResponse(0));
                    trainer.getSpawnedPets().get(type).setAutoSkill(newAuto);
                    trainer.getSpawnedPets().get(type).save();
                    trainer.sendMessage(Lang.translate("%user.set.success%"));
                }else {
                    trainer.getPetMap().get(type).setName(s.getInputResponse(0));
                    trainer.getPetMap().get(type).setAutoSkill(newAuto);
                    trainer.getPetMap().get(type).save();
                    trainer.sendMessage(Lang.translate("%user.set.success%"));
                }
            }else {
                PET_EDIT(trainer, type);
            }
        }, "", Lang.translate("%ui.pet.edit.confirm%"))));
    }

}
