package me.solobedwars.bounty.api;

public class Placeholder {

    private final String placeholder;
    private final String value;

    public Placeholder(String placeholder, String toReplace) {

        this.placeholder = placeholder;
        this.value = toReplace;

    }

    public static Placeholder of(String placeholder, String toReplace) {
        return new Placeholder(placeholder, toReplace);

    }

    public String replace(String text) {
        return text.replaceAll("\\{" + this.placeholder + "\\}", this.value);
    }


}
