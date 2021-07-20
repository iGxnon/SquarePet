package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.pet.PetResourceAche;
import xyz.lightsky.squarepet.prop.symbol.PetAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.Random;

public class MiddleHPProp extends BaseProp implements PetAcceptable {

    public static final int ID = 3;

    @Override
    public String getName() {
        return "中型HP药剂";
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public String getInfo() {
        return "宠物受伤了? 快来恢复HP";
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11) + 30;
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            pet.setHealth(addition + pet.getHealth());
            pet.save();
        }else {
            PetResourceAche ache = trainer.getPetMap().get(petType);
            ache.setHp(addition + ache.getHp());
            ache.save();
        }
        trainer.sendMessage("SP已经恢复HP " + addition + " 点");
        return true;
    }
}
