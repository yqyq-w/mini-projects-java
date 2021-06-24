package test;

import client.ClientInitClose;
import client.ClientScanner;
import client.ClientView;

import java.io.IOException;

public class ClientTest {

    public static void main(String[] args) {
        ClientInitClose cic = null;

        try {
            // 1.声明ClientInitClose类型的引用指向该类型的对象
            cic = new ClientInitClose();
            // 2.调用成员方法实现客户端的初始化操作
            cic.clientInit();
            // 3.声明ClientView类型的引用指向该类型的对象
            ClientView cv = new ClientView(cic);
            cv.clientMainPage();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 3.调用成员方法实现客户端的关闭操作
            try {
                cic.clientClose();
                ClientScanner.closeScanner();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
