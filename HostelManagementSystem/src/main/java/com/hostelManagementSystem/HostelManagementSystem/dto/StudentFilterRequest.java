package com.hostelManagementSystem.HostelManagementSystem.dto;

public class StudentFilterRequest {

    private String faculty;
    private String intake;
    private String gender;
    private String criteriaType;
    private Integer distanceThreshold;
    private Integer studentCount;

    public StudentFilterRequest() {
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCriteriaType() {
        return criteriaType;
    }

    public void setCriteriaType(String criteriaType) {
        this.criteriaType = criteriaType;
    }

    public Integer getDistanceThreshold() {
        return distanceThreshold;
    }

    public void setDistanceThreshold(Integer distanceThreshold) {
        this.distanceThreshold = distanceThreshold;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public void printRequest(){
        System.out.println("** Request Details **");
        System.out.println("\tFaculty            : " + this.faculty +
                           "\n\tIntake             : " + this.intake +
                           "\n\tGender             : " + this.gender +
                           "\n\tCriteria Type      : " + this.criteriaType +
                           "\n\tDistance Threshold : " + this.distanceThreshold +
                           "\n\tStudent Count      : " + this.studentCount);
    }
}
