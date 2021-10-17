package model;

import java.util.List;

public class Question {
    private int questionID;
    private String content;
    private int mark;
    private int maxChoose;
    private List<Answer> answers;

    public Question(int questionID, String content, int mark, int maxChoose, List<Answer> answers) {
        this.questionID = questionID;
        this.content = content;
        this.mark = mark;
        this.maxChoose = maxChoose;
        this.answers = answers;
    }
    
    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionId) {
        this.questionID = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMaxChoose() {
        return maxChoose;
    }

    public void setMaxChoose(int maxChoose) {
        this.maxChoose = maxChoose;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    } 

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Question{" + "questionID=" + questionID + ", content=" + content + ", mark=" + mark + ", maxChoose=" + maxChoose + ", answers=" + answers + '}';
    }
}
