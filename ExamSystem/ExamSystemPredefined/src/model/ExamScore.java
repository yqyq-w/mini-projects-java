package model;

/**
 * @author: Yiqing Wang
 */

import java.util.ArrayList;

public class ExamScore implements java.io.Serializable {
    private static final long serialVersionUID = 1225113970581096678L;

    // 计算某一门科目的成绩
    public static double getFinalScore(/*String examName,*/ ArrayList<String> stuAnswers, ArrayList<ExamQuestion> examContent) {
        double finalScore = 0;
        // 通过试卷名获得并遍历试卷库中的题目
        for (int i = 0; i < examContent.size(); i++) {
//            System.out.println(examContent.get(i).getAnswer());
            // 如果学生答案和答案一致，加一分
            if (examContent.get(i).getAnswer().equals(stuAnswers.get(i))) {
                finalScore++;
            }
        }
        // 换算成百分制
        finalScore = finalScore / examContent.size() * 100;
        return finalScore;
    }
}