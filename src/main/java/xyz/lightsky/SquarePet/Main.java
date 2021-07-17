package xyz.lightsky.SquarePet;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import xyz.lightsky.SquarePet.Form.Menu;
import xyz.lightsky.SquarePet.Form.API.WindowManager;
import xyz.lightsky.SquarePet.Listner.TrainerHandlePetListener;
import xyz.lightsky.SquarePet.Listner.TrainerListener;
import xyz.lightsky.SquarePet.Manager.*;
import xyz.lightsky.SquarePet.Prop.BaseProp;
import xyz.lightsky.SquarePet.Skill.BaseSkill;
import xyz.lightsky.SquarePet.Trainer.Trainer;

public class Main extends PluginBase {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static void info(String message) {
        getInstance().getLogger().info(message);
    }

    public static void warning(String message) {
        getInstance().getLogger().warning(message);
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        ConfigManager.init();
        PetManager.init();
        TrainerManager.init();
        DLCManager.init();
        MarketManager.init();

        BaseProp.init();
        BaseSkill.init();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new TrainerListener(), this);
        getServer().getPluginManager().registerEvents(new TrainerHandlePetListener(), this);
        getServer().getPluginManager().registerEvents(new WindowManager(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Trainer trainer = TrainerManager.trainerMap.get(sender.getName());

////            CompoundTag tag = PetManager.createTag((Vector3) sender, testCfg);
////            BaseSquarePet pet = new BaseSquarePet(((Player) sender).chunk, tag);
////            pet.setSkin(((Player) sender).getSkin());
////            pet.spawnToAll();
//
//            trainer.addPet("僵尸");
//            trainer.spawnPet("僵尸");
//
//            System.out.println(trainer.getInfo());
//
//            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
//                @Override
//                public void onRun(int i) {
//                    trainer.closeAllPets();
//                    System.out.println(trainer.hasSpawnedPet("僵尸"));
//                }
//            } ,5 * 20);
            Menu.MAIN((Player) sender);

            System.out.println(trainer.getBag().getContains());
        }
        return true;
    }
}
