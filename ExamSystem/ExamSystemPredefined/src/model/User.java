package model;

/**
 * @author: Yiqing Wang
 */

public class User implements java.io.Serializable {
    private static final long serialVersionUID = -1225113970581096678L;

    private String userName; // 用于描述用户名信息
    private String password; // 用于描述密码信息

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    // 用户名不能相同，重写equals和hashCode方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!userName.equals(user.userName)) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
