package io.itman.library.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 拼接图片地址
 *
 * @author: Ycq
 * @Date: 2018/9/8
 * @Time: 13:41
 */
public class PicUrl {

    /**
     *  拼接单个图片地址
     * @param request
     * @param pic  数据库中的图片字段
     * @return
     */
    public static String onePicUrl(HttpServletRequest request, String pic) {
        if (pic == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme());
        builder.append("://");
        builder.append(request.getServerName());
        builder.append(":");
        builder.append(request.getServerPort());
        builder.append("/images/");
        builder.append(pic);
        return builder.toString();
    }

    /**
     * 拼接单个图片地址,用逗号分隔
     * @param request
     * @param pic     数据库中的图片字段,用逗号分隔
     * @return
     */
    public static String manyPicUrl(HttpServletRequest request, String pic) {
        if (pic == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String[] picArray;
        if (pic != null) {
            picArray = pic.split(",");
            for (int i = 0; i < picArray.length; i++) {
                builder.append(onePicUrl(request, picArray[i]));
                if (i != picArray.length - 1) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

}
