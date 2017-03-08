package Main;

import Handlers.FileHandler;
import Listeners.AnnotationListener;
import POGOs.Config;
import POGOs.GlobalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Vaerys on 19/05/2016.
 */
public class Main {


    final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws UnknownHostException {
        System.out.println("Starting Program...");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info(">>> Running Shutdown Process <<<");
                Globals.saveFiles();
            }
        });

        String token;
        // you need to set a token in Token/Token.txt for the bot to run
        try {
            Discord4J.disableAudio();
            FileHandler.createDirectory(Constants.DIRECTORY_STORAGE);
            FileHandler.createDirectory(Constants.DIRECTORY_GLOBAL_IMAGES);
            FileHandler.createDirectory(Constants.DIRECTORY_COMP);
            FileHandler.createDirectory(Constants.DIRECTORY_BACKUPS);
            FileHandler.createDirectory(Constants.DIRECTORY_TEMP);
            FileHandler.createDirectory(Constants.DIRECTORY_OLD_FILES);
            FileHandler.createDirectory(Constants.DIRECTORY_ERROR);
            if (!Files.exists(Paths.get(Constants.FILE_CONFIG))) {
                FileHandler.writeToJson(Constants.FILE_CONFIG, new Config());
            }
            if (!Files.exists(Paths.get(Constants.FILE_GLOBAL_DATA))) {
                FileHandler.writeToJson(Constants.FILE_GLOBAL_DATA, new GlobalData());
            }

            //load config phase 1
            Config config = (Config) FileHandler.readFromJson(Constants.FILE_CONFIG, Config.class);
            GlobalData globalData = (GlobalData) FileHandler.readFromJson(Constants.FILE_GLOBAL_DATA, GlobalData.class);
            config.initObject();
            FileHandler.writeToJson(Constants.FILE_CONFIG, config);

            //getting bot token
            token = FileHandler.readFromFile(Constants.FILE_TOKEN).get(0);
            if (token == null) {
                logger.error("!!!BOT TOKEN NOT VALID PLEASE CHECK \"Storage/Token.txt\" AND UPDATE THE TOKEN!!!");
            }

            IDiscordClient client = Client.getClient(token, false);

            //load config phase 2
            Globals.initConfig(client, config, globalData);

            //login + register listener.
            EventDispatcher dispatcher = client.getDispatcher();
            dispatcher.registerListener(new AnnotationListener());
            client.login();

            //timed events init
            new TimedEvents();

            while (!client.isReady()) ;

            //makes sure that nothing in the config file will cause an error
            Globals.validateConfig();
            Globals.setVersion();
            consoleInput();
        } catch (DiscordException ex) {
            logger.error(ex.getErrorMessage());
        } catch (RateLimitException e) {
            e.printStackTrace();
        }
    }

    private static void consoleInput() {
        Scanner scanner = new Scanner(System.in);
        while (!Globals.isReady) ;
        logger.info("Console input initiated.");

        while (scanner.hasNextLine()) {
            while (Globals.consoleMessageCID == null) ;
            IChannel channel = Globals.getClient().getChannelByID(Globals.consoleMessageCID);
            String message = scanner.nextLine();
            message = message.replace("#thakyZ#", Globals.getClient().getUserByID("83984886494400512").getName());
            message = message.replace("#Dawn#", Globals.getClient().getUserByID("153159020528533505").getName());
            message = message.replace("teh", "the");
            message = message.replace("Teh", "The");
            System.out.println(message);
            if (!message.equals("")) {
                Utility.sendMessage(message, channel);
            }
        }
    }
}
