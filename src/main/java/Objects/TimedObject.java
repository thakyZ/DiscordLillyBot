package Objects;


import java.util.ArrayList;

/**
 * Created by Vaerys on 30/09/2016.
 */
public class TimedObject {
    public int doAdminMention = 0;
    String guildID;
    ArrayList<UserCountDown> waiterObjects = new ArrayList<>();
    ArrayList<ReminderObject> reminderObjects = new ArrayList<>();

    public TimedObject(String guildID) {
        this.guildID = guildID;
    }

    public String getGuildID() {
        return guildID;
    }

    public int getDoAdminMention() {
        return doAdminMention;
    }

    public void setDoAdminMention(int doAdminMention) {
        this.doAdminMention = doAdminMention;
    }

    public ArrayList<UserCountDown> getWaiterObjects() {
        return waiterObjects;
    }

    public void setWaiterObjects(ArrayList<UserCountDown> waiterObjects) {
        this.waiterObjects = waiterObjects;
    }

    public boolean mannageWaiter(String userID) {
        int i = 0;
        for(UserCountDown w: waiterObjects){
            if (w.getID().equals(userID)){
                waiterObjects.remove(i);
                return true;
            }
            i++;
        }
        waiterObjects.add(new UserCountDown(userID,60));
        return false;
    }

    public boolean addReminderObject(ReminderObject object){
        for (ReminderObject r: reminderObjects) {
            if (object.getUserID().equals(r.getUserID())){
                return true;
            }
        }
        reminderObjects.add(object);
        return false;
    }

    public ArrayList<ReminderObject> getReminderObjects() {
        return reminderObjects;
    }

    public void setReminderObjects(ArrayList<ReminderObject> reminderObjects) {
        this.reminderObjects = reminderObjects;
    }
}
