package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.manager.PetManager;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.pet.PetResourceCache;
import xyz.lightsky.squarepet.prop.symbol.PetAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.Random;

public class SPEnhanceProp extends BaseProp implements PetAcceptable {

    public static final int ID = 8;

    @Override
    public String getName() {
        return Lang.translate("%prop.spenhance%");
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public String getInfo() {
        return Lang.translate("%prop.spenhance.info%");
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11);
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            if(pet.getMaxSP() == PetManager.getUltimateSp(petType)) {
                trainer.sendMessage(Lang.translate("%user.prop.spenlarge.max%"));
                return false;
            }
            int result = addition + pet.getMaxSP();
            pet.setMaxSP(Math.min(result, PetManager.getUltimateSp(petType)));
            pet.save();
        }else {
            PetResourceCache ache = trainer.getPetMap().get(petType);
            if(ache.getMaxSP() == PetManager.getUltimateSp(petType)) {
                trainer.sendMessage(Lang.translate("%user.prop.spenlarge.max%"));
                return false;
            }
            int result = addition + ache.getMaxSP();
            ache.setMaxSP(Math.min(result, PetManager.getUltimateSp(petType)));
            ache.save();
        }
        trainer.sendMessage(Lang.translate("%user.prop.spenlarge.addition%")
                .replace("{addition}", String.valueOf(addition)));
        return true;
    }
}
