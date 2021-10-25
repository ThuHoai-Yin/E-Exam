package model;

import java.util.Collections;
import java.util.List;

public class Question {
    private int questionID;
    private String content;
    private int maxChoose;
    private List<Answer> answers;

    public Question(int questionID, String content, int maxChoose, List<Answer> answers) {
        this.questionID = questionID;
        this.content = content;
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
    
    public void shuffle() {
        Collections.shuffle(this.answers);
    }

    @Override
    public String toString() {
        return "Question{" + "questionID=" + questionID + ", content=" + content + ",maxChoose=" + maxChoose + ", answers=" + answers + '}';
    }
}
