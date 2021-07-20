package xyz.lightsky.squarepet.form;

import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.function.Consumer;

//todo complete Pet ui system
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

}
