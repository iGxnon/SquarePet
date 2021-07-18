package xyz.lightsky.SquarePet.prop;

import xyz.lightsky.SquarePet.prop.Symbol.TrainerAcceptable;
import xyz.lightsky.SquarePet.trainer.Trainer;

import java.util.Random;

public class LuckyStrawProp extends BaseProp implements TrainerAcceptable {

    public static final int ID = 0;

    @Override
    public String getName() {
        return "幸运草";
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getInfo() {
        return "快来提高自己的幸运值吧!";
    }

    @Override
    public boolean onUseToTrainer(Trainer trainer) {
        int raw = trainer.getLuckRate();
        trainer.setLuckRate(raw + (new Random().nextInt(101 - raw)));
        trainer.save();
        trainer.sendMessage("幸运值已经提升 " + (trainer.getLuckRate() - raw) + " 点");
        return true;
    }
}
