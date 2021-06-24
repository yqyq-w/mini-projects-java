package model;
/**
 * @author: Yiqing Wang
 */

public class UserMessage implements java.io.Serializable {
    private static final long serialVersionUID = -4729638835655094399L;

    private String type; // 消息的类型代表具体的业务
    private User user;   // 消息的具体内容
//    public final static String[] LOGIN_M = {"log_in", "manager_check", "user_check", "log_out", "exit_system"};
//    public final static String[] FEEDBACK_M = {"success", "fail"};
//    public final static String[] STUDENT_M = {"change_password", "check_password", "start_exam", "download_scores", "view_scores"};
//    public final static String[] ADMIN_STU_M = {"student_management", "add_student", "modify_student",
//                                                        "delete_student", "find_student", "return_to_previous"};
//    public final static String[] ADMIN_EXAM_M = {"exam_management", "add_question", "modify_question",
//                                                    "delete_question", "find_questions", "upload_questions"};

    public UserMessage() {
    }

    //
    public UserMessage(String type) {
        this.type = type;
        this.user = null;
    }

    public UserMessage(String type, User user) {
        this.type = type;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "type='" + type + '\'' +
                ", user=" + user +
                '}';
    }
}
