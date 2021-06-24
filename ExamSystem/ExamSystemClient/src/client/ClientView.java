package client;

/**
 * @author: Yiqing Wang
 */

import model.*;
//import model.TypeCaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 编程实现客户端的主界面绘制和相应功能的实现
 */
public class ClientView {
    /**
     * 为了可以使用输入输出流，采用合成复用原则实现
     */
    private ClientInitClose cic;

    private Scanner scnr = ClientScanner.getScanner();

    /**
     * 通过构造方法实现成员变量的初始化
     * @param cic
     */
    public ClientView(ClientInitClose cic) {
        this.cic = cic;
    }

    /**
     * 自定义成员方法实现客户端主界面的绘制
     */
    public void clientMainPage() throws IOException, ClassNotFoundException {
        while(true) {
            System.out.println("  \n\n\t\t   在线考试系统");
            System.out.println("-------------------------------------------");
            System.out.print("   [1] 学员系统");
            System.out.println("     [2] 管理员系统");
            System.out.println("   [0] 退出系统");
            System.out.println("-------------------------------------------");
            System.out.println("请选择要进行的业务编号：");
            //Scanner sc = new Scanner(System.in);
            //int choice = sc.nextInt();
            int choice = scnr.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("正在进入学员系统...");
                    // 调用clientStudentLogin进入学员系统
                    clientStudentLogin();
                    break;
                case 2:
                    System.out.println("正在进入管理员系统...");
                    // 调用clientManagerLogin进入管理员系统
                    clientManagerLogin();
                    break;
                case 0:
                    System.out.println("正在退出系统...");
                    // // 调用clientExit退出系统
                    clientExit();
                    return;
                default:
                    System.out.println("输入错误，请重新选择！");
            }
        }
    }

    /**
     * 自定义成员方法实现客户端管理员登录的功能
     */
    private void clientManagerLogin() throws IOException, ClassNotFoundException {
        // 1.准备管理员登录的相关数据
        System.out.println("请输入管理员的账户信息：");
        String userName = scnr.next();
        System.out.println("请输入管理员的密码：");
        String password = scnr.next();
        UserMessage tum = new UserMessage("managerCheck", new User(userName, password));
        // 2.将UserMessage类型的对象通过对象输出流发送给服务器
        cic.getOos().writeObject(tum);
        System.out.println("客户端发送管理员账户信息成功！");
        // 3.接收服务器的处理结果并给出提示
        // T t = TypeCaster.castType(Object o);
        tum = TypeCaster.castType(cic.getOis().readObject());
        if ("success".equals(tum.getType())) {
            System.out.println("登录成功，欢迎使用！");
            clientManagerView();
        } else {
            System.out.println("用户名或密码错误！");
        }
    }

    /**
     * 自定义成员方法实现客户端学员登录的功能
     */
    private void clientStudentLogin() throws IOException, ClassNotFoundException {
        // 1.准备管理员登录的相关数据
        System.out.println("请输入学员的账户信息：（学号）");
        String userName = scnr.next();
        System.out.println("请输入学员的密码：（初始密码为六个0）");
        String password = scnr.next();
        UserMessage tum = new UserMessage("userCheck", new User(userName, password));
        // 2.将UserMessage类型的对象通过对象输出流发送给服务器
        cic.getOos().writeObject(tum);
        System.out.println("客户端发送学员账户信息成功！");
        // 3.接收服务器的处理结果并给出提示
        tum = TypeCaster.castType(cic.getOis().readObject());
        if ("success".equals(tum.getType())) {
            System.out.println("登录成功，欢迎使用！");
            //
            clientStudentView(tum.getUser().getUserName());
        } else {
            System.out.println("用户名或密码错误！");
        }
    }

    /**
     * 自定义成员方法实现退出系统
     */
    private void clientExit() throws IOException {
        // 发送相应消息类型
        cic.getOos().writeObject(new UserMessage("exit_system"));
    }

    /**
     * 自定义成员方法进入学员系统
     * @param id
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void clientStudentView(String id) throws IOException, ClassNotFoundException {
        while (true) {
            // 根据用户输入的选择去实现查看个人信息、修改密码、开始考试、导出成绩、查询成绩、退出登录的功能
            System.out.println("  \n\n\t\t   学员系统");
            System.out.println("-------------------------------------------");
            System.out.print("   [1] 我的资料");
            System.out.println("     [2] 修改密码");
            System.out.print("   [3] 开始考试");
            System.out.println("     [4] 导出成绩");
            System.out.print("   [5] 查询成绩");
            System.out.println("     [0] 退出登录");
            System.out.println("-------------------------------------------");
            System.out.println("请选择要进行的学生业务编号：");
            int instruction = scnr.nextInt();
            switch (instruction) {
                case 1:
                    // 发送相应消息类型查看个人资料
                    cic.getOos().writeObject(new UserMessage("get_student", new User(id,"")));
                    UserMessage um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("当前个人信息为：");
                        Student studentFound = TypeCaster.castType(cic.getOis().readObject());
                        System.out.println(studentFound);
                    } else {
                        System.out.println("查看失败！");
                    }
                    break;
                case 2:
                    // 发送相应消息类型修改密码
                    System.out.println("请输入当前密码：");
                    cic.getOos().writeObject(new UserMessage("change_password", new User(id, scnr.next())));
                    um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("请设置新的密码：（长度至少为6）");
                        cic.getOos().writeObject(scnr.next());
                        um = TypeCaster.castType(cic.getOis().readObject());
                        System.out.println("success".equals(um.getType()) ? "密码已成功修改！" : "密码必须不得少于6位数！");
                    } else {
                        System.out.println("当前密码输入错误，无法修改密码！");
                    }
                    break;
                case 3:
                    // 发送相应消息类型开始考试
                    cic.getOos().writeObject(new UserMessage("start_exam", new User(id, "")));
                    // 测试试卷中Java01通过上传考题、Java02通过增加考题
                    System.out.println("请选择考试科目：（Java01、Java02）");
                    cic.getOos().writeObject(scnr.next());
                    ArrayList<ExamQuestion> examContent = TypeCaster.castType(cic.getOis().readObject());
                    ArrayList<String> myAnswers = new ArrayList<>();
                    System.out.println("考试已开始...");
                    for (ExamQuestion q: examContent) {
                        System.out.println(q.showToStudents()); // showToStudents方法不包含答案
                        System.out.println("请输入你的答案：（单选题）");
                        myAnswers.add(scnr.next().trim());
                    }
//                    System.out.println(myAnswers);
                    cic.getOos().writeObject(myAnswers);
                    System.out.println("考试已结束，正在提交答案...");
                    um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("成绩已可查看！");
                    } else {
                        System.out.println("每个科目只记录初次提交后的成绩，此次仅供复习，不记分...");
                    }
                    break;
                case 4:
                    // 发送相应消息类型下载成绩
                    cic.getOos().writeObject(new UserMessage("download_scores", new User(id, "")));
                    System.out.println("正在下载成绩单...");
                    um = TypeCaster.castType(cic.getOis().readObject());
                    System.out.println("success".equals(um.getType()) ? "成绩单已下载到本地！" : "暂无成绩可下载！");
                    break;
                case 5:
                    // 发送相应消息类型查询成绩
                    cic.getOos().writeObject(new UserMessage("view_scores", new User(id, "")));
                    um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        HashMap<String, Integer> scores = TypeCaster.castType(cic.getOis().readObject());
                        System.out.println("成绩单为：");
                        for (String k : scores.keySet()) {
                            System.out.println(k + ": " + scores.get(k));
                        }
                    } else {
                        System.out.println("暂无成绩可查看");
                    }
                    break;
                case 0:
                    // 发送相应消息类型退出登录，退回在线考试系统主页
                    cic.getOos().writeObject(new UserMessage("log_out"));
                    System.out.println("您已退出登录！");
                    return;
                default:
                    System.out.println("请输入正确的指令号码！");
            }
        }
    }

    /**
     * 自定义成员方法进入管理员系统
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void clientManagerView() throws IOException, ClassNotFoundException {
        while (true) {
            // 选择管理员业务
            System.out.println("  \n\n\t\t   管理员系统");
            System.out.println("-------------------------------------------");
            System.out.print("   [1] 学员管理模块");
            System.out.println("     [2] 考题管理模块");
            System.out.println("   [0] 退出登录");
            System.out.println("-------------------------------------------");
            System.out.println("请选择要进行的管理员业务编号：");
            int instruction = scnr.nextInt();
            switch (instruction) {
                case 1:
                    System.out.println("正在进入学员管理模块...");
                    // 通过clientStudentManagement进入学员管理模块
                    clientStudentManagement();
                    break;
                case 2:
                    System.out.println("正在进入考题管理模块...");
                    // 通过clientExamManagement进入考题管理模块
                    clientExamManagement();
                    break;
                case 0:
                    // 发送相应消息类型退出登录，退回在线考试系统主页
                    cic.getOos().writeObject(new UserMessage("log_out"));
                    System.out.println("管理员已退出登录！");
                    return;
                default:
                    System.out.println("请输入正确的指令号码！");
            }
        }
    }

    /**
     * 自定义成员方法进入学员管理模块
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void clientStudentManagement() throws IOException, ClassNotFoundException {
        while (true) {
            // 根据用户输入的选择去实现增加、删除、修改、查找以及遍历所有学生信息的功能
            System.out.println("  \n\n\t\t   管理员学员管理模块");
            System.out.println("-------------------------------------------");
            System.out.print("   [1] 添加学员");
            System.out.println("     [2] 删除学员");
            System.out.print("   [3] 修改学员");
            System.out.println("     [4] 查找学员");
            System.out.println("   [0] 返回主页");
            System.out.println("-------------------------------------------");
            System.out.println("请选择要进行的业务编号：");
            int instruction = scnr.nextInt();
            switch (instruction) {
                case 1:
                    // 发送相应消息类型添加学员
                    cic.getOos().writeObject(new UserMessage("add_student"));
                    System.out.println("请输入要添加学生的信息, 格式为\"学号 姓名 年龄\"：");
                    cic.getOos().writeObject(new Student(scnr.nextInt(), scnr.next(), scnr.nextInt()));
                    UserMessage um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("新学员添加成功！");
                    } else {
                        System.out.println(um.getType());
                    }
                    break;
                case 2:
                    // 发送相应消息类型删除学员
                    cic.getOos().writeObject(new UserMessage("delete_student"));
                    System.out.println("请输入要删除的学员的学号：");
                    cic.getOos().writeObject(scnr.next());
                    um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("学员删除成功！");
                    } else {
                        System.out.println("无法删除，系统中没有该学号的学生！");
                    }
                    break;
                case 3:
                    // 发送相应消息类型修改学员信息
                    cic.getOos().writeObject(new UserMessage("modify_student"));
                    System.out.println("请输入要修改的学员的学号：");
                    cic.getOos().writeObject(scnr.next());
                    um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("请选择要修改什么信息：\n" +
                                "[1] 学号\n[2] 姓名\n[3] 年龄");
                        cic.getOos().writeObject(scnr.nextInt());
                        System.out.println("要修改为：");
                        cic.getOos().writeObject(scnr.next());
                        String feedback = (String) cic.getOis().readObject();
                        if ("success".equals(feedback)) {
                            System.out.println("学员信息修改成功！");
                        } else {
                            System.out.println("修改失败！");
                            System.out.println(feedback);
                        }
                    } else {
                        System.out.println("无法修改，系统中没有该学号的学生！");
                    }
                    break;
                case 4:
                    // 发送相应消息类型查询学员
                    cic.getOos().writeObject(new UserMessage("find_student"));
                    System.out.println("请输入要查找的学员的学号：");
                    cic.getOos().writeObject(scnr.next());
                    um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("找到该学员！信息如下：");
                        Student studentFound = TypeCaster.castType(cic.getOis().readObject());
                        System.out.println(studentFound);
                    } else {
                        System.out.println("未找到该学号的学员！");
                    }
                    break;
                case 0:
                    System.out.println("退出学员管理模块...");
                    return; // return退出该模块的循环，回到管理员系统主页
                default:
                    System.out.println("请输入正确的指令号码！");
            }
        }
    }

    /**
     * 自定义成员方法进入考题管理模块
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void clientExamManagement() throws IOException, ClassNotFoundException {
        while (true) {
            // 根据用户输入的选择去实现增加、删除、修改、查找以及上传考题（选择题）的功能
            System.out.println("  \n\n\t\t   管理员考题管理模块");
            System.out.println("-------------------------------------------");
            System.out.print("   [1] 增加考题");
            System.out.println("     [2] 删除考题");
            System.out.print("   [3] 修改考题");
            System.out.println("     [4] 查找考题");
            System.out.print("   [5] 上传考题");
            System.out.println("     [0] 返回主页");
            System.out.println("-------------------------------------------");
            System.out.println("请选择要进行的业务编号：");
            int instruction = scnr.nextInt();
            switch (instruction) {
                case 1:
                    // 发送相应消息类型增加考题
                    cic.getOos().writeObject(new UserMessage("add_question"));
                    System.out.println("请输入要增加考题的试卷名称：");
                    cic.getOos().writeObject(scnr.next());
                    System.out.println("请输入要增加的考题信息, 格式为\"题干 选项A 选项B 选项C 选项D 答案\"：");
                    cic.getOos().writeObject(new ExamQuestion(scnr.next(), scnr.next(), scnr.next(), scnr.next(), scnr.next(), scnr.next()));
                    UserMessage um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("新考题添加成功！");
                    } else {
                        System.out.println("试卷中已存在此考题！");
                    }
                    break;
                case 2:
                    // 发送相应消息类型删除考题
                    cic.getOos().writeObject(new UserMessage("delete_question"));
                    System.out.println("请输入要删除考题所在的试卷名：");
                    cic.getOos().writeObject(scnr.next());
                    um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        ArrayList<ExamQuestion> examContent = TypeCaster.castType(cic.getOis().readObject());
                        System.out.println(examContent);
                        System.out.println("该试卷共有" + examContent.size() + "题，请输入要删除第几题：");
                        int choice = scnr.nextInt();
                        if (choice >= 1 && choice <= examContent.size()) {  // 用户输入1～examContent.size()，列表index 0～examContent.size()-1
                            cic.getOos().writeObject(new UserMessage("success"));
                            cic.getOos().writeObject(choice);
                            System.out.println("考题删除成功！");
                        } else {
                            cic.getOos().writeObject(new UserMessage("fail"));
                            System.out.println("输入的题号不合理！");
                        }
                    } else {
                        System.out.println("无法删除，系统中没有该试卷！");
                    }
                    break;
                case 3:
                    // 发送相应消息类型修改考题
                    cic.getOos().writeObject(new UserMessage("modify_question"));
                    System.out.println("请输入要修改考题所在的试卷名：");
                    cic.getOos().writeObject(scnr.next());
                    um = (UserMessage)cic.getOis().readObject();
                    if ("success".equals(um.getType())) {
                        ArrayList<ExamQuestion> examContent = TypeCaster.castType(cic.getOis().readObject());
                        System.out.println(examContent);
                        System.out.println("该试卷共有" + examContent.size() + "题，请输入要修改第几题：");
                        int choice = scnr.nextInt();
                        if (choice >= 1 && choice <= examContent.size()) {  // 用户输入1～examContent.size()，列表index 0～examContent.size()-1
                            cic.getOos().writeObject(new UserMessage("success"));
                            ExamQuestion questionToModify = examContent.get(choice - 1); // index = choice - 1
                            System.out.println("请选择要修改什么信息：\n" +
                                    "[1] 题干\n[2] 选项A\n[3] 选项B\n[4] 选项C\n[5] 选项D\n[6] 答案");
                            choice = scnr.nextInt();
                            System.out.println("请输入更新后的内容：");
                            String newINfo = scnr.next().trim();
                            switch (choice) {
                                case 1:
                                    questionToModify.setQuestion(newINfo);
                                    break;
                                case 2:
                                    questionToModify.setChoiceA(newINfo);
                                    break;
                                case 3:
                                    questionToModify.setChoiceB(newINfo);
                                    break;
                                case 4:
                                    questionToModify.setChoiceC(newINfo);
                                    break;
                                case 5:
                                    questionToModify.setChoiceD(newINfo);
                                    break;
                                case 6:
                                    questionToModify.setAnswer(newINfo);
                                    break;
                                default :
                                    System.out.println("请输入正确的指令！");
                            }
//                            // 查看一下一下
//                            System.out.println(questionToModify);
//                            System.out.println(examContent);
                            cic.getOos().writeObject(examContent);
                            System.out.println("已将修改后的考题发送至服务器！");
                        } else {
                            cic.getOos().writeObject(new UserMessage("fail"));
                            System.out.println("输入的题号不合理！");
                        }
                    } else {
                        System.out.println("无法修改，系统中没有该试卷！");
                    }
                    break;
                case 4:
                    // 发送相应消息类型查询考题
                    cic.getOos().writeObject(new UserMessage("find_questions"));
                    System.out.println("请输入要查找考题的试卷名：");
                    cic.getOos().writeObject(scnr.next());
                    um = TypeCaster.castType(cic.getOis().readObject());
                    if ("success".equals(um.getType())) {
                        System.out.println("找到该试卷的考题！题目如下：");
                        ArrayList<ExamQuestion> examContent = TypeCaster.castType(cic.getOis().readObject());
                        System.out.println(examContent);
                    } else {
                        System.out.println("系统中没有该试卷！");
                    }
                    break;
                case 5:
                    // 发送相应消息类型上传考题
                    cic.getOos().writeObject(new UserMessage("upload_questions"));
                    System.out.println("已进入上传考题界面...");
                    ArrayList<ExamQuestion>  uploadQuestions = TypeCaster.castType(cic.getOis().readObject());
                    System.out.println("上传考题成功，试卷如下：\n" + uploadQuestions); //
                    break;
                case 0:
                    System.out.println("退出考题管理模块...");
                    return;
                default:
                    System.out.println("请输入正确的指令号码！");;
            }
        }
    }

}



