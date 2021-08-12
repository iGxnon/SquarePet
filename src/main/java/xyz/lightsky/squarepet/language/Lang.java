package xyz.lightsky.squarepet.language;

import cn.nukkit.utils.Config;
import com.google.common.collect.ImmutableList;
import xyz.lightsky.squarepet.Main;
import xyz.lightsky.squarepet.exception.LanguageNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Lang {

    public static final int langFileCount = 4;

    public static final Config sysLang = new Config();

    public static final Config uiLang = new Config();

    public static final Config userLang = new Config();

    public static final Config propLang = new Config();

    public static List<String> availableLangList = new ArrayList<>();

    public static ImmutableList<String> availableKeyList = ImmutableList.<String>builder()
            .add("%sys.language.load.success%")
            .add("%sys.dlc.dir.loaded%")
            .add("%sys.dlc.list.loading%")
            .add("%sys.dlc.load.timing%")
            .add("%sys.config.loaded%")
            .add("%sys.dlc.notfind%")
            .add("%sys.dlc.loaded%")
            .add("%sys.dlc.load.success%")
            .add("%sys.market.dir.loaded%")
            .add("%sys.pet.dir.loaded%")
            .add("%sys.pet.register.success%")
            .add("%sys.trainer.dir.loaded%")
            .add("%sys.prop.load%")
            .add("%sys.skill.dir.loaded%")
            .add("%sys.skill.register.success%")
            .add("%sys.trainer.loading%")

            .add("%sys.command.dlcargs.wrong%")

            .add("%ui.market%")
            .add("%ui.market.switch.content%")
            .add("%ui.market.switch.pet%")
            .add("%ui.market.switch.prop%")
            .add("%ui.market.pet.content.tips%")
            .add("%ui.market.pet%")
            .add("%ui.market.buy%")
            .add("%ui.market.back%")
            .add("%ui.market.pet.info%")
            .add("%ui.market.buy.confirm%")
            .add("%ui.market.prop.switch.content%")
            .add("%ui.market.baseprop%")
            .add("%ui.market.skillstone%")
            .add("%ui.market.skillstone.info%")
            .add("%ui.market.skillstone.content%")
            .add("%ui.market.baseprop.info%")
            .add("%ui.market.baseprop.count%")

            .add("%ui.me.info.openbag%")
            .add("%ui.me.back%")
            .add("%ui.me.bag.title%")
            .add("%ui.me.bag.prop.info%")
            .add("%ui.me.bag.prop.use%")
            .add("%ui.me.bag.prop.choosepettouse%")
            .add("%ui.me.bag.prop.use.confirm%")

            .add("%ui.menu.main.title%")
            .add("%ui.menu.main.content%")
            .add("%ui.menu.main.mypet%")
            .add("%ui.menu.main.mypet.icon%")
            .add("%ui.menu.main.lineup%")
            .add("%ui.menu.main.lineup.icon%")
            .add("%ui.menu.main.givepet%")
            .add("%ui.menu.main.givepet.icon%")
            .add("%ui.menu.main.editpet%")
            .add("%ui.menu.main.editpet.icon%")
            .add("%ui.menu.main.market%")
            .add("%ui.menu.main.market.icon%")
            .add("%ui.menu.main.me%")
            .add("%ui.menu.main.me.icon%")
            .add("%ui.menu.main.op%")
            .add("%ui.menu.main.op.icon%")
            .add("%ui.menu.main.petchoose.give%")
            .add("%ui.menu.main.petchoose.edit%")
            .add("%ui.menu.confirm.content%")
            .add("%ui.menu.confirm.true%")
            .add("%ui.menu.confirm.false%")

            .add("%ui.op.title%")
            .add("%ui.op.edit.player%")
            .add("%ui.op.edit.market%")
            .add("%ui.op.edit.player.choose%")
            .add("%ui.op.switch.addpet%")
            .add("%ui.op.switch.delpet%")
            .add("%ui.op.switch.addprop%")
            .add("%ui.op.switch.addskill%")
            .add("%ui.op.switch.editprefix%")
            .add("%ui.op.switch.editlevel%")
            .add("%ui.op.addpet.choose%")
            .add("%ui.op.delpet.choose%")
            .add("%ui.op.addprop.choose%")
            .add("%ui.op.addskill.choose%")
            .add("%ui.op.addskill.target.content%")
            .add("%ui.op.editprefix.content%")
            .add("%ui.op.editlevel.content%")
            .add("%ui.op.edit.market.pet%")
            .add("%ui.op.edit.market.addmore%")
            .add("%ui.op.edit.market.pet.choose%")
            .add("%ui.op.edit.market.del.content%")
            .add("%ui.op.edit.market.editprice%")
            .add("%ui.op.edit.market.editprice.placeholder%")
            .add("%ui.op.edit.market.skill.choose%")
            .add("%ui.op.edit.market.prop.choose%")

            .add("%ui.pet.list.title%")
            .add("%ui.pet.spawn%")
            .add("%ui.pet.takeback%")
            .add("%ui.pet.back%")
            .add("%ui.pet.ride%")
            .add("%ui.pet.info%")
            .add("%ui.pet.lineup.title%")
            .add("%ui.pet.lineup.land.null%")
            .add("%ui.pet.lineup.fly.null%")
            .add("%ui.pet.lineup.swim.null%")
            .add("%ui.pet.lineup.land%")
            .add("%ui.pet.lineup.swim%")
            .add("%ui.pet.lineup.fly%")
            .add("%ui.pet.lineup.spawnall%")
            .add("%ui.pet.lineup.del.confirm%")
            .add("%ui.pet.lineup.add.confirm%")
            .add("%ui.pet.lineup.add.land%")
            .add("%ui.pet.lineup.add.swim%")
            .add("%ui.pet.lineup.add.fly%")
            .add("%ui.pet.givepet.dropdown%")
            .add("%ui.pet.edit.name%")
            .add("%ui.pet.edit.autoskill%")
            .add("%ui.pet.edit.confirm%")

            .build();

    public static void init() {
        File langDir = new File(Main.getInstance().getDataFolder() + "/Languages/");
        if(langDir.mkdirs()) {
            Main.getInstance().saveResource("Languages/chs/sys.yml");
            Main.getInstance().saveResource("Languages/chs/ui.yml");
            Main.getInstance().saveResource("Languages/chs/user.yml");
            Main.getInstance().saveResource("Languages/chs/prop.yml");
            Main.getInstance().saveResource("Languages/eng/sys.yml");
            Main.getInstance().saveResource("Languages/eng/ui.yml");
            Main.getInstance().saveResource("Languages/eng/user.yml");
            Main.getInstance().saveResource("Languages/eng/prop.yml");
            Main.getInstance().saveResource("Languages/cht/sys.yml");
            Main.getInstance().saveResource("Languages/cht/ui.yml");
            Main.getInstance().saveResource("Languages/cht/user.yml");
            Main.getInstance().saveResource("Languages/cht/prop.yml");
        }
        Stream.of(Objects.requireNonNull(langDir.listFiles()))
                .filter(File::isDirectory)
                // filter irregular language that contains lang files count not equal to `langFileCount`
                .filter(s -> Objects.requireNonNull(s.listFiles()).length == langFileCount)
                .map(File::getName)
                .forEach(s -> availableLangList.add(s));

        // ConfigManager is load behind Lang, so do not use ConfigManager.getLanguage()
        loadLang(Main.getInstance().getConfig().getString("language"));
    }

    public static void loadLang(String lang) {
        if(!availableLangList.contains(lang)) {
            new LanguageNotFoundException(lang).printStackTrace();
            return;
        }
        sysLang.load(Main.getInstance().getDataFolder() + "/Languages/" + lang + "/sys.yml", Config.YAML);
        uiLang.load(Main.getInstance().getDataFolder() + "/Languages/" + lang + "/ui.yml", Config.YAML);
        userLang.load(Main.getInstance().getDataFolder() + "/Languages/" + lang + "/user.yml", Config.YAML);
        propLang.load(Main.getInstance().getDataFolder() + "/Languages/" + lang + "/prop.yml", Config.YAML);
        Main.info(Lang.translate("%sys.language.load.success%"));
    }

    public static String translate(String key) {
        try {
            return tryTranslate(key);
        } catch (LanguageNotFoundException e) {
            e.printStackTrace();
            return "language error, please notify server manager to solve it";
        }
    }

    public static String tryTranslate(String key) throws LanguageNotFoundException {
        tryCheckLangKey(key);
        if(sysLang.exists(key)) {
            return sysLang.getString(key);
        }
        if(uiLang.exists(key)) {
            return uiLang.getString(key);
        }
        if(userLang.exists(key)) {
            return userLang.getString(key);
        }
        if(propLang.exists(key)) {
            return propLang.getString(key);
        }
        throw  new LanguageNotFoundException(key);
    }

    public static void tryCheckLangKey(String key) throws LanguageNotFoundException {
        if(!availableKeyList.contains(key)) {
            throw new LanguageNotFoundException(key);
        }
    }

}
