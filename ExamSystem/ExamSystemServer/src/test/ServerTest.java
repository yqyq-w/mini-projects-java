package test;

import server.ServerDao;
import server.ServerInitClose;
import server.ServerView;

import java.io.IOException;

public class ServerTest {

    public static void main(String[] args) {
        ServerInitClose sic = null;

        try {
            // 1.声明ServerInitClose类型的引用指向该类型的对象
            sic = new ServerInitClose();
            // 2.调用成员方法实现服务器的初始化操作
            sic.serverInit();
            // 4.声明ServerDao类型的引用指向该类型的对象
            ServerDao sd = new ServerDao();
            // 3.声明ServerView类型的引用指向该类型的对象
            ServerView sv = new ServerView(sic, sd);
            sv.serverReceive();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 3.调用成员方法实现服务器的关闭操作
            try {
                sic.serverClose();
                // 关闭IO流
                ServerDao.closeStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
