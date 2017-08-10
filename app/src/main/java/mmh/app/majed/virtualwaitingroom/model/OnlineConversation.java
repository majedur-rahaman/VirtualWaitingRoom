package mmh.app.majed.virtualwaitingroom.model;

/**
 * Created by majed on 7/27/2017.
 */

public class OnlineConversation {
    public int Id;
    public String CallerId;
    public String CalleeId;
    public String CallerName;
    public String CalleeName;
    public String Url;
    public String Message;
    public boolean IsRead;

    public OnlineConversation(int id, String callerId, String calleeId, String callerName, String calleeName, String url, String message, boolean isRead) {
        Id = id;
        CallerId = callerId;
        CalleeId = calleeId;
        CallerName = callerName;
        CalleeName = calleeName;
        Url = url;
        Message = message;
        IsRead = isRead;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCallerId() {
        return CallerId;
    }

    public void setCallerId(String callerId) {
        CallerId = callerId;
    }

    public String getCalleeId() {
        return CalleeId;
    }

    public void setCalleeId(String calleeId) {
        CalleeId = calleeId;
    }

    public String getCallerName() {
        return CallerName;
    }

    public void setCallerName(String callerName) {
        CallerName = callerName;
    }

    public String getCalleeName() {
        return CalleeName;
    }

    public void setCalleeName(String calleeName) {
        CalleeName = calleeName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isRead() {
        return IsRead;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }
}
