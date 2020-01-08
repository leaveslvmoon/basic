package io.itman.library.util;

/**
 * Created with IDEA
 *
 * @author: Ycq
 * @Date: 2018/9/5
 * @Time: 19:11
 */
public class MakeTimestamps {
    private String timeStamps;

    public MakeTimestamps(){
        //获取时间戳
        long l = System.currentTimeMillis();
        /*MD5后截取前30位并转大写*/
        String str = MD5.getMD5(String.valueOf(l)).substring(0,30).toUpperCase();
        this.timeStamps = str;
    }

    public String getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(String timeStamps) {
        this.timeStamps = timeStamps;
    }
}
