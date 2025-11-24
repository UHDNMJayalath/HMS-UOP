package com.hostelManagementSystem.HostelManagementSystem.dto;

import java.util.List;

public class RoomAllocationRequest {

    private String roomNo;
    private List<String> studentIds;

    public RoomAllocationRequest() {
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
}
