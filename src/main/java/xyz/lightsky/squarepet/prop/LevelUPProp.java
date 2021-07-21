package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.prop.symbol.TrainerAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

public class LevelUPProp extends BaseProp implements TrainerAcceptable {

    public static final int ID = 12;

    @Override
    public String getName() {
        return "训练师升级卡";
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public String getInfo() {
        return "是否还在为了宠物容量不够发愁?";
    }

    @Override
    public boolean onUseToTrainer(Trainer trainer) {
        if(trainer.getLevel() >= trainer.getMaxLevel()) {
            trainer.sendMessage("你已经达到了最高等级,无需升级");
            return false;
        }
        trainer.levelUP();
        return true;
    }
}
