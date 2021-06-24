package model;

/**
 * @author: Yiqing Wang
 */

public class TypeCaster {

    // ois接收后的强转为实际类型
    public static <T> T castType (Object ob) {
        return (T) ob;
    }

    public static void main(String[] args) {
        // 测试一下 用String接收不会报错
        String what = castType("what");
        // .getClass() -> class java.lang.String
        System.out.println(castType("what").getClass());

    }

}