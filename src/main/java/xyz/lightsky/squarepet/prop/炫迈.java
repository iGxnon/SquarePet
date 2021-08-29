package xyz.lightsky.squarepet.prop;

import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;
import xyz.lightsky.ModelManagerRe.ModelManagerRe;
import xyz.lightsky.squarepet.prop.symbol.TrainerAcceptable;
import xyz.lightsky.squarepet.utils.Tools;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.Random;

public class 炫迈 extends BaseProp implements TrainerAcceptable {

    public static final int ID = 11;

    @Override
    public String getName() {
        return "炫迈";
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public String getInfo() {
        return "不知道吃了会有什么作用?";
    }

    @Override
    public boolean onUseToTrainer(Trainer trainer) {
        String[] models = ModelManagerRe.getModels().keySet().toArray(new String[0]);
        Skin ache = trainer.getPlayer().getSkin();
        trainer.getPlayer().teleport(trainer.getPlayer().add(0, 1000, 0));
        TaskHandler handler = Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                if(i % 20 == 0) {
                    trainer.getPlayer().sendMessage(Tools.转化成吃了炫迈的话("吃了炫迈，停不下来!"));
                    int index = new Random().nextInt(models.length);
                    trainer.getPlayer().setSkin(ModelManagerRe.getModel(models[index]));
                }
                trainer.getPlayer().resetFallDistance();
            }
        }, 1);
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int i) {
                handler.cancel();
                trainer.getPlayer().setSkin(ache);
            }
        }, 30 * 20);
        int rate = new Random().nextInt(101);
        if(rate < 5) {
            trainer.sendMessage(Tools.转化成吃了炫迈的话("由于你吃了炫迈，触发了隐藏道具 升级卡!"));
            trainer.levelJump(rate);
        }
        return true;
    }

}
