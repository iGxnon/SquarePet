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
            .add("%user.%")
            .build();

    public static void init() {

        File langDir = new File(Main.getInstance().getDataFolder() + "/Languages/");
        if(langDir.mkdirs()) {
            Main.info("正在加载语言文件夹");
            Main.getInstance().saveResource("/Languages/");
        }
        Stream.of(Objects.requireNonNull(langDir.listFiles()))
                .filter(File::isDirectory)
                // filter irregular language that contains lang files count not equal to `langFileCount`
                .filter(s -> Objects.requireNonNull(s.listFiles()).length == langFileCount)
                .map(File::getName)
                .forEach(s -> availableLangList.add(s));
    }

    public static void loadLang(String lang) {
        if(!availableLangList.contains(lang)) {
            return;
        }
        sysLang.load(Main.getInstance().getDataFolder() + "/Languages/" + lang + "/sys.yml");
        uiLang.load(Main.getInstance().getDataFolder() + "/Languages/" + lang + "/ui.yml");
        userLang.load(Main.getInstance().getDataFolder() + "/Languages/" + lang + "/user.yml");
        Main.info("语言加载成功!");
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
