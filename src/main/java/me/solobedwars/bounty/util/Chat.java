package me.solobedwars.bounty.util;

import me.lucko.helper.text3.Text;

public class Chat {

    public static String colorFormat(String string, Object... objects) {

        return Text.colorize(String.format(string, objects));

    }

}
