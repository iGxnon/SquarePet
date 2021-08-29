package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.pet.PetResourceCache;
import xyz.lightsky.squarepet.prop.symbol.PetAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.Random;

public class MiddleHPProp extends BaseProp implements PetAcceptable {

    public static final int ID = 3;

    @Override
    public String getName() {
        return Lang.translate("%prop.hprecover.middle%");
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public String getInfo() {
        return Lang.translate("%prop.hprecover.middle.info%");
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11) + 30;
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            pet.setHealth(addition + pet.getHealth());
            pet.save();
        }else {
            PetResourceCache ache = trainer.getPetMap().get(petType);
            ache.setHp(addition + ache.getHp());
            ache.save();
        }
        trainer.sendMessage(Lang.translate("%user.prop.hprecover.addition%")
                .replace("{addition}", String.valueOf(addition)));
        return true;
    }
}
