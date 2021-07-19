package xyz.lightsky.SquarePet.dlc;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Utils;
import com.google.common.base.Preconditions;
import xyz.lightsky.SquarePet.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BaseDLC {

    private Server server;
    private boolean isEnabled = false;
    private boolean initialized = false;
    private PluginDescription description;
    private File file;
    private DLCLogger logger;
    private File dataFolder;

    public void init(Server server, PluginDescription description, File file) {
        if(!initialized) {
            this.description = description;
            this.server = server;
            this.file = file;
            logger = new DLCLogger(this);
            initialized = true;
            dataFolder = new File(Main.getInstance().getDataFolder() + "/DLC/", getName());
        }
    }

    public DLCLogger getLogger() {
        return  this.logger;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getName() {
        return this.description.getName();
    }

    public PluginDescription getDescription() {
        return description;
    }

    public Server getServer() {
        return server;
    }

    protected File getFile() {
        return file;
    }

    protected File getDataFolder() {
        return dataFolder;
    }

    protected void saveDefaultConfig() {
        saveResource("setting.yml");
    }

    public boolean saveResource(String filename) {
        return saveResource(filename, false);
    }

    public boolean saveResource(String filename, boolean replace) {
        return saveResource(filename, filename, replace);
    }

    public boolean saveResource(String filename, String outputName, boolean replace) {
        Preconditions.checkArgument(filename != null && outputName != null, "Filename can not be null!");
        Preconditions.checkArgument(!filename.trim().isEmpty() && !outputName.trim().isEmpty(), "Filename can not be empty!");

        File out = new File(dataFolder, outputName);
        if (!out.exists() || replace) {
            try (InputStream resource = getResource(filename)) {
                if (resource != null) {
                    File outFolder = out.getParentFile();
                    if (!outFolder.exists()) {
                        outFolder.mkdirs();
                    }
                    Utils.writeFile(out, resource);

                    return true;
                }
            } catch (IOException e) {
                Server.getInstance().getLogger().logException(e);
            }
        }
        return false;
    }

    public InputStream getResource(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    protected Config getConfig() {
        return new Config(new File(getDataFolder(), "setting.yml"));
    }


}
