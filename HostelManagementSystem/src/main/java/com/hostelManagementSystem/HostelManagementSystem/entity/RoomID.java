package com.hostelManagementSystem.HostelManagementSystem.entity;

import java.io.Serializable;
import java.util.Objects;

public class RoomID implements Serializable {

    private Integer hostelId;
    private String roomNo;

    public RoomID(Integer hostelId, String roomNo) {
        this.hostelId = hostelId;
        this.roomNo = roomNo;
    }

    public RoomID() {
    }

    public Integer getHostelId() {
        return hostelId;
    }

    public void setHostelId(Integer hostelId) {
        this.hostelId = hostelId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hostelId, this.roomNo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RoomID)) return false;
        RoomID roomID = (RoomID) obj;
        return Objects.equals(hostelId, roomID.hostelId)  && Objects.equals(roomNo, roomID.roomNo);
    }
}
