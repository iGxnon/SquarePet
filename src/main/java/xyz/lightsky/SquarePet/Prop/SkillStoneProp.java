package xyz.lightsky.SquarePet.Prop;

import xyz.lightsky.SquarePet.Pet.BaseSquarePet;
import xyz.lightsky.SquarePet.Pet.PetResourceAche;
import xyz.lightsky.SquarePet.Prop.Symbol.PetAcceptable;
import xyz.lightsky.SquarePet.Skill.BaseSkill;
import xyz.lightsky.SquarePet.Trainer.Trainer;

/**special prop, as player buy it, it will work!*/
public class SkillStoneProp extends BaseProp implements PetAcceptable {

    public static final int ID = 9;

    private BaseSkill achedSkill;

    @Override
    public String getName() {
        return "技能石";
    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public String getInfo() {
        return "给宠物学习技能 " + getSkill().getName() +  " 属性: " + getSkill().getAttribute().toString();
    }

    public void setSkill(BaseSkill achedSkill) {
        this.achedSkill = achedSkill;
    }

    public BaseSkill getSkill() {
        return achedSkill;
    }

    @Override
    public boolean onUseToPet(Trainer trainer, String petType) {
        if(trainer.hasSpawnedPet(petType)) {
            BaseSquarePet pet = trainer.getSpawnedPets().get(petType);
            boolean result = pet.addSkill(getSkill());
            pet.save();
            if(result){
                trainer.sendMessage("学习完毕!");
            }
            return result;
        }else {
            PetResourceAche ache = trainer.getPetMap().get(petType);
            if(ache.getSkills().size() < 3) {
                ache.getSkills().add(getSkill().getName());
                ache.save();
                trainer.sendMessage("学习完毕!");
                return true;
            }
        }
        return false;
    }
}
