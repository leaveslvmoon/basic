package io.itman.admin.controller;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
@RequestMapping(value = "/upload")
public class CommonUploadController {

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

    @GetMapping(value = "/toUploadUI")
    public String toFilesPage(Boolean ismulti, Model model,@RequestParam(value="path", required=false, defaultValue="pic") String path) {
        System.out.println("上传多选状态---"+ismulti);
        model.addAttribute("ismulti",ismulti);
        model.addAttribute("path",path);
        return "public/upload";
    }

    @PostMapping(value = "/doUpload")
    public void doUpload(HttpServletRequest request, HttpServletResponse response,
                         String name,
                         int chunk,
                         int chunks,
                         String Path,
                         String NameAdd,
                         @RequestParam("file") MultipartFile[] file) throws IOException {

        response.setCharacterEncoding( "UTF-8" );
        String tempFileName = name; /* 临时文件名 */
        String newFileName = null; /* 最后合并后的新文件名 */
        BufferedOutputStream outputStream    = null;



        /* System.out.println(FileUtils.getTempDirectoryPath()); */
        String uploadPath=imagePath;
        File up = new File( uploadPath );
        if ( !up.exists() )
        {
            up.mkdir();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String foder= df.format(new Date());//根据时间生成文件夹
        uploadPath = uploadPath+foder;
        File dateFile = new File(uploadPath);
        if(!dateFile.exists()){
            dateFile.mkdir();
        }
        if ( ServletFileUpload.isMultipartContent( request ) )
        {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold( 1024 );
                /* factory.setRepository(new File(repositoryPath));// 设置临时目录 */
                ServletFileUpload upload = new ServletFileUpload( factory );
                upload.setHeaderEncoding( "UTF-8" );
                /* upload.setSizeMax(5 * 1024 * 1024);// 设置附件最大大小，超过这个大小上传会不成功 */


                newFileName = UUID.randomUUID().toString().replace( "-", "" )
                        .concat( "." )
                        .concat( FilenameUtils.getExtension( tempFileName ) );
                if (chunk + 1 == chunks )
                {
                    outputStream = new BufferedOutputStream(
                            new FileOutputStream( new File( uploadPath, newFileName ) ) );
                    /* 遍历文件合并 */
                    for ( int i = 0; i < chunks; i++ )
                    {
                        byte[] bytes = file[i].getBytes();
                        outputStream.write( bytes );
                        outputStream.flush();

                    }
                    outputStream.flush();
                }
                Map<String, Object> m = new HashMap<String, Object>();
                m.put( "status", true );
                m.put( "fileUrl",   foder+"/"+newFileName );
                response.getWriter().write( JSON.toJSONString( m ) );

            } catch ( Exception e ) {
                e.printStackTrace();
                Map<String, Object> m = new HashMap<String, Object>();
                m.put( "status", false );
                response.getWriter().write( JSON.toJSONString( m ) );
            } finally {
                try {
                    if ( outputStream != null ) {
                        outputStream.close();
                    }
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }

    }


    @PostMapping(value = "/doUpload2")
    public void doUpload2(HttpServletRequest request, HttpServletResponse response,
                         String name,
                         int chunk,
                         int chunks,
                         String Path,
                         String NameAdd,
                         @RequestParam("file") MultipartFile[] file) throws IOException {

        response.setCharacterEncoding( "UTF-8" );
        String tempFileName = name; /* 临时文件名 */
        String newFileName = null; /* 最后合并后的新文件名 */
        BufferedOutputStream outputStream    = null;

        /* System.out.println(FileUtils.getTempDirectoryPath()); */
        String uploadPath=imagePath;

        File up = new File( uploadPath );
        if ( !up.exists() )
        {
            up.mkdir();
        }

        if ( ServletFileUpload.isMultipartContent( request ) )
        {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold( 1024 );
                /* factory.setRepository(new File(repositoryPath));// 设置临时目录 */
                ServletFileUpload upload = new ServletFileUpload( factory );
                upload.setHeaderEncoding( "UTF-8" );
                /* upload.setSizeMax(5 * 1024 * 1024);// 设置附件最大大小，超过这个大小上传会不成功 */


                newFileName = UUID.randomUUID().toString().replace( "-", "" )
                        .concat( "." )
                        .concat( FilenameUtils.getExtension( tempFileName ) );
                List<String> fileUrlList=new ArrayList<>();
                for (MultipartFile f:file) {
                    String newName=UUID.randomUUID().toString().replace( "-", "" )
                            .concat( "." )
                            .concat( FilenameUtils.getExtension( tempFileName ) );
                    outputStream = new BufferedOutputStream(
                            new FileOutputStream( new File( uploadPath, newName ) ) );
                    fileUrlList.add(newName);
                    byte[] bytes = f.getBytes();
                    outputStream.write( bytes );
                    outputStream.flush();
                }
                Map<String, Object> m = new HashMap<String, Object>();
                m.put( "status", true );
                m.put( "fileUrl",   fileUrlList );
                response.getWriter().write( JSON.toJSONString( m ) );

            } catch ( Exception e ) {
                e.printStackTrace();
                Map<String, Object> m = new HashMap<String, Object>();
                m.put( "status", false );
                response.getWriter().write( JSON.toJSONString( m ) );
            } finally {
                try {
                    if ( outputStream != null ) {
                        outputStream.close();
                    }
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }

    }

}
