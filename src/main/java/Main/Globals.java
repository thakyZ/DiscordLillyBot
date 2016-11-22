package Main;

import sx.blah.discord.api.IDiscordClient;

/**
 * Created by Vaerys on 14/08/2016.
 */
public class Globals {

    //CoolDowns
    public static int serverCoolDown = 0;
    public static int doAdminMention = 0;

    //Console Bot message Variables
    public static String consoleMessageCID = "203694223596191744";
    public static String creatorID = "83984886494400512";
    public static String botName = "LillyBot";
    public static IDiscordClient client;
    public static int argsMax = 500;
    public static int maxWarnings = 3;
    public static boolean isReady = false;

    public static IDiscordClient getClient() {
        return client;
    }

    public static void setClient(IDiscordClient client) {
        Globals.client = client;
    }
}
