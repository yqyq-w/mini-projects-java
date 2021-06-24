package server;

/**
 * @author: Yiqing Wang
 */


import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 编程实现数据的存取
 */
public class ServerDao {

    // 简易数据库读取
    private HashMap<String, User> users = new HashMap<>();
    private HashMap<String, Student> studentInfo = new HashMap<>();
    private HashMap<String, ArrayList<ExamQuestion>> exams = new HashMap<>();
    // 测试读写路径
    private String usersFilePath = "./ExamSystemDB/users.txt"; // 登录用户库读写路径
    private String stuInfoFilePath = "./ExamSystemDB/students.txt";  // 学生库读写路径
    private String examsFilePath = "./ExamSystemDB/exams.txt";  // 考卷库读写路径
    private String examContentPath = "./ExamSystemDB/exam_1.txt"; // 测试上传考题路径，其中有错选项和重复题目
    private String examScorePath = "./ExamSystemDB/exam_scores.txt"; // 测试下载成绩单路径
    // IO流
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private static PrintStream ps;
    private static BufferedReader br;
    /*
    关闭流
     */
    public static void closeStream() {
        if (!(null == ois)) {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!(null == oos)) {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!(null == br)) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ps.close();
    }

    /**
     * 编程实现管理员账号和密码的校验并将结果返回出去
     * @param user
     * @return
     */
    public boolean serverManagerCheck(User user) {
        if ("admin".equals(user.getUserName()) && "123456".equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * 编程实现学员账号和密码的校验并将结果返回出去
     * @param user
     * @return
     */
    public boolean serverStudentCheck(User user) {
        String id = user.getUserName();
        String pass = user.getPassword();
        if (users.containsKey(id) && pass.equals(users.get(id).getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * 写入登录用户对象
     *
     * 当希望将多个对象写入文件时，通常建议将多个对象放入一个集合中，然后将集合这个整体看做一个对象写入输出流中，
     * 此时只需要调用一次readObject方法就可以将整个集合的数据读取出来，从而避免了通过返回值进行是否达到文件末尾的判断。
     */
    public void serverWriteUsers(){
        try {
            oos = new ObjectOutputStream(new FileOutputStream(usersFilePath));
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取上次退出前保存的用户对象
     */
    public void serverReadUsers(){
        try {
            ois = new ObjectInputStream(new FileInputStream(usersFilePath));
            // 默认Object类型，需要强转
            users = (HashMap<String, User>) ois.readObject();
            System.out.println("users: " + users);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入学生对象
     */
    public void serverWriteStudentInfo(){
        try {
            oos = new ObjectOutputStream(new FileOutputStream(stuInfoFilePath));
            oos.writeObject(studentInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取上次退出前保存的学生对象
     */
    public void serverReadStudentInfo(){
        try {
            ois = new ObjectInputStream(new FileInputStream(stuInfoFilePath));
            // 默认Object类型，需要强转
            studentInfo = (HashMap<String, Student>) ois.readObject();

            System.out.println("studentInfo: " + studentInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入试卷对象
     */
    public void serverWriteExams(){
        try {
            oos = new ObjectOutputStream(new FileOutputStream(examsFilePath));
            oos.writeObject(exams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取上次退出前保存的试卷对象
     */
    public void serverReadExams(){
        try {
            ois = new ObjectInputStream(new FileInputStream(examsFilePath));
            // 默认Object类型，需要强转
            exams = (HashMap<String, ArrayList<ExamQuestion>>) ois.readObject();
            System.out.println("exams: " + exams);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现增加学生功能
     * @param s
     * @return
     */
    public int serverAddStudent(Student s) {
        // 先判断年龄和学号是否合理
        if (s.getAge() >= 4 && s.getAge() <= 60 && s.getId() > 0  ) {
            String idSt = s.getId()+"";
            if (null != s && !studentInfo.containsKey(idSt)) {
                studentInfo.put(idSt, s);
                // new一个student就new一个user, 用户名为学号，初始密码均设为"000000"，学生可后续修改
                users.put(idSt, new User(idSt, "000000"));
                return 1;
            } else {
                // 年龄合理但是学号重复
//                System.out.println("该学号的学生已存在！");
                return 2;
            }
        } // 年龄或学号不合理
//        System.out.println("年龄或学号不合理！");
        return 3;
    }

    /**
     * 实现删除学生功能
     * @param id
     * @return
     */
    public boolean serverDeleteStudent(String id) {
        // 如果系统中有该学号的学生，删除该学号的学生和用户
        if (studentInfo.containsKey(id)) {
            studentInfo.remove(id);
            users.remove(id);
            System.out.println("学员删除成功！");
            return true;
        }
        // 如果系统中没有该学号的学生直接返回false
        System.out.println("无法删除，系统中没有该学号的学生！");
        return false;
    }

    /**
     * 实现查找学生功能
     */
    public boolean serverFindStudent(String id) {
        if (studentInfo.containsKey(id)) {
            // 如果能查询到，打印输出具体信息
            System.out.println("查询到的具体信息为：" + studentInfo.get(id));
            return true;
        }
        System.out.println("查询不到该学号的学生！");
        return false;
    }

    /**
     * 返回给定id的学生对象
     * @param id
     * @return
     */
    public Student serverGetStudent(String id) {
        return studentInfo.get(id);
    }

    /**
     * 根据学号、修改部分、新信息修改学生信息
     * @param id
     * @param choice
     * @param newInfo
     * @return
     */
    public int serverModifyStudent(String id, int choice, String newInfo) {
        Student s2 = studentInfo.get(id);
        // 根据用户的指示进行相应修改
        switch(choice){
            case 1:
                System.out.println("学号修改为:" + newInfo);
                // 新学号可行，修改后需要更新学生库
                int newId = Integer.parseInt(newInfo);
                if (! studentInfo.containsKey(newInfo)) {
                    try {
                        // 修改学号后需要更新学生库
                        s2.setId(newId);
                        studentInfo.put(newInfo, s2);
                        // 删除旧学生
                        studentInfo.remove(id);
                    } catch (InvalidStudentIdException e) {
                        e.printStackTrace();
                        return 2;
                    }
                    // 修改学号后需要更新登录用户库
                    users.put(newInfo, new User(newInfo, users.get(id).getPassword()));
                    // 删除旧登录用户
                    users.remove(id);
                    return 1;
                } else {
                    // 如果新设置的学号重复，提醒
//                    System.out.println("该学号已存在，不能使用！");
                    return 3;
                }
                case 2:
                    System.out.println("姓名修改为：");
                    s2.setName(newInfo);
                    return 1;
                case 3:
                    System.out.println("年龄修改为：");
                    try {
                        s2.setAge(Integer.parseInt(newInfo));
                    } catch (InvalidAgeException e) {
                        e.printStackTrace();
                        return 4;
                    }
                    return 1;
            default:
//                System.out.println("请输入正确的指令号码！");
                return 0;
        }
    }

    /**
     * 检验当前密码是否输入正确
     * @param id
     * @param oldPass
     * @return
     */
    public boolean serverCheckPassword(String id, String oldPass) {
        if (users.get(id).getPassword().equals(oldPass)) {
            return true;
        }
        return false;
    }

    /**
     * 实现修改密码功能
     * @param id
     * @param newPass
     * @return
     */
    public boolean serverChangePassword(String id, String newPass) {
//        System.out.println(newPass.length());
        // 新密码长度不少于6则修改
        if (newPass.length() >= 6) {
            users.get(id).setPassword(newPass);
            return true;
        }
        return false;
    }

    /**
     * 返回给定科目的试卷
     * @param examName
     * @return
     */
    public ArrayList<ExamQuestion> serverGetExam(String examName){
        return exams.get(examName);
    }

    /**
     * 实现考试评分功能
     * @param stuID
     * @param examName
     * @param stuAnswers
     * @return
     */
    public boolean serverCheckExam(String stuID, String examName, ArrayList<String> stuAnswers){
        double examScore = ExamScore.getFinalScore(stuAnswers, serverGetExam(examName));
        // 将考试成绩加入该学员的成绩单
        return studentInfo.get(stuID).addScore(examName, examScore);
    }

    /**
     * 实现下载成绩单功能
     * @param id
     * @return
     * @throws FileNotFoundException
     */
    public boolean serverDownloadScores(String id) throws FileNotFoundException {
        if (studentInfo.get(id).getScores().isEmpty()) {
            return false;
        }
        // 成绩单不为空则下载到本地
        ps = new PrintStream(new FileOutputStream(new File(examScorePath)));
        ps.println(id + "的成绩单：");
        HashMap<String, Double> scores = studentInfo.get(id).getScores();
        for (String k : scores.keySet()) {
            ps.println(k + ": " + scores.get(k));
        }
        return true;
    }

    /**
     * 实现查看成绩但功能
     * @param id
     * @return
     */
    public HashMap<String, Double> serverViewScores(String id){
        return studentInfo.get(id).getScores();
    }

    /**
     * 实现增加考题功能
     * @param examName
     * @param examQuestion
     * @return
     */
    public boolean serverAddQuestion(String examName, ExamQuestion examQuestion) {
        // 如果是新试卷先创建试卷
        if (!exams.containsKey(examName)) {
            exams.put(examName, new ArrayList<ExamQuestion>());
            System.out.println("已创建新试卷，等待增加考题...");
        }
        // 如果题目重复不得增加
        if (exams.get(examName).contains(examQuestion)) {        // 重写
            System.out.println("试卷中已存在此考题，增加失败！");
                return false;
        }
        // 将考题放入试卷
        exams.get(examName).add(examQuestion);
        System.out.println("考题增加成功！");
        return true;
    }

    /**
     * 实现删除考题功能
     * @param examName
     * @param choice
     */
    public void serverDeleteQuestion(String examName, int choice) {
        exams.get(examName).remove(choice - 1);
    }

    /**
     * 实现查找考题功能
     * @param examName
     * @return
     */
    public boolean serverFindExam(String examName) {
        if (exams.containsKey(examName)) {
            System.out.println("存在该试卷！");
            return true;
        }
        System.out.println("系统中没有该试卷！");
        return false;
    }

    /**
     * 实现修改试卷（考题）功能
     * @param examName
     * @param revisedExam
     */
    public void serverReplaceExam(String examName, ArrayList<ExamQuestion> revisedExam) {
        // put已存在的key，覆盖value
        exams.put(examName, revisedExam);
    }
//    public void serverModifyQuestion() {
//    }

    /**
     * 实现上传考题功能
     * 本地文档格式要求：第一行是试卷名；其余行用tab隔开题干、选项A、B、C、D和答案
     * @return
     * @throws IOException
     */
    public ArrayList<ExamQuestion> serverUploadQuestions() throws IOException {
        br = new BufferedReader(new FileReader(examContentPath));
        System.out.println("正在从本地上传考题, 文档格式为\"第一行试卷名，其余依次为题干、选项A、B、C、D、答案\"：");
        String examName = br.readLine();
        ArrayList<ExamQuestion> questions;
        // 如果存在试卷新增考题，不存在则创建新试卷以及考题
        questions = exams.containsKey(examName) ? exams.get(examName) : new ArrayList<ExamQuestion>();
        while (null != br.readLine()) {
            // 用数组存放考题各部分
            String[] questionComponents = br.readLine().split("\\t");
            String question = questionComponents[0];
//            System.out.println(question);
            String choiceA = questionComponents[1];
//            System.out.println(choiceA);
            String choiceB = questionComponents[2];
//            System.out.println(choiceB);
            String choiceC = questionComponents[3];
//            System.out.println(choiceC);
            String choiceD = questionComponents[4];
//            System.out.println(choiceD);
            String answer = questionComponents[5];
//            System.out.println(answer);
            // 避免重复考题
            ExamQuestion questionRead = new ExamQuestion(question, choiceA, choiceB, choiceC, choiceD, answer);
            if (!questions.contains(questionRead)) {
                questions.add(questionRead);
//                System.out.println(questionRead);
            }
        }
        // 放入试卷库，已存在会更新value，即考题集合
        exams.put(examName, questions);
        return questions;
    }

}
