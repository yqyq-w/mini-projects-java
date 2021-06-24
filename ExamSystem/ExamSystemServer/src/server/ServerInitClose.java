package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 编程实现服务器的初始化和关闭
 */
public class ServerInitClose {
    /**
     * 自定义成员变量来记录Socket和流对象
     */
    private ServerSocket ss;
    private Socket s;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    /**
     * 自定义成员方法实现服务器的初始化操作
     */
    public void serverInit() throws IOException {
        // 1.创建ServerSocket类型的对象并提供端口号
        ss = new ServerSocket(8888);
        // 2.等待客户端的连接请求，调用accept方法
        System.out.println("等待客户端的连接请求...");
        s = ss.accept();
        System.out.println("客户端连接成功！");
        // 3.使用输入输出流进行通信
        ois = new ObjectInputStream(s.getInputStream());
        oos = new ObjectOutputStream(s.getOutputStream());
        System.out.println("服务器初始化成功！");

    }

    /**
     * 自定义成员方法实现服务器的关闭操作
     */
    public void serverClose() throws IOException {
        // 4.关闭Socket并释放有关的资源
        oos.close();
        ois.close();
        s.close();
        ss.close();
        System.out.println("服务器成功关闭！");
    }
}
