package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.manager.PetManager;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.pet.PetResourceCache;
import xyz.lightsky.squarepet.prop.symbol.PetAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.Random;

public class HPEnhanceProp extends BaseProp implements PetAcceptable {

    public static final int ID = 7;

    @Override
    public String getName() {
        return Lang.translate("%prop.hpenhance%");
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public String getInfo() {
        return Lang.translate("%prop.hpenhance.info%");
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11);
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            if(pet.getMaxHealth() == PetManager.getUltimateHp(petType)) {
                trainer.sendMessage(Lang.translate("%user.prop.hpenlarge.max%"));
                return false;
            }
            int result = addition + pet.getMaxHealth();
            pet.setMaxHealth(Math.min(result, PetManager.getUltimateHp(petType)));
            pet.save();
        }else {
            PetResourceCache ache = trainer.getPetMap().get(petType);
            if(ache.getMaxHP() == PetManager.getUltimateHp(petType)) {
                trainer.sendMessage(Lang.translate("%user.prop.hpenlarge.max%"));
                return false;
            }
            int result = addition + ache.getMaxHP();
            ache.setMaxHP(Math.min(result, PetManager.getUltimateHp(petType)));
            ache.save();
        }
        trainer.sendMessage(Lang.translate("%user.prop.hpenlarge.addition%")
                .replace("{addition}", String.valueOf(addition)));
        return true;
    }
}
