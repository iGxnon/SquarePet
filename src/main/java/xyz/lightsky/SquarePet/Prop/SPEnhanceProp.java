package xyz.lightsky.SquarePet.Prop;

import xyz.lightsky.SquarePet.Pet.BaseSquarePet;
import xyz.lightsky.SquarePet.Pet.PetResourceAche;
import xyz.lightsky.SquarePet.Prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.Trainer.Trainer;

import java.util.Random;

public class SPEnhanceProp extends BaseProp implements PetAcceptable {

    public static final int ID = 8;

    @Override
    public String getName() {
        return "SP扩容";
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public String getInfo() {
        return "宠物SP不够用? 随机给宠物扩大SP吧! 最大可扩大 10 点!";
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11);
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            pet.setMaxSP(addition + pet.getMaxSP());
            pet.save();
        }else {
            PetResourceAche ache = trainer.getPetMap().get(petType);
            ache.setMaxSP(addition + ache.getMaxSP());
            ache.save();
        }
        trainer.sendMessage("SP已经扩容SP " + addition + " 点");
        return true;
    }
}
