package com.example.majed.virtualwaitingroom.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by majed on 7/25/2017.
 */

public class Doctor {

    @SerializedName("FullName")
    private String FullName;
    @SerializedName("ContactId")
    private String ContactId;
    @SerializedName("OnlineStatus")
    private String OnlineStatus;
    @SerializedName("OnlineStatusStr")
    private String OnlineStatusStr;

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getContactId() {
        return ContactId;
    }

    public void setContactId(String contactId) {
        ContactId = contactId;
    }

    public String getOnlineStatus() {
        return OnlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        OnlineStatus = onlineStatus;
    }

    public String getOnlineStatusStr() {
        return OnlineStatusStr;
    }

    public void setOnlineStatusStr(String onlineStatusStr) {
        OnlineStatusStr = onlineStatusStr;
    }
}
