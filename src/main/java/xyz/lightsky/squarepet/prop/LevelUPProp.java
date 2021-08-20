package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.prop.symbol.TrainerAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

public class LevelUPProp extends BaseProp implements TrainerAcceptable {

    public static final int ID = 12;

    @Override
    public String getName() {
        return Lang.translate("%prop.levelup%");
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public String getInfo() {
        return Lang.translate("%prop.levelup.info%");
    }

    @Override
    public boolean onUseToTrainer(Trainer trainer) {
        if(trainer.getLevel() >= trainer.getMaxLevel()) {
            trainer.sendMessage(Lang.translate("%user.trainer.level.max%"));
            return false;
        }
        trainer.levelUP();
        return true;
    }
}
