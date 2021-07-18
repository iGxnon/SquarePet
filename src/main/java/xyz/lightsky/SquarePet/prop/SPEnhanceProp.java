package xyz.lightsky.SquarePet.prop;

import xyz.lightsky.SquarePet.manager.PetManager;
import xyz.lightsky.SquarePet.pet.BaseSquarePet;
import xyz.lightsky.SquarePet.pet.PetResourceAche;
import xyz.lightsky.SquarePet.prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.trainer.Trainer;

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
            if(pet.getMaxSP() == PetManager.getUltimateSp(petType)) {
                trainer.sendMessage("已经扩容至最大SP,无法继续扩容了!");
                return false;
            }
            int result = addition + pet.getMaxSP();
            pet.setMaxSP(Math.min(result, PetManager.getUltimateSp(petType)));
            pet.save();
        }else {
            PetResourceAche ache = trainer.getPetMap().get(petType);
            if(ache.getMaxSP() == PetManager.getUltimateSp(petType)) {
                trainer.sendMessage("已经扩容至最大SP,无法继续扩容了!");
                return false;
            }
            int result = addition + ache.getMaxSP();
            ache.setMaxSP(Math.min(result, PetManager.getUltimateSp(petType)));
            ache.save();
        }
        trainer.sendMessage("已经扩容SP " + addition + " 点");
        return true;
    }
}
