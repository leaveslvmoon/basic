package io.itman.library.util;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.library.exception.MyException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传工具类
 */
@Getter
@Setter
@ConfigurationProperties
@Component
public class UploadUtil {

    /**
     * 上传请求资源
     */
    private MultipartFile multipartFile = null;

    /**
     * 按照当日创建文件夹
     */
    @Value("${rm.isDayType}")
    private boolean isDayType;
    /**
     * 自定义文件路径
     */
    @Value("${rm.uploadPath}")
    private String uploadPath;

    @Value("${rm.imagePath}")
    private String imagePath;

    public static final String IMAGE_SUFFIX = "bmp,jpg,png,gif,jpeg,JPG,JPEG,BMP,GIF";

    private File currentFile;

    public UploadUtil(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public UploadUtil() {
    }

    public String upload() {
        if (isNull()) {
            throw new MyException("上传数据/地址获取异常");
        }

        String fileName = fileNameStyle();
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), currentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 格式化文件名 默认采用UUID
     *
     * @return
     */
    public String fileNameStyle() {
        String curr = multipartFile.getOriginalFilename();
        int suffixLen = curr.lastIndexOf(".");
        if (suffixLen == -1) {
            throw new MyException("文件获取异常");
        }
        String suffix = curr.substring(suffixLen, curr.length());
        int index = Arrays.binarySearch(IMAGE_SUFFIX.split(","),
                suffix.replace(".", ""));
        Boolean b= Arrays.asList(IMAGE_SUFFIX.split(",")).contains(suffix.replace(".", ""));

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String f= df.format(new Date());//根据时间生成文件夹
        curr = UUID.randomUUID() + suffix;
        String filename=curr;

        //image 情况
        curr = StringUtils.isEmpty(imagePath) || (!b) ?
                uploadPath +f+"/"+ File.separator + curr : imagePath +f+"/"+File.separator  + curr;

        currentFile = new File(curr);

        return f+"/"+filename;
    }

    /**
     * 检查上传的文件是否符合系统设置的要求，包括类型，大小
     * @return
     */
    public String checkFileOption(){
        String curr = multipartFile.getOriginalFilename();
        int suffixLen = curr.lastIndexOf(".");
        if (suffixLen == -1) {
            throw new MyException("文件获取异常");
        }
        String suffix = curr.substring(suffixLen, curr.length());//文件后缀
        Record r= Db.findFirst("select fileType,fileSize from sys_basic_info");
        String setSize=r.getStr("fileSize");//系统设置的大小,单位MB
        String setType=r.getStr("fileType");//系统设置的文件类型

        Long originalSize= multipartFile.getSize();
        if(originalSize>Long.valueOf(setSize)*1000*1000){
            return "文件超出"+setSize+"MB，请重新上传";
        }
        List<String> setTypeList= Arrays.asList(setType.split(","));
        if(!setTypeList.contains(suffix.replace(".", ""))){
            return "文件类型不在"+setType+"范围内，请重新上传";
        }
        return "ok";
    }

    /**
     * 检查上传的图片是否符合系统设置的要求，包括类型，大小
     * @return
     */
    public String checkImgOption(){
        String curr = multipartFile.getOriginalFilename();
        int suffixLen = curr.lastIndexOf(".");
        if (suffixLen == -1) {
            throw new MyException("文件获取异常");
        }
        String suffix = curr.substring(suffixLen, curr.length());//图片后缀

        Record r= Db.findFirst("select picExt,picSize from sys_basicinfo");
        BigDecimal setSize=r.getBigDecimal("picSize");//系统设置的大小,单位MB
        String setType=r.getStr("picExt");//系统设置的文件类型
        Long originalSize= multipartFile.getSize();
        if(originalSize>setSize.longValue()*1000*1000){
            return "图片超出"+setSize+"MB，请重 新上传";
        }

        List<String> setTypeList= Arrays.asList(setType.split(","));
        if(!setTypeList.contains(suffix.replace(".", ""))){
            return "图片类型不在"+setType+"范围内，请重新上传";
        }

        return "ok";
    }

    private boolean isNull() {
        if (null != multipartFile) {
            return false;
        }
        return true;
    }


}
