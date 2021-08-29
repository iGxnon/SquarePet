package xyz.lightsky.squarepet.exception;

public class LanguageNotFoundException extends Exception {

    public LanguageNotFoundException(String key) {
        super("Language key: " + key + " cannot find, please confirm its existence");
    }

}
