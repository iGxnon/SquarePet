package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.manager.MarketManager;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.pet.PetResourceCache;
import xyz.lightsky.squarepet.prop.symbol.PetAcceptable;
import xyz.lightsky.squarepet.skill.BaseSkill;
import xyz.lightsky.squarepet.trainer.Trainer;

/**special prop, as player buy it, it will work!*/
public class SkillStoneProp extends BaseProp implements PetAcceptable {

    public static final int ID = 9;

    private BaseSkill achedSkill;

    @Override
    public String getName() {
        return Lang.translate("%prop.skillstone%");
    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public String getInfo() {
        return Lang.translate("%prop.skillstone.info%")
                .replace("{skillName}", getSkill().getName())
                .replace("{attribute}", getSkill().getAttribute().toString());
    }

    public void setSkill(BaseSkill achedSkill) {
        this.achedSkill = achedSkill;
    }

    public BaseSkill getSkill() {
        return achedSkill;
    }

    /** after setSkill()*/
    @Override
    public int get$Cost() {
        return MarketManager.getSkillStone$Cost(getSkill().getName());
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            boolean result = pet.addSkill(getSkill());
            pet.save();
            if(result){
                trainer.sendMessage(Lang.translate("%user.learn.complete%"));
            }
            return result;
        }else {
            PetResourceCache ache = trainer.getPetMap().get(petType);
            if(ache.getSkills().size() < 3) {
                ache.getSkills().add(getSkill().getName());
                ache.save();
                trainer.sendMessage(Lang.translate("%user.learn.complete%"));
                return true;
            }
        }
        return false;
    }
}
