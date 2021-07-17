package xyz.lightsky.SquarePet.Prop;

import xyz.lightsky.SquarePet.Pet.BaseSquarePet;
import xyz.lightsky.SquarePet.Pet.PetResourceAche;
import xyz.lightsky.SquarePet.Prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.Trainer.Trainer;

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
            pet.setMaxHealth(addition + pet.getMaxHealth());
            pet.save();
        }else {
            PetResourceAche ache = trainer.getPetMap().get(petType);
            ache.setMaxHP(addition + ache.getMaxHP());
            ache.save();
        }
        trainer.sendMessage("HP已经扩容HP " + addition + " 点");
        return true;
    }
}
