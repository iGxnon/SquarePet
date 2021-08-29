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
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.plugin.PluginBase;
import xyz.lightsky.squarepet.form.Market;
import xyz.lightsky.squarepet.form.Menu;
import xyz.lightsky.squarepet.form.api.WindowManager;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.listner.TrainerHandlePetListener;
import xyz.lightsky.squarepet.listner.TrainerListener;
import xyz.lightsky.squarepet.manager.*;
import xyz.lightsky.squarepet.prop.BaseProp;
import xyz.lightsky.squarepet.skill.BaseSkill;
import xyz.lightsky.squarepet.utils.Tools;

import javax.xml.bind.Marshaller;
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
        saveDefaultConfig();
        Lang.init();
    }

    @Override
    public void onEnable() {

        ConfigManager.init();
        PetManager.init();
        TrainerManager.init();
        MarketManager.init();

        BaseProp.init();
        BaseSkill.init();

        getServer().getPluginManager().registerEvents(new TrainerListener(), this);
        getServer().getPluginManager().registerEvents(new TrainerHandlePetListener(), this);
        getServer().getPluginManager().registerEvents(new WindowManager(), this);

        DLCManager.init();

    }

    @Override
    public void onDisable() {
        MarketManager.save();
        TrainerManager.save();
        TrainerManager.trainerMap.clear();
        MarketManager.propPrices.clear();
        MarketManager.skillStonePrices.clear();
        MarketManager.petPrices.clear();
        DLCManager.dlcMap.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Menu.MAIN((Player) sender);
        }
        if(sender instanceof ConsoleCommandSender) {
            if(args.length != 2 || !Arrays.asList("install", "uninstall").contains(args[0])) {
                sender.sendMessage(Lang.translate("%sys.command.dlcargs.wrong%"));
                sender.sendMessage("spet install [DLC]");
                sender.sendMessage("spet uninstall [DLC]");
                return true;
            }
            String dlc = args[1];
            if(args[0].equals("install")) {
                DLCManager.registerDLC(dlc);
            }else {
                DLCManager.uninstallDLC(dlc);
            }
        }
        return true;
    }
}
