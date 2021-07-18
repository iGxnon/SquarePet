package xyz.lightsky.SquarePet.prop;

import xyz.lightsky.SquarePet.manager.PetManager;
import xyz.lightsky.SquarePet.pet.BaseSquarePet;
import xyz.lightsky.SquarePet.pet.PetResourceAche;
import xyz.lightsky.SquarePet.prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.trainer.Trainer;

import java.util.Random;

public class HPEnhanceProp extends BaseProp implements PetAcceptable {

    public static final int ID = 7;

    @Override
    public String getName() {
        return "HP扩容";
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public String getInfo() {
        return "宠物HP不够用? 随机给宠物扩大HP吧! 最大可扩大 10 点!";
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11);
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            if(pet.getMaxHealth() == PetManager.getUltimateHp(petType)) {
                trainer.sendMessage("已经扩容至最大HP,无法继续扩容了!");
                return false;
            }
            int result = addition + pet.getMaxHealth();
            pet.setMaxHealth(Math.min(result, PetManager.getUltimateHp(petType)));
            pet.save();
        }else {
            PetResourceAche ache = trainer.getPetMap().get(petType);
            if(ache.getMaxHP() == PetManager.getUltimateHp(petType)) {
                trainer.sendMessage("已经扩容至最大HP,无法继续扩容了!");
                return false;
            }
            int result = addition + ache.getMaxHP();
            ache.setMaxHP(Math.min(result, PetManager.getUltimateHp(petType)));
            ache.save();
        }
        trainer.sendMessage("已经扩容HP " + addition + " 点");
        return true;
    }
}
