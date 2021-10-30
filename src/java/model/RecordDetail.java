package model;

import java.util.Set;

public class RecordDetail {
    private String examName;
    private String studentFullName;
    private Set<Integer> selectedAnswers;

    public RecordDetail(String examName, String studentFullName, Set<Integer> selectedAnswers) {
        this.examName = examName;
        this.studentFullName = studentFullName;
        this.selectedAnswers = selectedAnswers;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }

    public Set<Integer> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(Set<Integer> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }
}
