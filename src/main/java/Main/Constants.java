package Main;

/**
 * Created by Vaerys on 03/08/2016.
 */
public class Constants {

    //Command prefix constants
    public static final String PREFIX_COMMAND = "$";
    public static final String PREFIX_CC = "$$";
    public static final String PREFIX_INDENT = "    ";
    public static final String PREFIX_EDT_LOGGER_INDENT = "                                     ";


    //-------Command Constants---------

    //Tag Types
    public static final String TAG_TYPE_ALL = "all";
    public static final String TAG_TYPE_CC = "CC";
    public static final String TAG_TYPE_ADMIN = "Admin";
    public static final String TAG_TYPE_INFO = "Info";

    //------------------------------------

    //Error Constants
    public static final String ERROR = "> An Error Occurred";
    public static final String ERROR_ROLE_NOT_FOUND = "> Role with that name not found.";
    public static final String ERROR_UPDATING_ROLE = "> An Error Occurred when trying to Update your roles.";
    public static final String ERROR_NIH = "> Not Yet Implemented.";
    public static final String ERROR_CC_NOT_FOUND = "> Command with that name could not be found.";
    public static final String ERROR_CHAR_NOT_FOUND = "> Character with that name could not be found.";


    //-------FilePath Constants--------

    //Directories
    public static final String DIRECTORY_STORAGE = "Storage/";
    public static final String DIRECTORY_BACKUPS = DIRECTORY_STORAGE + "Backups/";
    public static final String DIRECTORY_GLOBAL_IMAGES = DIRECTORY_STORAGE + "Images/";
    public static final String DIRECTORY_COMP = DIRECTORY_STORAGE + "Competition/";
    public static final String DIRECTORY_GUILD_IMAGES = "Images/";
    public static final String DIRECTORY_TEMP = DIRECTORY_STORAGE + "Temp/";
    public static final String DIRECTORY_OLD_FILES = DIRECTORY_STORAGE + "Old_Files/";
    public static final String DIRECTORY_ERROR = DIRECTORY_STORAGE + "Error/";


    //Files
    public static final String FILE_TOKEN = DIRECTORY_STORAGE + "Token.txt";
    public static final String FILE_CUSTOM = "Custom_Commands.json";
    public static final String FILE_GUILD_CONFIG = "Guild_Config.json";
    public static final String FILE_SERVERS = "Servers.json";
    public static final String FILE_CHARACTERS = "Characters.json";
    public static final String FILE_INFO = "Info.txt";
    public static final String FILE_CONFIG = DIRECTORY_STORAGE + "Config.json";
    public static final String FILE_CONFIG_BACKUP = DIRECTORY_BACKUPS + "Config.json";
    public static final String FILE_GLOBAL_DATA = DIRECTORY_STORAGE + "Global_Data.json";
    public static final String FILE_GLOBAL_DATA_BACKUP = DIRECTORY_BACKUPS + "Global_Data.json";

    public static final String FILE_COMPETITION = "Competition.json";


    //Special Messages
    public static final String DAILY_MESSAGE_1 = "> Mow WOW!";
    public static final String DAILY_MESSAGE_2 = "> I love Kitty Treats!";
    public static final String DAILY_MESSAGE_4 = "> HI TOBY! HOW ARE YOU!?!?";
    public static final String DAILY_MESSAGE_5 = "> Can I haz Kitty Treats?";
    public static final String DAILY_MESSAGE_3 = "> There is something out side in the backyard!";
    public static final String DAILY_MESSAGE_6 = "> I wanna play!";
    public static final String DAILY_MESSAGE_7 = "> Give me your finger so I can suckle on it!";


}
