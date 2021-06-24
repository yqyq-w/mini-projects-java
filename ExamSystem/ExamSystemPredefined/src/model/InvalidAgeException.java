package model;
/*
1. 编程题 
  基于学生信息管理系统增加以下两个功能： 
            a.自定义学号异常类和年龄异常类，并在该成员变量不合理时产生异常对象并抛出。 
            b.当系统退出时将 List 集合中所有学生信息写入到文件中，当系统启动时读取文件中所有学生信息到 List 集合中。

 @author: Yiqing Wang
 */

// 继承Exception类或者其子类
public class InvalidAgeException extends Exception {
    // 序列化的版本号
    static final long serialVersionUID = 7338751699312422965L;

    // 提供两个版本的构造方法，一个是无参构造方法
    public InvalidAgeException(){
    }

	// 另外一个是字符串作为参数的构造方法
    public InvalidAgeException(String message) {
        super(message);
    }

}
