package xyz.lightsky.SquarePet.dlc;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginDescription;

import java.io.File;

public class BaseDLC {

    private Server server;
    private boolean isEnabled = false;
    private boolean initialized = false;
    private PluginDescription description;
    private File file;
    private DLCLogger logger;

    public void init(Server server, PluginDescription description, File file) {
        if(!initialized) {
            this.description = description;
            this.server = server;
            this.file = file;
            logger = new DLCLogger(this);
            initialized = true;
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


}
