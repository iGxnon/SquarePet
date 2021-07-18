package xyz.lightsky.SquarePet.exception;

public class DLCLoadException extends Exception {

    public DLCLoadException(String dlc, String cause) {
        super("DLC: " + dlc + " load failed! Cause: " + cause);
    }

}
