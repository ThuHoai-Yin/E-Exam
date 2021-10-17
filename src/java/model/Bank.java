package model;

import java.util.Date;
import java.util.List;

public class Bank {
    private int bankID;
    private int creatorID;
    private List<Question> questions;
    private Date dateCreated;

    public Bank(int bankID, int creatorID, List<Question> questions, Date dateCreated) {
        this.bankID = bankID;
        this.creatorID = creatorID;
        this.questions = questions;
        this.dateCreated = dateCreated;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }
    
    public int getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    } 

    @Override
    public String toString() {
        return "Bank{" + "bankID=" + bankID + ", creatorID=" + creatorID + ", questions=" + questions + ", dateCreated=" + dateCreated + '}';
    }
}
