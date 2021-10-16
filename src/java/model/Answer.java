package model;

public class Answer {
    private int answerID;
    private String content;
    private boolean selected = false;

    public Answer(int answerID, String content) {
        this.answerID = answerID;
        this.content = content;
    }

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }
 
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }    

    @Override
    public String toString() {
        return "Answer{" + "content=" + content + ", selected=" + selected + '}';
    }  
}
