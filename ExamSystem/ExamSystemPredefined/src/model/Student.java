package model;
/*
4. 使用 List 集合实现简易的学生信息管理系统， 
   其中学生的信息有：学号、姓名、年龄。 

5. 新增了学生成绩信息的相关成员变量和方法

@author: Yiqing Wang
 */

import java.util.HashMap;
import java.util.Objects;

// 实现java.io.Serializable接口以启用其序列化功能
public class Student implements java.io.Serializable {
    // 序列化的版本号
    private static final long serialVersionUID = 7338751699312426993L;
    // 私有化成员变量
    private int id;
    private int age;
    private String name;
    // 新增成绩信息
    private HashMap<String, Double> scores = new HashMap<>();

    public Student() {
    }

    // 有参构造中调用set方法设置成员变量值
    public Student(int id, String name, int age){
        // 同时有两个异常都能打印出来
        try {
            setId(id);
        } catch (InvalidStudentIdException e) {
            e.printStackTrace();
        }
        try {
            setAge(age);
        } catch (InvalidAgeException e) {
            e.printStackTrace();
        }
        setName(name);
    }

    // 基于学号的有参构造
    public Student(int id) {
        try {
            setId(id);
            // 补获异常
        } catch (InvalidStudentIdException e) {
            e.printStackTrace();
        }
    }

    // 增加一门课的成绩信息
    public boolean addScore(String examName, double finalScore) {
        if (scores.containsKey(examName)) {
            System.out.println("已参加过该考试，不得改分！");
            return false;
        }
        scores.put(examName, finalScore);
        return true;
    }


    // getters & setters
    public int getId() {
        return id;
    }


    // 设置学号时，若学号不合理，抛出学号异常
    public void setId(int id) throws InvalidStudentIdException {
        if (id > 0) {
            this.id = id;
        } else {
            // System.out.println("学号不合理！");
            throw new InvalidStudentIdException("学号不合理！");
        }
    }
    public int getAge() {
        return age;
    }

    // 设置年龄时，若年龄不合理，抛出年龄异常
    public void setAge(int age) throws InvalidAgeException {
        if (age >= 4 && age <= 60) {
            this.age = age;
        } else {
            // System.out.println("年龄不合理！");
            throw new InvalidAgeException("年龄不合理！");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Double> getScores() {
        return scores;
    }

    public void setScores(HashMap<String, Double> scores) {
        this.scores = scores;
    }


    // 基于学号，重写equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    // 重写hashCode方法
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 重写toString方法便于打印输出
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }



}
