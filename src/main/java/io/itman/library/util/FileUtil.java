package io.itman.library.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;


public class FileUtil {

    public static final int BUFFER_SIZE = 4096;


    /**
     * formatPath 转义文件目录
     *
     * @param path
     * @return
     */
    public static String formatPath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * combainPath文件路径合并
     *
     * @param eins
     * @param zwei
     * @return
     */
    public static String combainPath(String eins, String zwei) {
        String dori = "";
        eins = null == eins ? "" : formatPath(eins);
        zwei = null == zwei ? "" : formatPath(zwei);
        if (!eins.endsWith("/") && zwei.indexOf("/") != 0) {
            dori = eins + "/" + zwei;
        } else {
            dori = (eins + zwei).replaceAll("//", "/");
        }
        return dori;
    }

    /**
     * list2Array 列表转换数组
     *
     * @param list
     * @return
     */
    public static String[] list2Array(List list) {
        String array[] = (String[]) list.toArray(new String[list.size()]);
        return array;
    }

    /**
     * cp 复制文件
     *
     * @param source
     * @param destination
     * @param loop
     * @return
     */
    public static List<File> cp(String source, String destination, boolean loop) {
        List<File> list = new ArrayList();
        try {
            File srcFile = new File(source);
            File desFile = new File(destination);
            list.addAll(cp(srcFile, desFile, loop));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return list;
    }

    public static void j2log(Object o, Object o1, Exception ex) {
        ex.printStackTrace();
    }

    /**
     * cp 复制文件
     *
     * @param source 源
     * @param destination 目的地
     * @param loop  是否递归遍历
     * @return
     */
    public static List<File> cp(File source, File destination, boolean loop) {
        List<File> list = new ArrayList();
        try {
            if (!source.exists() || source.isDirectory()) {
                throw new FileNotFoundException();
            }
            list.add(cp(source, destination));
            if (loop) {
                String[] subFile = source.list();
                for (String subPath : subFile) {
                    String src = combainPath(source.getPath(), subPath);//子文件原文件路径
                    String des = combainPath(destination.getPath(), subPath);//子文件目标路径
                    File subfile = new File(src);
                    if (subfile.isFile()) {
                        list.add(cp(src, des));
                    } else if (subfile.isDirectory() && loop) {
                        list.addAll(cp(src, des, loop));
                    }
                }
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return list;
    }

    /**
     * 复制文件夹
     *
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir)
                        .getAbsolutePath()
                        + File.separator + file[i].getName());
                if (sourceFile.getName().indexOf(".vax") < 0)
                    cp(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * cp 单文件复制文件
     *
     * @param source
     * @param destination
     * @return
     */
    public static File cp(String source, String destination) {
        File desFile = null;
        try {
            File srcFile = new File(source);
            desFile = new File(destination);
            desFile = cp(srcFile, desFile);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return desFile;
    }

    /**
     * cp 单文件复制文件
     *
     * @param source
     * @param destination
     * @return
     */
    public static File cp(File source, File destination) {
        FileInputStream in = null;
        FileOutputStream out = null;
        FileChannel inc = null;
        FileChannel outc = null;
        try {
            if (!source.exists() || source.isDirectory()) {
                throw new FileNotFoundException();
            }
            if (source.getPath().equals(destination.getPath())) {
                return source;
            }
            long allbytes = du(source, false);
            if (!destination.exists()) {
                destination.createNewFile();
            }
            in = new FileInputStream(source.getPath());
            out = new FileOutputStream(destination);
            inc = in.getChannel();
            outc = out.getChannel();
            ByteBuffer byteBuffer = null;
            long length = 2097152;//基本大小，默认2M
            long _2M = 2097152;
            while (inc.position() < inc.size()) {
                if (allbytes > (64 * length)) {//如果文件大小大于128M 缓存改为64M
                    length = 32 * _2M;
                } else if (allbytes > (32 * length)) {//如果文件大小大于64M 缓存改为32M
                    length = 16 * _2M;
                } else if (allbytes > (16 * length)) {//如果文件大小大于32M 缓存改为16M
                    length = 8 * _2M;
                } else if (allbytes > (8 * length)) {//如果文件大小大于16M 缓存改为8M
                    length = 4 * _2M;
                } else if (allbytes > (4 * length)) {//如果文件大小大于8M 缓存改为4M
                    length = 2 * _2M;
                } else if (allbytes > (2 * length)) {//如果文件大小大于4M 缓存改为2M
                    length = _2M;
                } else if (allbytes > (length)) {//如果文件大小大于2M 缓存改为1M
                    length = _2M / 2;
                } else if (allbytes < length) {//如果文件小于基本大小，直接输出
                    length = allbytes;
                }
                allbytes = inc.size() - inc.position();
                byteBuffer = ByteBuffer.allocateDirect((int) length);
                inc.read(byteBuffer);
                byteBuffer.flip();
                outc.write(byteBuffer);
                outc.force(false);
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        } finally {
            try {
                if (null != inc) {
                    inc.close();
                }
                if (null != outc) {
                    outc.close();
                }
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (Exception ex) {
                j2log(null, null, ex);
            }
        }
        return destination;
    }

    /**
     * rename 文件重命名
     *
     * @param filePath
     * @param from
     * @param to
     * @return
     */
    public static File rename(String filePath, String from, String to) {
        File newFile = null;
        try {
            File oldFile = new File(combainPath(filePath, from));
            newFile = new File(combainPath(filePath, to));
            rename(newFile, oldFile);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return newFile;
    }

    /**
     * rename 文件重命名
     *
     * @param to
     * @param from
     * @return
     */
    public static File rename(File from, File to) {
        try {
            String newPath = to.getPath();
            String oldPath = from.getPath();
            if (!oldPath.equals(newPath)) {
                if (!to.exists()) {
                    from.renameTo(to);
                }
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return to;
    }

    /**
     * mv 移动文件
     *
     * @param fileName
     * @param source
     * @param destination
     * @param cover
     */
    public static void mv(String fileName, String source, String destination, boolean cover) {
        try {
            if (!source.equals(destination)) {
                File oldFile = new File(combainPath(source, fileName));
                File newFile = new File(combainPath(destination, fileName));
                mv(oldFile, newFile, cover);
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * mv 移动文件
     *
     * @param source
     * @param destination
     * @param cover
     */
    public static void mv(String source, String destination, boolean cover) {
        try {
            if (!source.equals(destination)) {
                File oldFile = new File(source);
                File newFile = new File(destination);
                mv(oldFile, newFile, cover);
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * mv 移动文件
     *
     * @param source
     * @param destination
     * @param cover
     */
    public static void mv(File source, File destination, boolean cover) {
        try {
            if (!source.exists()) {
                throw new FileNotFoundException();
            }
            StringBuilder fileName = new StringBuilder(source.getName());
            if (!cover && source.getPath().equals(destination.getPath())) {
                if (fileName.indexOf(".") > 0) {
                    fileName.insert(fileName.lastIndexOf("."), "_副本");
                } else {
                    fileName.append("_副本");
                }
                cp(source, new File(combainPath(source.getParent(), fileName.toString())));
            } else {
                source.renameTo(destination);
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * extensions 获取文件扩展名信息
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static String[] extensions(String filePath, String fileName) {
        String[] extension = {};
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            extensions(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return extension;
    }

    /**
     * extensions 获取文件扩展名信息
     *
     * @param fullPath
     * @return
     */
    public static String[] extensions(String fullPath) {
        String[] extension = {};
        try {
            File file = new File(fullPath);
            extensions(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return extension;
    }

    /**
     * 判断目录是否存在
     *
     * @param strDir
     * @return
     */
    public static boolean existsDirectory(String strDir) {
        File file = new File(strDir);
        return file.exists() && file.isDirectory();
    }

    /**
     * 判断文件是否存在
     *
     * @param strDir
     * @return
     */
    public static boolean existsFile(String strDir) {
        File file = new File(strDir);
        return file.exists();
    }


    /**
     * 得到文件的大小
     *
     * @param fileName
     * @return
     */
    public static int getFileSize(String fileName) {

        File file = new File(fileName);
        FileInputStream fis = null;
        int size = 0;
        try {
            fis = new FileInputStream(file);
            size = fis.available();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * extensions 获取文件扩展名信息
     *
     * @param file
     * @return
     */
    public static String[] extensions(File file) {
        String[] extension = {};
        try {
            if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.lastIndexOf(".") >= 0) {
                    int lastIndex = fileName.lastIndexOf(".");
                    extension[0] = String.valueOf(lastIndex);//扩展名的“.”的索引
                    extension[1] = fileName.substring(lastIndex + 1);//扩展名
                    extension[2] = fileName.substring(0, lastIndex);//文件名
                }
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return extension;
    }

    /**
     * ls 遍历文件
     *
     * @param filePath
     * @param loop
     * @return
     */
    public static List<File> ls(String filePath, boolean loop) {
        List<File> list = new ArrayList();
        try {
            File file = new File(filePath);
            list.addAll(ls(file, loop));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return list;
    }

    /**
     * ls 遍历文件
     *
     * @param file
     * @param loop
     * @return
     */
    public static List<File> ls(File file, boolean loop) {
        List<File> list = new ArrayList();
        try {
            list.add(file);
            if (!file.isDirectory()) {
                list.add(file);
            } else if (file.isDirectory()) {
                File[] subList = file.listFiles();
                subList = filesSort(subList, true);
                for (File subFile : subList) {
                    if (subFile.isDirectory() && loop) {
                        list.addAll(ls(subFile.getPath(), loop));
                    } else {
                        list.add(subFile);
                    }
                }
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return list;
    }




    /**
     * filesSort 文件排序（默认升序）
     *
     * @return
     */
    public static File[] filesSort(File[] inFiles, boolean asc) {
        List<String> files = new ArrayList();
        List<String> dirs = new ArrayList();
        for (File subFile : inFiles) {
            if (subFile.isDirectory()) {
                dirs.add(subFile.getPath());
            } else if (subFile.isFile()) {
                files.add(subFile.getPath());
            }
        }
        String[] fileArray = {};
        if (files.size() > 0) {
            fileArray = list2Array(files);
            Arrays.sort(fileArray);
            if (!asc) {
                Arrays.sort(fileArray, Collections.reverseOrder());
            }
        }
        String[] dirArray = {};
        if (dirs.size() > 0) {
            dirArray = list2Array(dirs);
            Arrays.sort(dirArray);
            if (!asc) {
                Arrays.sort(dirArray, Collections.reverseOrder());
            }
        }
        return concat2FileArray(fileArray, dirArray);
    }

    /**
     * concat2FileArray 合并文件数组
     *
     * @param old1
     * @param old2
     * @return
     */
    public static File[] concat2FileArray(String[] old1, String[] old2) {
        File[] newArray = new File[old1.length + old2.length];
        for (int i = 0, n = old1.length; i < n; i++) {
            newArray[i] = new File(old1[i]);
        }
        for (int i = 0, j = old1.length, n = (old1.length + old2.length); j < n; i++, j++) {
            newArray[j] = new File(old2[i]);
        }
        return newArray;
    }

    /**
     * read 读取文本文件
     *
     * @param filePath
     * @param fileName
     * @param charset
     * @return
     */
    public static StringBuilder read(String filePath, String fileName, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            sb.append(FileUtil.tail(file, false, 0, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return sb;
    }

    /**
     * read 读取文本文件
     *
     * @param fullPath
     * @param charset
     * @return
     */
    public static StringBuilder read(String fullPath, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(fullPath);
            sb.append(FileUtil.tail(file, false, 0, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return sb;
    }

    /**
     * read 读取文本文件
     *
     * @param file
     * @param charset
     * @return
     */
    public static StringBuilder read(File file, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(FileUtil.tail(file, false, 0, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return sb;
    }

    /**
     * find 读取文本文件指定行
     *
     * @param filePath
     * @param fileName
     * @param line
     * @param charset
     * @return
     */
    public static StringBuilder find(String filePath, String fileName, int line, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            sb.append(FileUtil.tail(file, true, line, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return sb;
    }

    /**
     * find 读取文本文件指定行
     *
     * @param fullPath
     * @param line
     * @param charset
     * @return
     */
    public static StringBuilder find(String fullPath, int line, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(fullPath);
            sb.append(FileUtil.tail(file, true, line, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return sb;
    }

    /**
     * find 读取文本文件指定行
     *
     * @param file
     * @param line
     * @param charset
     * @return
     */
    public static StringBuilder find(File file, int line, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(FileUtil.tail(file, true, line, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return sb;
    }

    /**
     * tail 读取文本文件
     *
     * @param filePath
     * @param fileName
     * @param charset
     * @param find
     * @param line
     * @return
     */
    public static StringBuilder tail(String filePath, String fileName, boolean find, int line, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            sb.append(FileUtil.tail(file, find, line, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return sb;
    }

    /**
     * tail 读取文本文件
     *
     * @param fullPath
     * @param charset
     * @param find
     * @param line
     * @return
     */
    public static StringBuilder tail(String fullPath, boolean find, int line, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(fullPath);
            sb.append(FileUtil.tail(file, find, line, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return sb;
    }

    /**
     * tail 读取文本文件
     *
     * @param file
     * @param charset
     * @param find
     * @param line
     * @return
     */
    public static StringBuilder tail(File file, boolean find, int line, String charset) {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferReader = null;
        if (null == charset || "".equals(charset)) {
            charset = "UTF-8";
        }
        try {
            if (!file.exists() || file.isDirectory()) {
                throw new FileNotFoundException();
            }
            String fullPath = file.getPath();
            bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fullPath), charset));
            String temp;
            for (int i = 0; (temp = bufferReader.readLine()) != null; i++) {
                if (!find || line == i) {
                    sb.append(temp);
                }
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        } finally {
            if (null != bufferReader) {
                try {
                    bufferReader.close();
                } catch (IOException ex) {
                    j2log(null, null, ex);
                }
            }
        }
        return sb;
    }

    /**
     * sed 读取文本文件
     *
     * @param filePath
     * @param fileName
     * @param charset
     * @return
     */
    public static List<String> sed(String filePath, String fileName, String charset) {
        List<String> list = new ArrayList();
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            list.addAll(FileUtil.sed(file, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return list;
    }

    /**
     * sed 读取文本文件
     *
     * @param fullPath
     * @param charset
     * @return
     */
    public static List<String> sed(String fullPath, String charset) {
        List<String> list = new ArrayList();
        try {
            File file = new File(fullPath);
            list.addAll(FileUtil.sed(file, charset));
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return list;
    }

    /**
     * sed 读取文本文件
     *
     * @param file
     * @param charset
     * @return
     */
    public static List<String> sed(File file, String charset) {
        List<String> list = new ArrayList();
        BufferedReader bufferReader = null;
        if (null == charset || "".equals(charset)) {
            charset = "UTF-8";
        }
        try {
            if (!file.exists() || file.isDirectory()) {
                throw new FileNotFoundException();
            }
            String fullPath = file.getPath();
            bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fullPath), charset));
            String temp;
            for (int i = 0; (temp = bufferReader.readLine()) != null; i++) {
                list.add(temp);
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        } finally {
            if (null != bufferReader) {
                try {
                    bufferReader.close();
                } catch (IOException ex) {
                    j2log(null, null, ex);
                }
            }
        }
        return list;
    }

    /**
     * cat 读取文本文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static byte[] cat(String filePath, String fileName) {
        byte[] output = {};
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            output = FileUtil.cat(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return output;
    }

    /**
     * cat 读取文本文件
     *
     * @param fullPath
     * @return
     */
    public static byte[] cat(String fullPath) {
        byte[] output = {};
        try {
            File file = new File(fullPath);
            output = FileUtil.cat(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return output;
    }

    /**
     * cat 读取文本文件
     *
     * @param file
     * @return
     */
    public static byte[] cat(File file) {
        InputStream in = null;
        byte[] output = {};
        try {
            if (!file.exists() || file.isDirectory()) {
                throw new FileNotFoundException();
            }
            String fullPath = file.getPath();
            long length = du(file, false);
            long _2M = 2097152;
            byte[] bytes = new byte[(int) length];
            in = new FileInputStream(fullPath);
            for (int count = 0; count != -1; ) {
                if (length > 16 * _2M) {
                    length = 4 * _2M;
                } else if (length > 8 * _2M) {
                    length = 2 * _2M;
                } else if (length > 4 * _2M) {
                    length = _2M;
                } else if (length > 2 * _2M) {
                    length = _2M / 2;
                } else if (length > _2M) {
                    length = _2M / 4;
                } else {
                    length = 4096;
                }
                bytes = new byte[(int) length];
                count = in.read(bytes);
                output = concatArray(bytes, output);
                length = in.available();
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception ex) {
                    j2log(null, null, ex);
                }
            }
        }
        return output;
    }

    /**
     * 合并数组
     *
     * @param old1
     * @param old2
     * @return
     */
    public static byte[] concatArray(byte[] old1, byte[] old2) {
        byte[] newArray = new byte[old1.length + old2.length];
        System.arraycopy(old1, 0, newArray, 0, old1.length);
        System.arraycopy(old2, 0, newArray, old1.length, old2.length);
        return newArray;
    }

    /**
     * dd 写入文件fullPath内容content
     *
     * @param filePath
     * @param fileName
     * @param content
     * @param isAppend
     */
    public static void dd(String filePath, String fileName, byte[] content, boolean isAppend) {
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            FileUtil.dd(file, content, isAppend);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * dd 写入文件fullPath内容content
     *
     * @param fullPath
     * @param content
     * @param isAppend
     */
    public static void dd(String fullPath, byte[] content, boolean isAppend) {
        try {
            File file = new File(fullPath);
            FileUtil.dd(file, content, isAppend);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * dd 写入文件fullPath内容content
     *
     * @param file
     * @param content
     * @param isAppend
     */
    public static void dd(File file, byte[] content, boolean isAppend) {
        FileOutputStream fileOutputStream = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file, isAppend);
            fileOutputStream.write(content);
        } catch (Exception ex) {
            j2log(null, null, ex);
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException ex) {
                j2log(null, null, ex);
            }
        }
    }

    /**
     * write 写文件内容content到文件fullPath
     *
     * @param filePath
     * @param fileName
     * @param content
     */
    public static void write(String filePath, String fileName, String content) {
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            FileUtil.write(file, content, true);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * write 写文件内容content到文件fullPath
     *
     * @param fullPath
     * @param content
     */
    public static void write(String fullPath, String content) {
        try {
            File file = new File(fullPath);
            FileUtil.write(file, content, true);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * write 写文件内容content到文件fullPath
     *
     * @param file
     * @param content
     */
    public static void write(File file, String content) {
        try {
            FileUtil.write(file, content, true);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * write 写（追加）文件内容content到文件fullPath
     *
     * @param filePath
     * @param fileName
     * @param content
     * @param isAppend
     */
    public static void write(String filePath, String fileName, String content, boolean isAppend) {
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            FileUtil.write(file, content, isAppend);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * write 写（追加）文件内容content到文件fullPath
     *
     * @param fullPath
     * @param content
     * @param isAppend
     */
    public static void write(String fullPath, String content, boolean isAppend) {
        try {
            File file = new File(fullPath);
            FileUtil.write(file, content, isAppend);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * write 写（追加）文件内容content到文件fullPath
     *
     * @param file
     * @param content
     * @param isAppend
     */
    public static void write(File file, String content, boolean isAppend) {
        FileWriter fileWriter = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getPath(), isAppend);
            fileWriter.write(content);
        } catch (Exception ex) {
            j2log(null, null, ex);
        } finally {
            if (null != fileWriter) {
                try {
                    fileWriter.close();
                } catch (IOException ex) {
                    j2log(null, null, ex);
                }
            }
        }
    }

    /**
     * tail 添加文件内容content到文件的index位置
     *
     * @param filePath
     * @param fileName
     * @param content
     * @param index
     */
    public static void tail(String filePath, String fileName, String content, long index) {
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            FileUtil.tail(file, content, index);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * tail 添加文件内容content到文件的index位置
     *
     * @param fullPath
     * @param content
     * @param index
     */
    public static void tail(String fullPath, String content, long index) {
        try {
            File file = new File(fullPath);
            FileUtil.tail(file, content, index);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * tail 添加文件内容content到文件的index位置
     *
     * @param file
     * @param content
     * @param index
     */
    public static void tail(File file, String content, long index) {
        RandomAccessFile randomAccessFile = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            randomAccessFile = new RandomAccessFile(file.getPath(), "rw");
            randomAccessFile.seek(index);
            randomAccessFile.writeBytes(content);
        } catch (Exception ex) {
            j2log(null, null, ex);
        } finally {
            if (null != randomAccessFile) {
                try {
                    randomAccessFile.close();
                } catch (Exception ex) {
                    j2log(null, null, ex);
                }
            }
        }
    }

    /**
     * mkdir 创建目录
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File mkdir(String filePath, String fileName) {
        File file = null;
        try {
            String fullPath = combainPath(filePath, fileName);
            file = new File(fullPath);
            file = mkdir(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return file;
    }

    /**
     * mkdir 创建目录
     *
     * @param fullPath
     * @return
     */
    public static boolean mkdir(String fullPath) {
        File file = null;
        try {
            file = new File(fullPath);
            file = mkdir(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return existsDirectory(fullPath);
    }


    /**
     * mkdir 创建目录
     *
     * @param file
     * @return
     */
    public static File mkdir(File file) {
        try {
            if (!file.exists()) {
                file.mkdir();//如果文件夹不存在则创建
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return file;
    }


    /**
     * touch 创建文件
     *
     * @param filePath
     * @param fileName
     */
    public static void touch(String filePath, String fileName) {
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            touch(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * touch 创建文件
     *
     * @param fullPath
     */
    public static void touch(String fullPath) {
        try {
            File file = new File(fullPath);
            touch(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * touch 创建文件
     *
     * @param file
     */
    public static void touch(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();//如果文件不存在则创建
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * rm 删除文件
     *
     * @param filePath
     * @param fileName
     */
    public static void rm(String filePath, String fileName) {
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            rm(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * rm 删除文件
     *
     * @param fullPath
     */
    public static void rm(String fullPath) {
        try {
            File file = new File(fullPath);
            rm(file);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * rm 删除文件
     *
     * @param file
     */
    public static void rm(File file) {
        try {
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            if (file.isFile()) {
                file.delete();
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * rmdir 删除目录
     *
     * @param filePath
     * @param fileName
     * @param loop
     */
    public static void rmdir(String filePath, String fileName, boolean loop) {
        try {
            String fullPath = combainPath(filePath, fileName);
            File dir = new File(fullPath);
            rmdir(dir, loop);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * rmdir 删除目录
     *
     * @param fullPath
     * @param loop
     */
    public static void rmdir(String fullPath, boolean loop) {
        try {
            File dir = new File(fullPath);
            rmdir(dir, loop);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * rmdir 删除目录
     *
     * @param dir
     * @param loop
     */
    public static void rmdir(File dir, boolean loop) {
        try {
            if (!dir.exists()) {
                throw new FileNotFoundException();
            }
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                int length = files.length;
                for (int i = 0; i < length && loop; i++) {
                    if (files[i].isDirectory()) {
                        rmdir(files[i], loop);
                    } else {
                        rm(files[i]);
                    }
                }
                if (loop || length == 0) {
                    dir.delete();
                }
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
    }

    /**
     * du 获取文件实际大小
     *
     * @param filePath
     * @param fileName
     * @param loop
     * @return
     */
    public static long du(String filePath, String fileName, boolean loop) {
        long size = 0;
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            size = du(file, loop);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return size;
    }

    /**
     * du 获取文件实际大小
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static long du(String filePath, String fileName) {
        long size = 0;
        try {
            String fullPath = combainPath(filePath, fileName);
            File file = new File(fullPath);
            size = du(file, false);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return size;
    }

    /**
     * du 获取文件实际大小
     *
     * @param fullPath
     * @return
     */
    public static long du(String fullPath) {
        long size = 0;
        try {
            File file = new File(fullPath);
            size = du(file, false);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return size;
    }

    /**
     * du 获取文件实际大小
     *
     * @param file
     * @return
     */
    public static long du(File file) {
        long size = 0;
        try {
            size = du(file, false);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return size;
    }

    /**
     * du 获取文件实际大小
     *
     * @param fullPath
     * @param loop
     * @return
     */
    public static long du(String fullPath, boolean loop) {
        long size = 0;
        try {
            File file = new File(fullPath);
            size = du(file, loop);
        } catch (Exception ex) {
            j2log(null, null, ex);
        }
        return size;
    }

    /**
     * du 获取文件实际大小
     *
     * @param file
     * @param loop
     * @return
     */
    public static long du(File file, boolean loop) {
        FileChannel fileChannel = null;
        long size = 0;
        try {
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            if (file.isFile()) {
                FileInputStream fis = new FileInputStream(file);
                fileChannel = fis.getChannel();
                size = fileChannel.size();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                int length = files.length;
                for (int i = 0; i < length && loop; i++) {
                    if (files[i].isDirectory()) {
                        du(files[i], loop);
                    } else {
                        size += du(files[i], false);
                    }
                }
            }
        } catch (Exception ex) {
            j2log(null, null, ex);
        } finally {
            if (null != fileChannel) {
                try {
                    fileChannel.close();
                } catch (Exception ex) {
                    j2log(null, null, ex);
                }
            }
        }
        return size;
    }

    /**
     * 获取文件编码格式
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String getFileEncoding(String fileName) {
        int p = 0;
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(fileName));
            p = (bin.read() << 8) + bin.read();
        } catch (IOException ex) {
            j2log(null, null, ex);
        } finally {
            if (null != bin) {
                try {
                    bin.close();
                } catch (IOException ex) {
                    j2log(null, null, ex);
                }
            }
        }
        String code = null;
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }

        return code;
    }


    /**
     * convert the contents of the given byte array to the given output File.
     *
     * @param in  the byte array to convert from
     * @param out the file to convert to
     * @throws IOException in case of I/O errors
     */
    public static void convert(byte[] in, File out) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(in);
        OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
        convert(inStream, outStream);
    }

    /**
     * convert the contents of the given input File into a new byte array.
     *
     * @param in the file to convert from
     * @return the new byte array that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(File in) throws IOException {
        return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
    }

    /**
     * convert the contents of the given InputStream to the given OutputStream.
     * Closes both streams when done.
     *
     * @param in  the stream to convert from
     * @param out the stream to convert to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int convert(InputStream in, OutputStream out) throws IOException {
        try {
            int byteCount = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * convert the contents of the given byte array to the given OutputStream.
     * Closes the stream when done.
     *
     * @param in  the byte array to convert from
     * @param out the OutputStream to convert to
     * @throws IOException in case of I/O errors
     */
    public static void convert(byte[] in, OutputStream out) throws IOException {
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * convert the contents of the given InputStream into a new byte array.
     * Closes the stream when done.
     *
     * @param in the stream to convert from
     * @return the new byte array that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        convert(in, out);
        return out.toByteArray();
    }

    /**
     * convert the contents of the given Reader to the given Writer.
     * Closes both when done.
     *
     * @param in  the Reader to convert from
     * @param out the Writer to convert to
     * @return the number of characters copied
     * @throws IOException in case of I/O errors
     */
    public static int convert(Reader in, Writer out) throws IOException {
        try {
            int byteCount = 0;
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * convert the contents of the given String to the given output Writer.
     * Closes the write when done.
     *
     * @param in  the String to convert from
     * @param out the Writer to convert to
     * @throws IOException in case of I/O errors
     */
    public static void convert(String in, Writer out) throws IOException {
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * convert the contents of the given Reader into a String.
     * Closes the reader when done.
     *
     * @param in the reader to convert from
     * @return the String that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static String copyToString(Reader in) throws IOException {
        StringWriter out = new StringWriter();
        convert(in, out);
        return out.toString();
    }


    /**
     * 根据路径遍历文件，获取相关属性
     * @param pathName 文件路线
     * @return
     * @throws IOException
     */
    public static List<Map> loopTraversal(String pathName) throws IOException {
        List<Map> list=new ArrayList<>();
        int filecount=0;
        //获取pathName的File对象
        File dirFile = new File(pathName);
        //判断该文件或目录是否存在，不存在时在控制台输出提醒
        if (!dirFile.exists()) {
            System.out.println("do not exit");
            return list;
        }
        //判断如果不是一个目录，就判断是不是一个文件，时文件则输出文件路径
        if (!dirFile.isDirectory()) {
            if (dirFile.isFile()) {
                System.out.println(dirFile.getCanonicalFile());
            }
            return list;
        }

        System.out.print("|--");
        System.out.println(dirFile.getName());
        //获取此目录下的所有文件名与目录名
        String[] fileList = dirFile.list();
        for (int i = 0; i < fileList.length; i++) {
            //遍历文件目录
            String string = fileList[i];
            //File("documentName","fileName")是File的另一个构造器
            File file = new File(dirFile.getPath(),string);
            //读取文件名
            String name = file.getName();
            long size= file.length()/1024/1024;
            String filePath = file.getPath();
            //如果是一个目录，搜索深度depth++，输出目录名后，进行递归
            if (file.isDirectory()) {
                //递归
                list.addAll(loopTraversal(file.getCanonicalPath()));
            }else{
                Calendar cal = Calendar.getInstance();
                long time = file.lastModified();
                cal.setTimeInMillis(time);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Map map=new HashMap();
                map.put("depth",0);
//                map.put("id",filePath);
                map.put("id",pathName.substring(pathName.lastIndexOf("\\")+1)+"/"+name);
                map.put("name",name);
                map.put("size",size);
                map.put("createTime",formatter.format(cal.getTime()));
                list.add(map);

            }
        }
        return list;
    }




    /**
     * 根据路径遍历文件，获取相关属性
     * @param pathName 文件路线
     * @param firstDepth  起始深度  第一层规定的深度，默认推荐传0
     * @return
     * @throws IOException
     */
    public static List<Map> loopTraversal(String pathName, int firstDepth) throws IOException {
        List<Map> list=new ArrayList<>();
        int filecount=0;
        //获取pathName的File对象
        File dirFile = new File(pathName);
        //判断该文件或目录是否存在，不存在时在控制台输出提醒
        if (!dirFile.exists()) {
            System.out.println("do not exit");
            return list;
        }
        //判断如果不是一个目录，就判断是不是一个文件，时文件则输出文件路径
        if (!dirFile.isDirectory()) {
            if (dirFile.isFile()) {
                System.out.println(dirFile.getCanonicalFile());
            }
            return list;
        }

        for (int j = 0; j < firstDepth; j++) {
            System.out.print("  ");
        }
        System.out.print("|--");
        System.out.println(dirFile.getName());
        //获取此目录下的所有文件名与目录名
        String[] fileList = dirFile.list();
        int currentDepth=firstDepth+1;
        for (int i = 0; i < fileList.length; i++) {
            //遍历文件目录
            String string = fileList[i];
            //File("documentName","fileName")是File的另一个构造器
            File file = new File(dirFile.getPath(),string);
            //读取文件名
            String name = file.getName();
            long size= file.length()/1024/1024;
            String filePath = file.getPath();
            //如果是一个目录，搜索深度depth++，输出目录名后，进行递归
            if (file.isDirectory()) {
                //递归
                list.addAll(loopTraversal(file.getCanonicalPath(),currentDepth));
            }else{
                Calendar cal = Calendar.getInstance();
                long time = file.lastModified();
                cal.setTimeInMillis(time);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Map map=new HashMap();
                map.put("id",name);
                map.put("name",name);
                map.put("size",size);
                map.put("depth",currentDepth);
                map.put("createTime",cal.getTime());
//                map.put("filePath",filePath);
                list.add(map);

            }
        }
        return list;
    }

    /*
     * @Description //
     * @Param [pathName]
     * @return java.util.List<java.util.Map>
     **/
    public static List<Map> directorys(String pathName){
        List<Map> list=new ArrayList<>();
        //获取pathName的File对象
        File dirFile = new File(pathName);
        //判断该文件或目录是否存在，不存在时在控制台输出提醒
        if (!dirFile.exists()) {
            System.out.println("do not exit");
            return list;
        }
        //获取此目录下的所有目录
        String[] fileList = dirFile.list();
        for (int i = 0; i < fileList.length; i++) {
            //遍历目录
            String string = fileList[i];
            //File("documentName","fileName")是File的另一个构造器
            File file = new File(dirFile.getPath(), string);
            //读取目录名
            String name = file.getName();
            String filePath = file.getPath();
            Map map=new HashMap();
            map.put("id",filePath);
            map.put("text",name);
            list.add(map);
        }
        return list;
    }

    public static List<Object> treeList(String pathName){
        List<Map> directorys =directorys(pathName);
        List<Object> treeList = new ArrayList<>();
        Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
        mapArr.put("id",pathName);
        mapArr.put("text",pathName);
        mapArr.put("children",directorys);
        treeList.add(mapArr);
        return treeList;
    }

    public static void main(String[] args) {
//        System.out.println(getFileEncoding("output.txt"));
    }
}
