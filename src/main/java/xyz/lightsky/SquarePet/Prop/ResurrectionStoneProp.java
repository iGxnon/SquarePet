package xyz.lightsky.SquarePet.Prop;

import xyz.lightsky.SquarePet.Pet.BaseSquarePet;
import xyz.lightsky.SquarePet.Pet.PetResourceAche;
import xyz.lightsky.SquarePet.Prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.Trainer.Trainer;

public class ResurrectionStoneProp extends BaseProp implements PetAcceptable {

    public static final int ID = 10;

    @Override
    public String getName() {
        return "复活石";
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public String getInfo() {
        return "宠物力竭了? 快来复活它吧!";
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        PetResourceAche ache = trainer.getPetMap().get(petType);
        if(!ache.isPreDead()) {
            return false;
        }
        ache.setPreDead(false);
        ache.setHp(ache.getMaxHP());
        ache.save();
        trainer.sendMessage("宠物已经复活");
        return true;
    }
}
