package xyz.lightsky.squarepet.prop;

import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.prop.symbol.TrainerAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.Random;

public class LuckyStrawProp extends BaseProp implements TrainerAcceptable {

    public static final int ID = 0;

    @Override
    public String getName() {
        return Lang.translate("%prop.luckystraw%");
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getInfo() {
        return Lang.translate("%prop.luckystraw.info%");
    }

    @Override
    public boolean onUseToTrainer(Trainer trainer) {
        int raw = trainer.getLuckRate();
        trainer.setLuckRate(raw + (new Random().nextInt(101 - raw)));
        trainer.save();
        int addition = trainer.getLuckRate() - raw;
        trainer.sendMessage(Lang.translate("%user.trainer.lucky.addition%")
                .replace("{addition}", String.valueOf(addition)));
        return true;
    }
}
