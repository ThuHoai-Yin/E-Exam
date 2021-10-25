package model;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class Exam {

    private String examCode;
    private List<Question> questions;
    private Timestamp openDate;
    private Timestamp closeDate;
    private Timestamp examEndTime;
    private int duration;

    public Exam(String examCode, List<Question> questions, Timestamp openDate, Timestamp closeDate, Timestamp examEndTime, int duration) {
        this.examCode = examCode;
        this.questions = questions;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.examEndTime = examEndTime;
        this.duration = duration;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Timestamp getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Timestamp openDate) {
        this.openDate = openDate;
    }

    public Timestamp getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Timestamp closeDate) {
        this.closeDate = closeDate;
    }

    public Timestamp getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(Timestamp examEndTime) {
        this.examEndTime = examEndTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public void shuffle() {
        Collections.shuffle(this.questions);
    }

    @Override
    public String toString() {
        return "Test{" + "examCode=" + examCode + ", questions=" + questions + ", examEndTime=" + examEndTime + ", duration=" + duration + '}';
    }
}
