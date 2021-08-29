package xyz.lightsky.squarepet.exception;

public class DLCLoadException extends Exception {

    public DLCLoadException(String dlc, String cause) {
        super("DLC: " + dlc + " load failed! Cause: " + cause);
    }

}
