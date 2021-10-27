package model;

import java.sql.Timestamp;
import java.util.List;

public class Bank {

    private int bankID;
    private String bankName;
    private String courseName;
    private int creatorID;
    private String creatorFullName;
    private List<Question> questions;
    private Timestamp dateCreated;

    public Bank(int bankID, String bankName, String courseName, int creatorID, String creatorFullName, List<Question> questions, Timestamp dateCreated) {
        this.bankID = bankID;
        this.bankName = bankName;
        this.courseName = courseName;
        this.creatorID = creatorID;
        this.creatorFullName = creatorFullName;
        this.questions = questions;
        this.dateCreated = dateCreated;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Bank{" + "bankID=" + bankID + ", bankName=" + bankName + ", courseName=" + courseName + ", creatorFullName=" + creatorFullName + ", questions=" + questions + ", dateCreated=" + dateCreated + '}';
    }

}
