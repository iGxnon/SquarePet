package xyz.lightsky.SquarePet.Exception;

public class DLCLoadException extends Exception {

    public DLCLoadException(String dlc, String cause) {
        super("DLC: " + dlc + " load failed! Cause: " + cause);
    }

}
