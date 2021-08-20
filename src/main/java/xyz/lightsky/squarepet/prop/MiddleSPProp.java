package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.pet.PetResourceCache;
import xyz.lightsky.squarepet.prop.symbol.PetAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.Random;

public class MiddleSPProp extends BaseProp implements PetAcceptable {

    public static final int ID = 4;

    @Override
    public String getName() {
        return Lang.translate("%prop.sprecover.middle%");
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public String getInfo() {
        return Lang.translate("%prop.sprecover.middle.info%");
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        int addition = new Random().nextInt(11) + 30;
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            pet.setSp(addition + pet.getSp());
            pet.save();
        }else {
            PetResourceCache ache = trainer.getPetMap().get(petType);
            ache.setSp(addition + ache.getSp());
            ache.save();
        }
        trainer.sendMessage(Lang.translate("%user.prop.sprecover.addition%")
                .replace("{addition}", String.valueOf(addition)));
        return true;
    }
}
