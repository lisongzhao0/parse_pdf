package com.happy.gene.utility;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by zhaolisong on 31/05/2017.
 */
public class FileUtil {

    public static FileUtil newInstance() {
        return new FileUtil();
    }

    private SetUtil    setUtil    = SetUtil.newInstance();
    private StringUtil stringUtil = StringUtil.newInstance();

    private FileUtil() {}

    public String getFileName(String path) {
        String fileName = path;
        int lastIndexOfSlash = fileName.lastIndexOf('\\');
        int lastIndexOfBackslash = fileName.lastIndexOf('/');
        if (lastIndexOfBackslash>0) {
            fileName = fileName.substring(lastIndexOfBackslash+1);
        }
        else if (lastIndexOfSlash>0) {
            fileName = fileName.substring(lastIndexOfSlash+1);
        }
        else {
            fileName = System.currentTimeMillis()+"";
        }
        return fileName;
    }

    public FileInputStream getFileInputStream(String path) {
        if (!fileExist(path)) {
            return null;
        }
        File file = new File(path);
        try { return new FileInputStream(file); }
        catch (FileNotFoundException ex) { return null; }
    }

    public FileOutputStream getFileOutputStream(String path, boolean override) {
        if (fileExist(path)) {
            if (!override) {
                return null;
            }
            else {
                deleteFile(path);
            }
        }
        File file = new File(path);
        try { return new FileOutputStream(file); }
        catch (FileNotFoundException ex) { return null; }
    }

    public File getFile(String path) {
        if (!fileExist(path)) {
            return null;
        }
        File file = new File(path);
        return file;
    }

    public List<File> getDirectoryFiles(String dirAbsolutePath, String suffix) {
        List<File>  files   = getDirectoryFiles(dirAbsolutePath);
        if (setUtil.isListEmpty(files)) {
            return files;
        }

        for (int i=0; i < files.size(); i ++) {
            File tmp = files.get(i);
            if (!tmp.getName().endsWith(suffix)) {
                files.remove(i);
                i --;
                continue;
            }
        }
        return files;
    }

    public List<File> getDirectoryFiles(String dirAbsolutePath) {
        List<File>  result  = new ArrayList<>();
        if (stringUtil.isEmpty(dirAbsolutePath)) {
            System.err.println("delete no directory");
            return null;
        }

        File dir = new File(dirAbsolutePath);
        boolean exist = dir.exists();
        if (!exist) {
            return null;
        }
        if (dir.isFile()) {
            return null;
        }

        List<File>  dirSearched = new ArrayList<>();
        File[]      dirOrFile   = dir.listFiles();
        if (null != dirOrFile) {
            for (File tmp : dirOrFile) {
                getDirectoryFiles(tmp, result, dirSearched);
            }
        }

        return result;
    }

    private void getDirectoryFiles(File fileOrDir, List<File> result, List<File> dirSearched) {
        if (null==fileOrDir) {
            return;
        }
        if (fileOrDir.isFile()) {
            result.add(fileOrDir);
            return;
        }
        if (dirSearched.contains(fileOrDir)) {
            return;
        }
        dirSearched.add(fileOrDir);

        File[]  dirOrFile   = fileOrDir.listFiles();
        if (null != dirOrFile) {
            for (File tmp : dirOrFile) {
                getDirectoryFiles(tmp, result, dirSearched);
            }
        }
        return;
    }

    public void writeFile(InputStream input, File outputFile) throws IOException {
        File outputDir = outputFile.getParentFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        OutputStream output = null;
        output = new FileOutputStream(outputFile);
        byte[] buffer = new byte[10240];

        int read = 0;
        while ((read = input.read(buffer))>0) {
            output.write(buffer, 0, read);
            read = 0;
        }
        output.flush();
        output.close();
    }

    public void writeFile(InputStream input, String outputFile) throws IOException {
        writeFile(input, new File(outputFile));
    }

    public boolean copyFile(File fromFile, File toFile) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fromFile);
            out = new FileOutputStream(toFile);
            return copyStream(in, out);
        }
        catch (Exception e) {
            StringBuilder errMsg = new StringBuilder();
            errMsg.append("failed! copy file from ").append(fromFile.getAbsolutePath())
                  .append(" to ").append(toFile.getAbsolutePath())
                  .append(", exception is ").append(e.getMessage());
            System.err.println(errMsg.toString());
            return false;
        }
        finally {
            if (out != null) {
                try { out.close(); } catch (IOException ioe) {}
            }
            if (in != null) {
                try { in.close(); } catch (IOException ioe) {}
            }
        }
    }

    public boolean copyStream(InputStream in, OutputStream out) {
        final int MAX = 10240;
        byte[] buf = new byte[MAX];
        try {
            for (int bytesRead = in.read(buf, 0, MAX); bytesRead != -1; bytesRead = in.read(buf, 0, MAX)) {
                out.write(buf, 0, bytesRead);
            }
            return true;
        }
        catch (IOException ioe) {
            StringBuilder errMsg = new StringBuilder();
            errMsg.append("failed! copy stream, exception is ").append(ioe.getMessage());
            System.err.println(errMsg.toString());
            return false;
        }
    }

    public boolean moveFile(String srcPath, String destPath) {
        File src = new File(srcPath);
        File dest = new File(destPath);

        if (dest.exists()) {
            StringBuilder errMsg = new StringBuilder();
            errMsg.append("dest file is exist, will delete it, destFile=").append(dest);
            System.err.println(errMsg.toString());
            dest.delete();
        }
        if (!dest.getParentFile().exists()) {
            if (!dest.getParentFile().mkdirs()) {
                StringBuilder errMsg = new StringBuilder();
                errMsg.append("make dest file's parent directory failed!, destFile=").append(dest);
                System.err.println(errMsg.toString());
                return false;
            }
        }
        if (!(src.isFile() && src.exists())) {
            StringBuilder errMsg = new StringBuilder();
            errMsg.append("src file is not file, or not existed! srcFile").append(src);
            System.err.println(errMsg.toString());
            return false;
        }

        boolean success = copyFile(src, dest);
        success = success && deleteFile(srcPath);
        if (!success) {
            StringBuilder errMsg = new StringBuilder();
            errMsg.append("Failed to move ").append(src).append(" to ").append(dest);
            System.err.println(errMsg.toString());
        }
        return success;
    }

    public void moveFiles(Map<String, String> src2dest) {
        if (setUtil.isMapEmpty(src2dest)) {
            System.err.println("move no files from src to dest");
            return;
        }
        Set<String> srcKeys = src2dest.keySet();
        for (String src : srcKeys) {
            String dest = src2dest.get(src);
            moveFile(src, dest);
        }
    }

    public boolean fileExist(String fileAbsolutePath) {
        if (stringUtil.isEmpty(fileAbsolutePath)) {
            return false;
        }
        File file = new File(fileAbsolutePath);
        boolean exist = file.exists();
        return exist && file.isFile();
    }

    public String fileAbsolutePath(String fileAbsolutePath) {
        if (stringUtil.isEmpty(fileAbsolutePath)) {
            return null;
        }
        File file = new File(fileAbsolutePath);
        boolean exist = file.exists();
        if (exist) {
            return file.getAbsolutePath();
        }
        else {
            return null;
        }
    }

    public boolean directoryExist(String fileAbsolutePath) {
        if (stringUtil.isEmpty(fileAbsolutePath)) {
            return false;
        }
        File file = new File(fileAbsolutePath);
        boolean exist = file.exists();
        return exist && file.isDirectory();
    }

    public boolean makeDirectory(String parentDir, String dir) {
        File file = new File(parentDir);
        if (!(dir == null || dir.trim().equals(""))) {//子目录不为空
            file = new File(parentDir + "/" + dir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            return file.mkdirs();
        }
        return true;
    }

    public boolean deleteDirectory(String dirAbsolutePath) {
        if (stringUtil.isEmpty(dirAbsolutePath)) {
            System.err.println("delete no directory");
            return true;
        }

        File dir = new File(dirAbsolutePath);
        boolean exist = dir.exists();
        if (!exist) {
            return true;
        }
        if (dir.isFile()) {
            return true;
        }

        return deleteDirectory(dir);
    }

    private boolean deleteDirectory(File dir) {
        if (null==dir || !dir.exists()) {
            return true;
        }

        File[] files = dir.listFiles();
        for (int i = 0; null!=files && i < files.length; i++) {
            if (!deleteDirectory(files[i])) {
                return false;
            }
        }

        if (dir.isFile()) {
            dir.delete();
            return true;
        }
        dir.delete();
        return true;
    }

    public boolean deleteFile(String fileAbsolutePath) {
        if (stringUtil.isEmpty(fileAbsolutePath)) {
            System.err.println("delete no file");
            return true;
        }
        File file = new File(fileAbsolutePath);
        boolean exist = file.exists();
        if (!exist) {
            return true;
        }
        if (file.isDirectory()) {
            return true;
        }
        return file.delete();
    }

    public void deleteFiles(List<String> fileAbsolutePaths) {
        if (setUtil.isListEmpty(fileAbsolutePaths)) {
            return;
        }
        for (String relPath : fileAbsolutePaths) {
            boolean success = deleteFile(relPath);
            if (!success) {
                System.err.println("fail to delete file="+relPath);
            }
        }
    }

    public List<String> readTextByLine(String text) {
        if (null==text || text.trim().isEmpty()) { return null; }
        try {
            BufferedReader reader   = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes("UTF-8"))));
            List<String>   lines    = new ArrayList<>();

            String  strRow = null;
            while (null!=(strRow=reader.readLine())) {
                lines.add(strRow);
            }
            return lines;
        }
        catch (Exception ex){}
        return null;
    }

    /*=================================================================
     *       storage file system structure utilities
     *=================================================================*/
    public static final String FileFormatter = "^[0-9|a-f|A-F]{2,2}/[0-9|a-f|A-F]+$";

    /**
     * 例如：
     * localBaseDir：/data/storage/
     * fileRelativePath：http://www.baidu.com/website/xxxyyyzzz/xx/xxxxxxxx
     *
     * 判断文件目录（例如：/data/storage/xxxyyyzzz/xx）下是否有文件 xxxxxxxx
     * @param localBaseDir 文件目录（例如：/data/storage/）
     * @param fileRelativePath 文件路径（例如：http://www.baidu.com/website/xxxyyyzzz/xx/xxxxxxxx）
     */
    public boolean localFileExist(String localBaseDir, String fileRelativePath) {
        if (stringUtil.isEmpty(fileRelativePath)) {
            System.err.println("relative file path is empty");
            return false;
        }
        if (stringUtil.isEmpty(localBaseDir)) {
            System.err.println("local base dir is empty");
            return false;
        }

        String[] baseurlDirSha1 = decodeFilePath(fileRelativePath);
        String filePath = localBaseDir + baseurlDirSha1[1] + File.separator + baseurlDirSha1[2] + File.separator + baseurlDirSha1[3];
        return fileExist(filePath);
    }

    /**
     * 例如：
     * filePath：http://www.baidu.com/website/official/xx/xxxxxxxx
     * filePath：/data/storage_go2nurse/site/official/xx/xxxxxxxx
     * 返回：
     * official/xx/xxxxxxxx
     *
     * 获取文件在 cooltoo 中的相对路径
     * @param resourceUrl 文件目录（例如：http://www.baidu.com/website/xxxyyyzzz/xx/xxxxxxxx）
     * @return 文件在 cooltoo 中的相对路径（例如：xxxyyyzzz/xx/xxxxxxxx）
     */
    public String localFileRelativePath(String resourceUrl) {
        if (stringUtil.isEmpty(resourceUrl)) {
            System.err.println("relative file path is empty");
            return "";
        }
        String[] baseUrlDirSha1 = decodeFilePath(resourceUrl);
        if (stringUtil.isEmpty(baseUrlDirSha1[1]) &&
            stringUtil.isEmpty(baseUrlDirSha1[2]) &&
            stringUtil.isEmpty(baseUrlDirSha1[3])) {
            System.err.println("decode dir and filename is empty");
            return "";
        }
        String relativePath = baseUrlDirSha1[1] + File.separator + baseUrlDirSha1[2] + File.separator + baseUrlDirSha1[3];
        return relativePath;
    }

    /**
     * 例如：
     * fileUrls：http://www.baidu.com/website/xxxyyyzzz/xx/xxxxxxxx
     * fileUrls：/data/storage_go2nurse/site/xxxyyyzzz/xx/xxxxxxxx
     * 返回：
     * xxxyyyzzz/xx/xxxxxxxx
     *
     * 获取文件在 cooltoo 中的相对路径
     * @param fileUrls 文件目录（例如：http://www.baidu.com/website/xxxyyyzzz/xx/xxxxxxxx）
     * @return 文件和文件在 cooltoo 中的相对路径的映射关系
     *          （例如：http://www.baidu.com/website/xxxyyyzzz/xx/xxxxxxxx<--->xxxyyyzzz/xx/xxxxxxxx）
     */
    public Map<String, String> localFileRelativePath(List<String> fileUrls) {
        Map<String, String> fileRelativePaths = new HashMap<>();
        if (setUtil.isListEmpty(fileUrls)) {
            System.err.println("the file url list is empty");
            return fileRelativePaths;
        }

        for (String filepath : fileUrls) {
            String relativePath = localFileRelativePath(filepath);
            if (stringUtil.isEmpty(relativePath)) {
                System.err.println("decode file="+filepath+" dir and filename is empty");
                fileRelativePaths.clear();
                return fileRelativePaths;
            }
            fileRelativePaths.put(filepath, relativePath);
        }

        return fileRelativePaths;
    }

    /**
     * 对文件进行编码，返回为文件所在的子路径和文件名两部分
     * @param fileName 文件名
     * @return  [dir, sha1]
     */
    public String[] encodeFilePath(String fileName) throws NoSuchAlgorithmException {
        if (stringUtil.isEmpty(fileName)) {
            fileName = "unknown";
            System.err.println("the file name is invalid, set it to="+fileName);
        }
        if (stringUtil.isEmpty(fileName)) {
            fileName = System.currentTimeMillis()+"_"+System.nanoTime();
        }
        else {
            if (fileName.length()>50) {
                fileName = fileName.substring(0, 50);
            }
        }

        long   nanoTime     = System.nanoTime();
        String strNanoTime  = fileName+"_"+nanoTime;
        String sha1         = stringUtil.sha1(strNanoTime);
        String newDirecotry = sha1.substring(0, 2);
        String newFileName  = sha1.substring(2);
        return new String[]{newDirecotry, newFileName};
    }

    /**
     * 对 cooltoo 文件路径进行解码，返回为文件所在的子路径和文件名两部分
     * @param localFilePath cooltoo 文件路径
     * @return  [url 前缀, 分类目录,  dir 子目录, sha1 文件名]
     */
    public String[] decodeFilePath(String localFilePath) {
        if (stringUtil.isEmpty(localFilePath)) {
            System.err.println("the file path is empth");
            throw new RuntimeException("PARAMETER_IS_EMPTY");
        }

        StringBuilder splash = new StringBuilder();
        splash.append('\\').append('\\');
        localFilePath = localFilePath.replaceAll(splash.toString(), "/");

        String[] component    = localFilePath.split("/");
        if (component.length<2) {
            return new String[]{"", "", component[0]};
        }
        if (component.length<3) {
            return new String[]{"", component[0], component[1]};
        }
        String tmpType      = component[component.length-3];
        String tmpDirectory = component[component.length-2];
        String tmpSha1  = component[component.length-1];

        int      baseUrlEndIdx= localFilePath.indexOf(tmpType+"/"+tmpDirectory+"/"+tmpSha1);
        String   baseUrl      = "";
        if (baseUrlEndIdx>1) {
            baseUrl = localFilePath.substring(0, baseUrlEndIdx-1);
        }

        return new String[]{baseUrl, tmpType, tmpDirectory, tmpSha1};
    }

    /**
     * 对 cooltoo 文件路径进行解构，返回为文件所在的子路径和文件名两部分
     * @param cooltooFilePaths cooltoo 文件路径
     * @return  源文件路径与解构后 [url 前缀, 分类目录, dir 子目录, sha1 文件名] 的映射关系
     */
    public Map<String, String[]> decodeFilePaths(List<String> cooltooFilePaths) {
        if (setUtil.isListEmpty(cooltooFilePaths)) {
            return new HashMap<>();
        }
        Map<String, String[]> path2UrlDirSha1 = new HashMap<>();
        for (String tmpPath : cooltooFilePaths) {
            String[] urlDirSha1 = decodeFilePath(tmpPath);
            path2UrlDirSha1.put(tmpPath, urlDirSha1);
        }
        return path2UrlDirSha1;
    }



    /**
     * 文件系统中的文件移动到 dest 中
     * @param srcAbsPath 文件系统路径
     * @param destTypeDir 目标文件目录
     * @return  源文件路径与移动后路径的映射
     */
    public Map<String, String> moveFilesToDest(List<String> srcAbsPath,
                                               String destTypeDir,
                                               boolean needEveryMovementOk) {
        if (setUtil.isListEmpty(srcAbsPath)) {
            System.err.println("the files list is empty");
            return new HashMap<>();
        }
        if (null==destTypeDir) {
            System.err.println("the dest storage is empty");
            return new HashMap<>();
        }

        // absolute_or_relative_file_path_in_temporary---->relative_file_path_in_storage
        String currentMoving = "currentMoving";
        Map<String, String> filePath2StoragePath = new Hashtable<>();
        Map<String, String> successMoved         = new Hashtable<>();
        File fileExist;
        try {
            for (String srcFilePath : srcAbsPath) {
                fileExist = new File(srcFilePath);
                if (!fileExist.exists()) {
                    System.err.println("file not exist. file is "+srcFilePath);
                    continue;
                }
                currentMoving = srcFilePath;
                String[] baseurlDirSha1 = decodeFilePath(srcFilePath);
                // relative_file_path_in_temporary--->dir/sha1

                String storagePath = baseurlDirSha1[0];
                // dest_file_dir--->storage_base_path/dir
                String destDirPath  = storagePath + File.separator + destTypeDir + File.separator + baseurlDirSha1[2] + File.separator;
                // dest_file_path--->storage_base_path/dir/sha1
                String destFilePath = destDirPath + baseurlDirSha1[3];

                // make the storage directory if necessary
                File destDir = new File(destDirPath);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                // move temporary file to storage dir
                boolean movedSuccessfully = moveFile(srcFilePath, destFilePath);
                if (needEveryMovementOk && !movedSuccessfully) {
                    throw new IOException("move file failed!");
                }
                successMoved.put(destFilePath, srcFilePath);

                String relativeFilePath = destTypeDir
                                        + File.separator
                                        + baseurlDirSha1[2]
                                        + File.separator
                                        + baseurlDirSha1[3];
                filePath2StoragePath.put(srcFilePath, relativeFilePath);
            }
            return filePath2StoragePath;
        }
        catch (Exception ex) {
            System.err.println("move file="+currentMoving+" failed! exception="+ex);
            filePath2StoragePath.clear();
            moveFiles(successMoved);/* rollback file moved */
            throw new RuntimeException("OPERATION_FAILED");
        }
    }

    /**
     * 将 cooltoo 文件系统中的文件从 src 移动到 dest 中
     * @param pathsInStorage cooltoo 文件路径
     * @param baseDir 文件系统目录
     * @param srcTypeDir 源文件目录
     * @param destTypeDir 目标文件目录
     * @return  文件在目标文件系统中的路径
     */
    public List<String> moveFileFromSrcToDest(List<String> pathsInStorage,
                                              String baseDir,
                                              String srcTypeDir,
                                              String destTypeDir) {
        if (setUtil.isListEmpty(pathsInStorage)) {
            System.err.println("nothing need to move");
            return new ArrayList<>();
        }
        if (null==srcTypeDir) {
            System.err.println("src is empty");
            return new ArrayList<>();
        }
        if (null==destTypeDir) {
            System.err.println("dest is empty");
            return new ArrayList<>();
        }
        System.out.println("move files to from="+srcTypeDir+" to="+destTypeDir);

        List<String> srcFilesInStorage = new ArrayList<>();
        for (String tmp : pathsInStorage) {
            String srcAbsPath = baseDir + File.separator + srcTypeDir + File.separator + tmp;
            String dstAbsPath = baseDir + File.separator + destTypeDir + File.separator + tmp;
            if (stringUtil.isEmpty(srcAbsPath)) {
                continue;
            }
            moveFile(srcAbsPath, dstAbsPath);
        }

        return srcFilesInStorage;
    }


}
