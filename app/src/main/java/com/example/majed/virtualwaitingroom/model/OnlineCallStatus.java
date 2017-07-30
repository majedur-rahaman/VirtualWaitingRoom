package com.example.majed.virtualwaitingroom.model;

/**
 * Created by majed on 7/28/2017.
 */

public class OnlineCallStatus {
    private int Id;
    private String CalleeId;
    private boolean IsRead;

    public OnlineCallStatus(int id, String calleeId, boolean isRead) {
        Id = id;
        CalleeId = calleeId;
        IsRead = isRead;
    }
}
