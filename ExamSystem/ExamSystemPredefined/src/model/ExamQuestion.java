package model;

/**
 * @author: Yiqing Wang
 */


import java.util.Objects;

public class ExamQuestion implements java.io.Serializable {
    private static final long serialVersionUID = 4729638835655094399L;

    private String question;  // 考题的题干
    private String choiceA;   // 选项A
    private String choiceB;   // 选项B
    private String choiceC;   // 选项C
    private String choiceD;   // 选项D
    private String answer ;   // 正确答案

    public ExamQuestion() {
    }

    public ExamQuestion(String question, String choiceA, String choiceB, String choiceC, String choiceD) {
        this.question = question;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
    }

    public ExamQuestion(String question, String choiceA, String choiceB, String choiceC, String choiceD, String answer) {
        this.question = question;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    // 管理员能看到所有信息
    @Override
    public String toString() {
        return "-------------------------------------\n" +
                question + '\n' +
                ", A. " + choiceA + '\t' +
                ", B. " + choiceB + '\n' +
                ", C. " + choiceC + '\t' +
                ", D. " + choiceD + '\n' +
                ", Correct Answer: " + answer +
                "\n-------------------------------------";
    }

    // 学生不能看到正确答案
    public String showToStudents() {
        return "-------------------------------------\n" +
                question + '\n' +
                ", A. " + choiceA + '\t' +
                ", B. " + choiceB + '\n' +
                ", C. " + choiceC + '\t' +
                ", D. " + choiceD +
                "\n-------------------------------------";
    }

    // 一份试卷中题干不能重复，
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamQuestion that = (ExamQuestion) o;
        return Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question);
    }
}


