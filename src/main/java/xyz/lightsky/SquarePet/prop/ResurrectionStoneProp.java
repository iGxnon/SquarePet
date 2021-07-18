package xyz.lightsky.SquarePet.prop;

import xyz.lightsky.SquarePet.pet.PetResourceAche;
import xyz.lightsky.SquarePet.prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.trainer.Trainer;

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
            trainer.sendMessage("该宠物没有濒死!");
            return false;
        }
        ache.setPreDead(false);
        ache.setHp(ache.getMaxHP());
        ache.save();
        trainer.sendMessage("宠物已经复活");
        return true;
    }
}
