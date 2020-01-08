package io.itman.library.util;
import java.io.*;

/**
 * mysql 备份
 * @author ces
 */
public class MySQLDatabaseBackup {
    /**
     * Java代码实现MySQL数据库导出
     *
     * @author GaoHuanjie
     * @param hostIP MySQL数据库所在服务器地址IP
     * @param userName 进入数据库所需要的用户名
     * @param password 进入数据库所需要的密码
     * @param savePath 数据库导出文件保存路径
     * @param fileName 数据库导出文件文件名
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    public static boolean exportDatabaseTool(String hostIP, String userName, String password, String savePath, String fileName, String databaseName) throws InterruptedException {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath + File.separator;
        }

        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));
            Process process = Runtime.getRuntime().exec(" mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + databaseName);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine())!= null){
                printWriter.println(line);
            }
            printWriter.flush();
            if(process.waitFor() == 0){//0 表示线程正常终止。
                return true;
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Runtime.getRuntime().exec() 还有一些局限性, 就是无法像cmd那样执行较为复杂的命令. 比如, 输出流的重定向如：
     * Runtime.getRuntime.exec("XX.exe YY.doc > ZZ.txt");
     * 他会立即返回, 不会去执行. 但是我们可以这样做, 能够完成于cmd中一样的工作:
     * Runtime.getRuntime.exec("cmd /c XX.exe YY.doc > ZZ.txt");
     * 其中 /c 就是"执行后面字符串的命令". 这样就OK了,但同时还是要注意"错误输出流"的问题,依然要单开一个线程读取.否则一样会阻塞的
     * @param hostIP
     * @param userName
     * @param password
     * @param dataBase
     * @param savePath
     * @param bakFileName
     * @return
     */
    public static Boolean restore(String hostIP,String userName, String password,String dataBase,String savePath,String bakFileName){
        System.out.println( System.getProperty("os.name") );
        System.out.println( System.getProperty("os.version") );
        System.out.println( System.getProperty("os.arch") );
        try {
            System.out.println("mysql -h"+hostIP+" -u"+userName+" -p"+password+" "+dataBase +" < "+savePath+bakFileName);
            Process process =Runtime.getRuntime().exec("cmd /c mysql -h"+hostIP+" -u"+userName+" -p"+password+" "+dataBase +" < "+savePath+bakFileName);
            //Process process =Runtime.getRuntime().exec("ipconfig");
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");

            if(process.waitFor() == 0){//0 表示线程正常终止。
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args){
        try {
            if (exportDatabaseTool("47.98.243.19:3306", "root", "xbkj123", "E:/backupDatabase", "2018-06-06.dump", "xinnongren")) {
                System.out.println("数据库成功备份！！！");
            } else {
                System.out.println("数据库备份失败！！！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        try {
//            if(restore("127.0.0.1", "root", "541015", "rm","D:\\backupDatabase\\","2018-06-06.dump")){
//                System.out.println("数据库还原成功！！！");
//            }else {
//                System.out.println("数据库还原失败！！！");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }
}
