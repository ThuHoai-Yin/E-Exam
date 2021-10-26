package model;

import java.sql.Timestamp;

public class Record {
    private String examCode;
    private int recordID;
    private int studentID;
    private String studentFullname;
    private Timestamp examDate;
    private Timestamp dateSubmitted;
    private int numOfCorrectAnswers;
    private int numOfAnswers;

    public Record(String examCode, int recordID, int studentID, String studentFullname, Timestamp examDate, Timestamp dateSubmitted, int numOfCorrectAnswers, int numOfAnswers) {
        this.examCode = examCode;
        this.recordID = recordID;
        this.studentID = studentID;
        this.studentFullname = studentFullname;
        this.examDate = examDate;
        this.dateSubmitted = dateSubmitted;
        this.numOfCorrectAnswers = numOfCorrectAnswers;
        this.numOfAnswers = numOfAnswers;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getStudentFullname() {
        return studentFullname;
    }

    public void setStudentFullname(String studentFullname) {
        this.studentFullname = studentFullname;
    }

    public Timestamp getExamDate() {
        return examDate;
    }

    public void setExamDate(Timestamp examDate) {
        this.examDate = examDate;
    }

    public Timestamp getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Timestamp dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public int getNumOfCorrectAnswers() {
        return numOfCorrectAnswers;
    }

    public void setNumOfCorrectAnswers(int numOfCorrectAnswers) {
        this.numOfCorrectAnswers = numOfCorrectAnswers;
    }

    public int getNumOfAnswers() {
        return numOfAnswers;
    }

    public void setNumOfAnswers(int numOfAnswers) {
        this.numOfAnswers = numOfAnswers;
    }

    @Override
    public String toString() {
        return "Record{" + "recordID=" + recordID + ", studentID=" + studentID + ", studentFullname=" + studentFullname + ", examDate=" + examDate + ", dateSubmitted=" + dateSubmitted + ", numOfCorrectAnswers=" + numOfCorrectAnswers + ", numOfAnswers=" + numOfAnswers + '}';
    }
}