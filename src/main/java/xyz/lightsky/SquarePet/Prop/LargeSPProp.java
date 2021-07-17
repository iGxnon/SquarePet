package xyz.lightsky.SquarePet.Prop;

import xyz.lightsky.SquarePet.Pet.BaseSquarePet;
import xyz.lightsky.SquarePet.Pet.PetResourceAche;
import xyz.lightsky.SquarePet.Prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.Trainer.Trainer;

import java.util.Random;

public class LargeSPProp extends BaseProp implements PetAcceptable {

    public static final int ID = 6;

    @Override
    public String getName() {
        return "大型SP药剂";
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public String getInfo() {
        return "宠物SP不够? 快来大幅恢复SP";
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11) + 50;
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
