package com.github.vaerys.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;

public class InitEvent {

    final static Logger logger = LoggerFactory.getLogger(Main.class);

    @EventSubscriber
    public void initBot(ReadyEvent event) {
        //makes sure that nothing in the config file will cause an error
        if (!Globals.isCreatorValid()) {
            System.exit(Constants.EXITCODE_STOP);
        }
//        if (args.length > 0 && args[0].equals("-w")) {
//            WikiBuilder.handleCommandLists();
//        }
        Main.consoleInput();
    }

}
