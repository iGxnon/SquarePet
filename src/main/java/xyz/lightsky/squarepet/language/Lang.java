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

    public static final int langFileCount = 3;

    public static final Config sysLang = new Config();

    public static final Config uiLang = new Config();

    public static final Config userLang = new Config();

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
            .build();

    public static void init() {
        File langDir = new File(Main.getInstance().getDataFolder() + "/Languages/");
        if(langDir.mkdirs()) {
            Main.getInstance().saveResource("Languages/chs/sys.yml");
            Main.getInstance().saveResource("Languages/chs/ui.yml");
            Main.getInstance().saveResource("Languages/chs/user.yml");
            Main.getInstance().saveResource("Languages/eng/sys.yml");
            Main.getInstance().saveResource("Languages/eng/ui.yml");
            Main.getInstance().saveResource("Languages/eng/user.yml");
            Main.getInstance().saveResource("Languages/cht/sys.yml");
            Main.getInstance().saveResource("Languages/cht/ui.yml");
            Main.getInstance().saveResource("Languages/cht/user.yml");
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
        throw  new LanguageNotFoundException(key);
    }

    public static void tryCheckLangKey(String key) throws LanguageNotFoundException {
        if(!availableKeyList.contains(key)) {
            throw new LanguageNotFoundException(key);
        }
    }

}
