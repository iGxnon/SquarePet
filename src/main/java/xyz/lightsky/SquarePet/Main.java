package xyz.lightsky.SquarePet;


/*
 *
 *    _____                             ____       __
 *   / ___/____ ___  ______ _________  / __ \___  / /_
 *   \__ \/ __ `/ / / / __ `/ ___/ _ \/ /_/ / _ \/ __/
 *  ___/ / /_/ / /_/ / /_/ / /  /  __/ ____/  __/ /_
 * /____/\__, /\__,_/\__,_/_/   \___/_/    \___/\__/
 *         /_/
 *
 */


import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import xyz.lightsky.SquarePet.form.Menu;
import xyz.lightsky.SquarePet.form.API.WindowManager;
import xyz.lightsky.SquarePet.listner.TrainerHandlePetListener;
import xyz.lightsky.SquarePet.listner.TrainerListener;
import xyz.lightsky.SquarePet.manager.*;
import xyz.lightsky.SquarePet.prop.BaseProp;
import xyz.lightsky.SquarePet.skill.BaseSkill;
import xyz.lightsky.SquarePet.utils.Tools;
import xyz.lightsky.SquarePet.trainer.Trainer;

import java.util.Arrays;

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
        getLogger().info(Tools.logo());
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
    public void onDisable() {
        MarketManager.save();
        TrainerManager.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Trainer trainer = TrainerManager.trainerMap.get(sender.getName());

//            CompoundTag tag = PetManager.createTag((Vector3) sender, testCfg);
//            BaseSquarePet pet = new BaseSquarePet(((Player) sender).chunk, tag);
//            pet.setSkin(((Player) sender).getSkin());
//            pet.spawnToAll();
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
        }
        return true;
    }
}
