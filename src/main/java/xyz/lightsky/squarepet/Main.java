package xyz.lightsky.squarepet;


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
import xyz.lightsky.squarepet.form.Menu;
import xyz.lightsky.squarepet.form.api.WindowManager;
import xyz.lightsky.squarepet.listner.TrainerHandlePetListener;
import xyz.lightsky.squarepet.listner.TrainerListener;
import xyz.lightsky.squarepet.manager.*;
import xyz.lightsky.squarepet.pet.animation.AnimationController;
import xyz.lightsky.squarepet.prop.BaseProp;
import xyz.lightsky.squarepet.skill.BaseSkill;
import xyz.lightsky.squarepet.utils.Tools;
import xyz.lightsky.squarepet.trainer.Trainer;

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
        MarketManager.init();

        BaseProp.init();
        BaseSkill.init();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new TrainerListener(), this);
        getServer().getPluginManager().registerEvents(new TrainerHandlePetListener(), this);
        getServer().getPluginManager().registerEvents(new WindowManager(), this);

        DLCManager.init();

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
            Menu.MAIN((Player) sender);
        }
        return true;
    }
}
