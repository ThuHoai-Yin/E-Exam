package model;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class Test {

    private String testCode;
    private List<Question> questions;
    private Timestamp examEndTime;
    private int duration;

    public Test(String testCode, List<Question> questions, Timestamp examEndTime, int duration) {
        this.testCode = testCode;
        this.questions = questions;
        this.examEndTime = examEndTime;
        this.duration = duration;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
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
        return "Test{" + "testCode=" + testCode + ", questions=" + questions + ", examEndTime=" + examEndTime + ", duration=" + duration + '}';
    }
}
