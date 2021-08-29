package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.pet.PetResourceCache;
import xyz.lightsky.squarepet.prop.symbol.PetAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

public class ResurrectionStoneProp extends BaseProp implements PetAcceptable {

    public static final int ID = 10;

    @Override
    public String getName() {
        return Lang.translate("%prop.resurrectionstone%");
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public String getInfo() {
        return Lang.translate("%prop.resurrectionstone.info%");
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        PetResourceCache ache = trainer.getPetMap().get(petType);
        if(!ache.isPreDead()) {
            trainer.sendMessage(Lang.translate("%user.pet.not.die%"));
            return false;
        }
        ache.setPreDead(false);
        ache.setHp(ache.getMaxHP());
        ache.save();
        trainer.sendMessage(Lang.translate("%user.pet.resurrection%"));
        return true;
    }
}
