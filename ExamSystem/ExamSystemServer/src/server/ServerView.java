package server;

/**
 * @author: Yiqing Wang
 */


import model.*;
//import model.TypeCaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 编程实现服务器的主功能
 */
public class ServerView {

    /**
     * 使用合成复用原则
     */
    private ServerInitClose sic;
    private ServerDao sd;

    /**
     * 通过构造方法实现成员变量的初始化
     * @param sic
     */
    public ServerView(ServerInitClose sic, ServerDao sd) {
        this.sic = sic;
        this.sd = sd;
    }

    /**
     * 自定义成员方法实现客户端发来消息的接收并处理
     */
    public void serverReceive() throws IOException, ClassNotFoundException {
        // 启动系统后读取上次保存的各项数据
        loadData();
        // 服务器根据客户端消息类型进行相应处理
        while (true) {
            UserMessage um = TypeCaster.castType(sic.getOis().readObject());
            System.out.println("接收到的消息是：" + um);
            switch (um.getType()){
                case "managerCheck":
                    checkManager(um);
                    break;
                case "userCheck":
                    checkStudent(um);
                    break;
                case "log_out":
                    saveData();
                    System.out.println("客户端已退出系统");
                    break;
                case "change_password":
                    changePassword(um);
                    break;
                case "start_exam":
                    // 通用方法 T t = TypeCaster.castType (Object ob);
                    String examName = TypeCaster.castType(sic.getOis().readObject());
                    startExam(um.getUser().getUserName(), examName);
                    break;
                case "download_scores":
                    downloadExamSores(um.getUser().getUserName());
                    break;
                case "view_scores":
                    viewExamSores(um.getUser().getUserName());
                    break;
                case "add_student":
                    Student s = TypeCaster.castType(sic.getOis().readObject());
                    addStudent(s);
                    break;
                case "modify_student":
                    String id = TypeCaster.castType(sic.getOis().readObject());
                    // 修改部分需要先判断该学号学员是否存在
                    if (sd.serverFindStudent(id)){
                        sic.getOos().writeObject(new UserMessage("success"));
                        // 存在的话进行相应部分的修改
                        int choice = TypeCaster.castType(sic.getOis().readObject());
                        String newInfo = TypeCaster.castType(sic.getOis().readObject());
                        modifyStudent(id, choice, newInfo);
                    } else {
                        sic.getOos().writeObject(new UserMessage("fail"));
                    }
                    break;
                case "delete_student":
                    id = TypeCaster.castType(sic.getOis().readObject());
                    deleteStudent(id);
                    break;
                case "get_student":
                    // 也调用findStudent方法，仅输出语句不同
                    findStudent(um.getUser().getUserName());
                    System.out.println("已将学员的个人信息发送到客户端！");
                case "find_student":
//                    id = um.getUser().getUserName();
                    findStudent(um.getUser().getUserName());
                    break;
                case "add_question":
                    // 根据试卷名增加题目
                    examName = TypeCaster.castType(sic.getOis().readObject());
                    ExamQuestion examQuestion = TypeCaster.castType(sic.getOis().readObject());
                    addQuestion(um, examName, examQuestion);
                    break;
                case "modify_question":
                    examName = TypeCaster.castType(sic.getOis().readObject());
                    modifyQuestion(um, examName);
                    break;
                case "delete_question":
                    examName = TypeCaster.castType(sic.getOis().readObject());
                    deleteQuestion(um, examName);
                    break;
                case "find_questions":
                    examName = TypeCaster.castType(sic.getOis().readObject());
                    findQuestions(um, examName);
                    break;
                case "upload_questions":
                    uploadQuestions();    // 真正使用改成通过获取文件路径作为参数上传考题
                    break;
                case "exit_system":
                    // 退出系统，保存所有数据
                    saveData();
                    // 在ServerTest里关闭
//                    ServerDao.closeStream();
                    System.out.println();
                    return;
                default:
                    System.out.println("无法识别的请求类型！");
            }
        }
    }

    /**
     * 自定义成员方法检验管理员登录请求
     * @param um
     * @throws IOException
     */
    private void checkManager(UserMessage um) throws IOException {
        // 调用方法实现管理员账号和密码信息的校验
        if (sd.serverManagerCheck(um.getUser())) {
            um.setType("success");
        } else {
            um.setType("fail");
        }
        System.out.println("服务端收到客户端的\"管理员登录\"请求，请求数据为"+ um.getUser() +
                "，调用sd.serverManagerCheck方法，响应结果为:" + um.getType());
        // 将校验结果发送给客户端
        sic.getOos().writeObject(um);
        System.out.println("服务器发送校验结果成功！");
    }

    /**
     * 自定义成员方法检验学员登录请求
     * @param um
     * @throws IOException
     */
    private void checkStudent(UserMessage um) throws IOException {
        // 调用方法实现学员账号和密码信息的校验
        if (sd.serverStudentCheck(um.getUser())) {
            um.setType("success");
        } else {
            um.setType("fail");
        }
        System.out.println("服务端收到客户端的\"学员登录\"请求，请求数据为"+ um.getUser() +
                "，调用sd.serverStudentCheck方法，响应结果为:" + um.getType());
        // 将校验结果发送给客户端
        sic.getOos().writeObject(um);
        System.out.println("服务器发送校验结果成功！");
    }

    /**
     * 自定义成员方法实现学员修改密码方法
     * @param um
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void changePassword(UserMessage um) throws IOException, ClassNotFoundException {
        String id = um.getUser().getUserName();
        String oldPass = um.getUser().getPassword();
//        System.out.println(id + ": " + oldPass);
        // 先判断当前密码是否输入正确
        if (sd.serverCheckPassword(id, oldPass)) {
            um.setType("success");
            sic.getOos().writeObject(um);
            System.out.println("已确认当前密码，等待接收新密码...");
            String newPass = TypeCaster.castType(sic.getOis().readObject());
            System.out.println("客户端输入的新密码为：" + newPass);
//            System.out.println(id + "：" + newPass);
            // 再判断新密码长度是否合理
            if (sd.serverChangePassword(id, newPass)) {
                sic.getOos().writeObject(new UserMessage("success"));
                System.out.println("客户端新密码设置成功！");
                sd.serverWriteUsers();
                sd.serverReadUsers();
            } else {
                sic.getOos().writeObject(new UserMessage("fail"));
                System.out.println("新密码少于6位数，设置失败！");
            }
        } else {
            um.setType("fail");
            sic.getOos().writeObject(um);
            System.out.println("当前密码输入错误！");
        }
    }

    /**
     * 自定义成员方法实现学员开始考试方法
     * @param userName
     * @param examName
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void startExam(String userName, String examName) throws IOException, ClassNotFoundException {
        // 调用方法获取试卷
        ArrayList<ExamQuestion> examContent = sd.serverGetExam(examName);
        sic.getOos().writeObject(examContent);
        System.out.println("学员需要完成的考卷发送成功！");
        // 考试结束后，接收学员答案
        System.out.println("接收客户端答案...");
        ArrayList<String> answers = TypeCaster.castType(sic.getOis().readObject());
        System.out.println(answers);
        // 如果是初次提交，登记分数
        if (sd.serverCheckExam(userName, examName, answers)) {
            System.out.println("考试已结束，分数计算完毕！");
            sic.getOos().writeObject(new UserMessage("success"));
        } else {
            sic.getOos().writeObject(new UserMessage("fail"));
            System.out.println("每个科目只记录初次提交后的成绩，此次仅供复习，不记分...");
        }
    }

    /**
     * 自定义成员方法实现学员下载成绩单到本地
     * @param id
     * @throws IOException
     */
    private void downloadExamSores(String id) throws IOException {
        // 若有任意成绩进行下载，调用相关方法
        if (sd.serverDownloadScores(id)) {     // 可改成根据路径地址的参数下载
            sic.getOos().writeObject(new UserMessage("success"));
            System.out.println("客户端成绩单已下载到本地");
        } else {
            sic.getOos().writeObject(new UserMessage("fail"));
            System.out.println("该用户暂无成绩，无需下载！");
        }
    }

    /**
     * 自定义成员方法实现学员在线查看成绩单
     * @param id
     * @throws IOException
     */
    private void viewExamSores(String id) throws IOException {
        // 若有任意成绩，调用方法查看
        HashMap<String, Double> scores = sd.serverViewScores(id);
        if (scores.isEmpty()) {
            sic.getOos().writeObject(new UserMessage("fail"));
            System.out.println("该用户暂无成绩！");
        } else {
            sic.getOos().writeObject(new UserMessage("success"));
            sic.getOos().writeObject(scores);
            System.out.println("已将" + id + "的成绩单发送至客户端！");
        }
    }

    /**
     * 自定义成员方法实现管理员添加学员方法
     * @param s
     * @throws IOException
     */
    private void addStudent(Student s) throws IOException {
//        int addResult = sd.serverAddStudent(s);
        // 调用方法增加学员，根据反馈进行相应操作
        switch (sd.serverAddStudent(s)){
            case 1:
                System.out.println("管理员成功添加学员！");
                sic.getOos().writeObject(new UserMessage("success"));
                saveData();
                loadData();
                break;
            case 2:
                sic.getOos().writeObject(new UserMessage("该学号的学生已存在，添加失败！"));
                System.out.println("该学号的学生已存在，管理员添加学员失败！");
                break;
            case 3:
                sic.getOos().writeObject(new UserMessage("年龄或学号不合理，添加失败！(提示：学号 > 0; 4 <= 年龄 <= 70)"));
                System.out.println("年龄或学号不合理，管理员添加学员失败！");
                break;
        }
    }

    /**
     * 自定义成员方法实现管理员修改学员方法
     * @param id
     * @param choice
     * @param newInfo
     * @throws IOException
     */
    private void modifyStudent(String id, int choice, String newInfo) throws IOException {
        // 调用方法修改学员信息，根据反馈进行相应操作
        switch (sd.serverModifyStudent(id, choice, newInfo)) {
            case 1:
                sic.getOos().writeObject("success");
                // 或者每次有修改实时保存更新到本地，不等到退出登录
                saveData();
                loadData();
                System.out.println("信息修改成功！");
                break;
            case 2:
                sic.getOos().writeObject("学号不合理！");
                System.out.println("学号不合理！");
                break;
            case 3:
                sic.getOos().writeObject("该学号已存在，不能使用！");
                System.out.println("该学号已存在，不能使用！");
                break;
            case 4:
                sic.getOos().writeObject("年龄不合理！");
                System.out.println("年龄不合理！");
                break;
            case 0:
                sic.getOos().writeObject("请输入正确的指令号码！");
                System.out.println("请输入正确的指令号码！");
                break;
        }
    }

    /**
     * 自定义成员方法实现管理员删除学员方法
     * @param id
     * @throws IOException
     */
    private void deleteStudent(String id) throws IOException {
        // 调用方法删除正确学号的学员
        if (sd.serverDeleteStudent( id)) {
            sic.getOos().writeObject(new UserMessage("success"));
            saveData();
            loadData();
        } else {
            sic.getOos().writeObject(new UserMessage("fail"));
        }
    }

    /**
     * 自定义成员方法实现管理员查看学员方法
     * @param id
     * @throws IOException
     */
    private void findStudent(String id) throws IOException {
        // 如果查找到学员，发送其基本信息
        if (sd.serverFindStudent(id)){ // find
            sic.getOos().writeObject(new UserMessage("success"));
            sic.getOos().writeObject(sd.serverGetStudent(id)); // get
        } else {
            sic.getOos().writeObject(new UserMessage("fail"));
        }
    }

    /**
     * 自定义成员方法实现管理员增加考题方法
     * @param um
     * @param examName
     * @param examQuestion
     * @throws IOException
     */
    private void addQuestion(UserMessage um, String examName, ExamQuestion examQuestion) throws IOException {
        // 增加指定试卷中的指定新考题
        if (sd.serverAddQuestion(examName, examQuestion)) {
            um.setType("success");
//            System.out.println(sd.serverGetExam(examName));
            sd.serverWriteExams();
            sd.serverReadExams();
        } else {
            um.setType("fail");
        }
        sic.getOos().writeObject(um);
    }

    /**
     * 自定义成员方法实现管理员修改考题方法
     * @param um
     * @param examName
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void modifyQuestion(UserMessage um, String examName) throws IOException, ClassNotFoundException {
        // 先判断是否存在指定考卷
        if (sd.serverFindExam(examName)) {
            um.setType("success");
            sic.getOos().writeObject(um);
            ArrayList<ExamQuestion> examContent =  sd.serverGetExam(examName);
            sic.getOos().writeObject(examContent);
            // 等待客户端修改题目内容
            UserMessage feedback = TypeCaster.castType(sic.getOis().readObject());
            if ("success".equals(feedback.getType())) {
                System.out.println("等待客户端修改题目内容...");
                // 如果修改后题干不重复，将题目加进该试卷
                ArrayList<ExamQuestion> revisedExamContent = TypeCaster.castType(sic.getOis().readObject());
                sd.serverReplaceExam(examName, revisedExamContent);
                System.out.println("该试卷考题修改成功！");
                // 测试看一下
//                System.out.println(sd.serverGetExam(examName));
                sd.serverWriteExams();
                sd.serverReadExams();
            } else {
                System.out.println("客户端输入的题号不合理，无法修改！");
            }
        } else {
            um.setType("fail");
            sic.getOos().writeObject(um);
            System.out.println("客户端输入的试卷不合理，无法修改！");
        }
    }

    /**
     * 自定义成员方法实现管理员删除考题方法
     * @param um
     * @param examName
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void deleteQuestion(UserMessage um, String examName) throws IOException, ClassNotFoundException {
        // 先判断是否存在指定考卷
        if (sd.serverFindExam(examName)) {
            um.setType("success");
            sic.getOos().writeObject(um);
            ArrayList<ExamQuestion> examContent = sd.serverGetExam(examName);
            sic.getOos().writeObject(examContent);
            UserMessage feedback = TypeCaster.castType(sic.getOis().readObject());
            if ("success".equals(feedback.getType())) {
                // 在题号范围内删除考题
                int choice = TypeCaster.castType(sic.getOis().readObject());
                sd.serverDeleteQuestion(examName, choice);
//                System.out.println(sd.serverGetExam(examName));
                sd.serverWriteExams();
                sd.serverReadExams();
            } else {
                System.out.println("客户端输入的题号不合理，无法删除！");
            }
        } else {
            um.setType("fail");
            sic.getOos().writeObject(um);
            System.out.println("客户端输入的试卷不合理，无法删除！");
        }
    }

    /**
     * 自定义成员方法实现管理员查询考题方法
     * @param um
     * @param examName
     * @throws IOException
     */
    private void findQuestions(UserMessage um, String examName) throws IOException {
        // 如果存在指定试卷，返回其卷面所有题目
        if (sd.serverFindExam(examName)) {
            um.setType("success");
            sic.getOos().writeObject(um);
            sic.getOos().writeObject(sd.serverGetExam(examName));
        } else {
            um.setType("fail");
            sic.getOos().writeObject(um);
        }
    }

    /**
     * 自定义成员方法实现管理员导入考题（选择题）方法
     * @throws IOException
     */
    private void uploadQuestions() throws IOException {
        // 调用方法将测试路径文件的一定格式的文字以考题对象导入
        ArrayList<ExamQuestion> uploadQuestions = sd.serverUploadQuestions();
        sic.getOos().writeObject(uploadQuestions);
        // 测试查看一下
        System.out.println("用户端上传了考题，新试卷如下：\n" + uploadQuestions);
    }

    /**
     * 自定义成员方法实现保存更新数据库（写入本地）
     */
    private void saveData() {
        System.out.println("保存更新数据库...");
        sd.serverWriteUsers();
        sd.serverWriteStudentInfo();
        sd.serverWriteExams();
    }

    /**
     * 自定义成员方法实现从本地读取数据库
     */
    private void loadData() {
        System.out.println("读取数据库...");
        sd.serverReadUsers();
        sd.serverReadStudentInfo();
        sd.serverReadExams();
    }

}
