package xyz.lightsky.SquarePet.prop;

import xyz.lightsky.SquarePet.pet.BaseSquarePet;
import xyz.lightsky.SquarePet.pet.PetResourceAche;
import xyz.lightsky.SquarePet.prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.trainer.Trainer;

import java.util.Random;

public class LargeHPProp extends BaseProp implements PetAcceptable {

    public static final int ID = 5;

    @Override
    public String getName() {
        return "大型HP药剂";
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public String getInfo() {
        return "宠物受伤了? 快来大幅恢复HP";
    }


    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11) + 50;
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
