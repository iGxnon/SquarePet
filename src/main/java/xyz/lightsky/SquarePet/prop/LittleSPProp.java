package xyz.lightsky.SquarePet.prop;

import xyz.lightsky.SquarePet.pet.BaseSquarePet;
import xyz.lightsky.SquarePet.pet.PetResourceAche;
import xyz.lightsky.SquarePet.prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.trainer.Trainer;

import java.util.Random;

public class LittleSPProp extends BaseProp implements PetAcceptable {

    public static final int ID = 2;

    @Override
    public String getName() {
        return "小型SP药剂";
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String getInfo() {
        return "宠物SP不够? 快来小幅恢复SP";
    }


    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11) + 5;
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            pet.setSp(addition + pet.getSp());
            pet.save();
        }else {
            PetResourceAche ache = trainer.getPetMap().get(petType);
            ache.setSp(addition + ache.getSp());
            ache.save();
        }
        trainer.sendMessage("SP已经恢复SP " + addition + " 点");
        return true;
    }
}
