package io.itman.library.chunzhenip;

/**
 * Created by Administrator on 2018/8/7.
 */
public class Test {
    public static void main(String[] args) {
        //指定纯真数据库的文件名，所在文件夹
        IPSeeker ip=new IPSeeker("qqwry.dat","D:/java/ip");
        //测试IP 58.20.43.13
        System.out.println(ip.getIPLocation("127.0.0.1").getCountry()+":"+ip.getIPLocation("58.20.43.13").getArea());
    }
}
