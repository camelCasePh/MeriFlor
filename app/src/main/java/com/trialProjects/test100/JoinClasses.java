package com.trialProjects.test100;

public class JoinClasses {

    private String classID, studentID, className, classSection;

    public JoinClasses(){

    }

    public JoinClasses(String classID, String studentID, String className, String classSection) {
        this.classID = classID;
        this.studentID = studentID;
        this.className = className;
        this.classSection = classSection;
    }

    public String getClassID() {
        return classID;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getClassName() {
        return className;
    }

    public String getClassSection() {
        return classSection;
    }

}
