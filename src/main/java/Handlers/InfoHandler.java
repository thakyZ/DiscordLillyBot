package Handlers;

import Main.Constants;
import Main.TagSystem;
import Main.Utility;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This Class Is the Handler for the updateInfo Method it pulls the info.txt doc and then updates the channel based on the contents of the file.
 */
public class InfoHandler {
    private IGuild guild;
    private IChannel channel;
    List<String> infoContents;

    public InfoHandler(IChannel channel, IGuild guild) {
        this.channel = channel;
        this.guild = guild;
        infoContents = FileHandler.readFromFile(Utility.getFilePath(guild.getID(), Constants.FILE_INFO));
        updateChannel();
    }

    private void updateChannel() {
        Utility.deleteMessage(channel.getMessages());
        StringBuilder builder = new StringBuilder();
        ArrayList<String> stringChunks = new ArrayList<>();
        String lastChunk;
        String nextChunk;
        String imageTag;
        String[] splited;
        String lastAttepmt;
        String imagePrefix = "#image#{";
        String imageSuffix = "}";
        String tagBreak = "#split#";
        String image;

        String[] contentsSplit;
        //prep for the everything...
        for (String s: infoContents){
            //this ignores commented out code.
            if (!s.startsWith("//")){
                if (builder.toString().contains(imagePrefix)){
                    imageTag = imagePrefix + StringUtils.substringBetween(builder.toString(),imagePrefix,imageSuffix) + imageSuffix;
                    splited = builder.toString().split(Pattern.quote(imageTag));
                    lastChunk = splited[0];
                    nextChunk = splited[1];
                    stringChunks.add(lastChunk);
                    stringChunks.add(imageTag);
                    builder.delete(0,builder.length());
                    builder.append(nextChunk);
                }
                if (builder.toString().contains(tagBreak)){
                    splited = builder.toString().split(Pattern.quote(tagBreak));
                    lastChunk = splited[0];
                    nextChunk = splited[1];
                    stringChunks.add(lastChunk);
                    builder.delete(0,builder.length());
                    builder.append(nextChunk);
                }
                if ((builder + doTextTags(s) + "\n").length() > 2000){
                    stringChunks.add(builder.toString());
                    builder.delete(0, builder.length());
                }
                builder.append(doTextTags(s) + "\n");
            }
        }
        stringChunks.add(builder.toString());
        builder.delete(0, builder.length());


        //actual code.
        for (String contents : stringChunks){
            contents = TagSystem.tagNoNL(contents);
            contents = TagSystem.tagEmoji(contents,guild);
            if (contents.contains(imagePrefix)){
                image = StringUtils.substringBetween(contents, imagePrefix, imageSuffix);
                File file = new File(Utility.getGuildImageDir(guild.getID()) + image);
                Utility.sendFile("",file,channel);
            }else {
                Utility.sendMessage(contents,channel);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        for (String contents : stringChunks) {
//            try {
//                contents = TagSystem.tagNoNL(contents);
//                contents = TagSystem.tagEmoji(contents,guild);
//                //test to see if adding the next set will make the message to long and stops that.
//                if ((builder.toString() + contents).length() > 1800) {
//                    Utility.sendMessage(builder.toString(), channel);
//                    Thread.sleep(2000);
//                    builder.delete(0, builder.length());
//                }
//                do {
//                    lastAttepmt = contents;
//                    String toAppend = "";
//                    if (contents.contains(imagePrefix)) {
//                        //getting image file location
//                        image = StringUtils.substringBetween(contents, imagePrefix, imageSuffix);
//                        //splitting it so that the stuff before the image can be sent
//                        contentsSplit = contents.split(Pattern.quote(imagePrefix + image + imageSuffix));
//
//                        //if the first part of the array isn't empty get that.
//                        if (contentsSplit.length != 0) {
//                            toAppend = contentsSplit[0];
//                        }
//                        //append that to the thinger
//                        builder.append(toAppend);
//                        //send what you have already and then clear the builder.
//                        Utility.sendMessage(builder.toString(), channel);
//                        builder.delete(0, builder.length());
//                        Thread.sleep(2000);
//                        //clear the stuff that was just sent and the the image code from things
//                        contents = contents.replaceFirst(Pattern.quote(toAppend + imagePrefix + image + imageSuffix), "");
//                        //set up the file
//                        File file = new File(Utility.getGuildImageDir(guild.getID()) + image);
//                        //if it exists send it if not send and error
//                        if (!file.exists()) {
//                            Utility.sendMessage(file.getPath() + " Does not Exist", channel);
//                            Thread.sleep(2000);
//                        } else {
//                            Utility.sendFile("", channel, file);
//                        }
//                    }
//                } while (!contents.equals(lastAttepmt) && contents.contains(imagePrefix));
//                builder.append(contents);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        //after everything send the last set
//        Utility.sendMessage(builder.toString(), channel);
    }


    private String doTextTags(String s) {
        s = TagSystem.tagChannel(s);
        s = TagSystem.tagDisplayName(s, guild);
        s = TagSystem.tagSpacer(s);
        return s;
    }


}
