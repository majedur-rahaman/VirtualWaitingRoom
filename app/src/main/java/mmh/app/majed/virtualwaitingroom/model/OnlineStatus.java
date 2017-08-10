package mmh.app.majed.virtualwaitingroom.model;

/**
 * Created by majed on 7/31/2017.
 */

public class OnlineStatus {

    public String contactId;
    public int onlinestatus;

    public OnlineStatus(String contactId, int onlineStatus) {
        this.contactId = contactId;
        this.onlinestatus = onlineStatus;
    }

    public String getContactId() {
        return contactId;
    }

    public int getOnlineStatus() {
        return onlinestatus;
    }
}
