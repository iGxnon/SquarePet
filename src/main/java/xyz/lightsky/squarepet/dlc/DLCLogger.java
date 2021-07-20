package xyz.lightsky.squarepet.dlc;

import cn.nukkit.utils.LogLevel;
import cn.nukkit.utils.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class DLCLogger implements Logger {

    private final String pluginName;
    private final org.apache.logging.log4j.Logger log;

    public DLCLogger(BaseDLC dlc) {
        this.log = LogManager.getLogger(dlc.getDescription().getMain());
        this.pluginName = dlc.getName();
    }

    public void emergency(String message) {
        this.log(LogLevel.EMERGENCY, message);
    }

    public void alert(String message) {
        this.log(LogLevel.ALERT, message);
    }

    public void critical(String message) {
        this.log(LogLevel.CRITICAL, message);
    }

    public void error(String message) {
        this.log(LogLevel.ERROR, message);
    }

    public void warning(String message) {
        this.log(LogLevel.WARNING, message);
    }

    public void notice(String message) {
        this.log(LogLevel.NOTICE, message);
    }

    public void info(String message) {
        this.log(LogLevel.INFO, message);
    }

    public void debug(String message) {
        this.log(LogLevel.DEBUG, message);
    }

    private Level toApacheLevel(LogLevel level) {
        switch(level) {
            case NONE:
                return Level.OFF;
            case EMERGENCY:
            case CRITICAL:
                return Level.FATAL;
            case ALERT:
            case WARNING:
            case NOTICE:
                return Level.WARN;
            case ERROR:
                return Level.ERROR;
            case DEBUG:
                return Level.DEBUG;
            default:
                return Level.INFO;
        }
    }

    public void log(LogLevel level, String message) {
        this.log.log(this.toApacheLevel(level), "DLC: [{}]: {}", this.pluginName, message);
    }

    public void emergency(String message, Throwable t) {
        this.log(LogLevel.EMERGENCY, message, t);
    }

    public void alert(String message, Throwable t) {
        this.log(LogLevel.ALERT, message, t);
    }

    public void critical(String message, Throwable t) {
        this.log(LogLevel.CRITICAL, message, t);
    }

    public void error(String message, Throwable t) {
        this.log(LogLevel.ERROR, message, t);
    }

    public void warning(String message, Throwable t) {
        this.log(LogLevel.WARNING, message, t);
    }

    public void notice(String message, Throwable t) {
        this.log(LogLevel.NOTICE, message, t);
    }

    public void info(String message, Throwable t) {
        this.log(LogLevel.INFO, message, t);
    }

    public void debug(String message, Throwable t) {
        this.log(LogLevel.DEBUG, message, t);
    }

    public void log(LogLevel level, String message, Throwable t) {
        this.log.log(this.toApacheLevel(level), "DLC: [{}]: {}", this.pluginName, message, t);
    }
}
