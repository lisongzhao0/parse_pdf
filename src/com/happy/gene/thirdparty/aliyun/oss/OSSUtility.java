package com.happy.gene.thirdparty.aliyun.oss;

//import com.aliyun.oss.ClientConfiguration;
//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.OSSException;
//import com.aliyun.oss.model.*;
import com.happy.gene.utility.FileUtil;
import com.happy.gene.utility.SetUtil;
import com.happy.gene.utility.StringUtil;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaolisong on 24/10/2017.
 */
public final class OSSUtility {

    private final SetUtil       setUtil     = SetUtil.newInstance();
    private final FileUtil      fileUtil    = FileUtil.newInstance();
    private final StringUtil    stringUtil  = StringUtil.newInstance();

    private String endpoint         = null;
    private String endpointInner    = null;
    private String accessKeyId      = null;
    private String accessKeySecret  = null;
    private String bucketName       = null;

    public static final OSSUtility newInstance() { return new OSSUtility(); }
    private OSSUtility() {}

    public String getEndpoint() { return endpoint; }
    public String getEndpointInner() { return endpointInner; }
    public String getAccessKeyId() { return accessKeyId; }
    public String getAccessKeySecret() { return accessKeySecret; }
    public String getBucketName() { return bucketName; }

    public OSSUtility setEndpoint(String endpoint) { this.endpoint = endpoint; return this; }
    public OSSUtility setEndpointInner(String endpointInner) { this.endpointInner = endpointInner; return this; }
    public OSSUtility setAccessKeyId(String accessKeyId) { this.accessKeyId = accessKeyId; return this; }
    public OSSUtility setAccessKeySecret(String accessKeySecret) { this.accessKeySecret = accessKeySecret; return this; }
    public OSSUtility setBucketName(String bucketName) { this.bucketName = bucketName; return this; }

//    public OSSClient newOSSClient() {
//        OSSClient ossClient = null;
//
//        ClientConfiguration conf = new ClientConfiguration();
//        conf.setIdleConnectionTime(1000);
//        try { ossClient = new OSSClient(getEndpoint(), getAccessKeyId(), getAccessKeySecret(), conf); } catch (Exception ex) { ex.printStackTrace(); }
//
//        return ossClient;
//    }

    public boolean existObject(String key, Map<String, String> resMsg) {
//        OSSClient ossClient = newOSSClient();
//        if (null==ossClient) {
//            return false;
//        }
//
//
//        StringBuilder error = new StringBuilder();
//        try {
//            return ossClient.doesObjectExist(getBucketName(), key);
//        } catch (OSSException oe) {
//            error.append("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            error.append("Error Message: " + oe.getErrorCode());
//            error.append("Error Code:    " + oe.getErrorCode());
//            error.append("Request ID:    " + oe.getRequestId());
//            error.append("Host ID:       " + oe.getHostId());
//            resMsg.put("reason", error.toString());
//        } catch (ClientException ce) {
//            error.append("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            error.append("Error Message: " + ce.getMessage());
//            resMsg.put("reason", error.toString());
//        } finally {
//            ossClient.shutdown();
//        }
        return false;
    }

//    public OSSObject getObject(String key, Map<String, String> resMsg) {
//        OSSClient ossClient = newOSSClient();
//        if (null==ossClient) {
//            return null;
//        }
//
//        StringBuilder error = new StringBuilder();
//        OSSObject obj = null;
//        try {
//            obj = ossClient.getObject(getBucketName(), key);
//        } catch (OSSException oe) {
//            error.append("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            error.append("Error Message: " + oe.getErrorCode());
//            error.append("Error Code:    " + oe.getErrorCode());
//            error.append("Request ID:    " + oe.getRequestId());
//            error.append("Host ID:       " + oe.getHostId());
//            resMsg.put("reason", error.toString());
//        } catch (ClientException ce) {
//            error.append("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            error.append("Error Message: " + ce.getMessage());
//            resMsg.put("reason", error.toString());
//        } finally {
//            ossClient.shutdown();
//        }
//        return obj;
//    }

    public List<String> listObject(String prefix, String delimiter, int maxKeys) {
        List<String> res = new ArrayList<>();
//        OSSClient ossClient = newOSSClient();
//        if (null==ossClient) {
//            return res;
//        }
//
//        ListObjectsRequest lor = new ListObjectsRequest();
//        lor.setBucketName(getBucketName());
//        lor.setMaxKeys(maxKeys);
//        lor.setDelimiter(delimiter);
//        lor.setPrefix(prefix);
//        try {
//            ObjectListing list = ossClient.listObjects(lor);
//            List<OSSObjectSummary> listObj = list.getObjectSummaries();
//            if (!setUtil.isListEmpty(listObj)) {
//                for (OSSObjectSummary obj : listObj) {
//                    res.add(obj.getKey());
//                }
//            }
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        finally {
//            ossClient.shutdown();
//        }

        return res;
    }

    public String putObject(String dir, String filePath, Map<String, String> resMsg) {
        if (null==dir || dir.trim().isEmpty()) {
            resMsg.put("reason", "dir is empty"); return null;
        }
        if (null==filePath || filePath.trim().isEmpty()) {
            resMsg.put("reason", "filePath is empty"); return null;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            resMsg.put("reason", "src file can not read"); return null;
        }

        dir = dir.replace('\\', '/');
        if (!dir.endsWith("/")) { dir = dir + "/";}

        String suffix       = null;
        String sha1         = null;
        long   milliTime    = System.currentTimeMillis();
        long   nanoTime     = System.nanoTime();
        String strNanoTime  = file.getName()+"_"+milliTime+"_"+nanoTime;
        String fileName     = file.getName();
        int    lastDotIdx   = fileName.lastIndexOf(".");
        if (lastDotIdx>0) { suffix = fileName.substring(lastDotIdx+1); }

        try {sha1           = stringUtil.sha1(strNanoTime);}
        catch (Exception ex) {
            resMsg.put("reason", "get sha1 failed"); return null;
        }

        String newDirectory = sha1.substring(0, 2);
        String newFileName  = sha1.substring(2);

//        OSSClient ossClient = newOSSClient();
//        if (null==ossClient) {
//            resMsg.put("reason", "new oss client failed"); return null;
//        }

        StringBuilder error = new StringBuilder();
        String key = null;
//        try {
//            key = dir + newDirectory + "/" + newFileName + "." + suffix;
//
//            try { ossClient.getObject(bucketName, dir); }
//            catch (Exception ex) {
//                ossClient.putObject(bucketName, dir, new ByteArrayInputStream(new byte[0]));
//            }
//
//            try { ossClient.getObject(bucketName, dir + newDirectory + "/"); }
//            catch (Exception ex) {
//                ossClient.putObject(bucketName, dir + newDirectory + "/", new ByteArrayInputStream(new byte[0]));
//            }
//
//            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
//            // 待上传的本地文件
//            uploadFileRequest.setUploadFile(filePath);
//            // 设置并发下载数，默认1
//            uploadFileRequest.setTaskNum(5);
//            // 设置分片大小，默认100KB
//            uploadFileRequest.setPartSize(1024 * 1024 * 1);
//            // 开启断点续传，默认关闭
//            uploadFileRequest.setEnableCheckpoint(true);
//
//            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);
//
//            CompleteMultipartUploadResult multipartUploadResult = uploadResult.getMultipartUploadResult();
//            System.out.println("put file ETag="+multipartUploadResult.getETag());
//
//        } catch (OSSException oe) {
//            error.append("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            error.append("Error Message: " + oe.getErrorCode());
//            error.append("Error Code:    " + oe.getErrorCode());
//            error.append("Request ID:    " + oe.getRequestId());
//            error.append("Host ID:       " + oe.getHostId());
//            resMsg.put("reason", error.toString());
//            key = null;
//        } catch (ClientException ce) {
//            error.append("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            error.append("Error Message: " + ce.getMessage());
//            resMsg.put("reason", error.toString());
//            key = null;
//        } catch (Throwable e) {
//            e.printStackTrace();
//            resMsg.put("reason", e.getMessage());
//            key = null;
//        } finally {
//            ossClient.shutdown();
//        }
        return key;
    }

    public String putObject(String dir, String subDir, String filePath, Map<String, String> resMsg) {
        if (null==dir || dir.trim().isEmpty()) {
            resMsg.put("reason", "dir is empty"); return null;
        }
        if (null==subDir || subDir.trim().isEmpty()) {
            resMsg.put("reason", "subDir is empty"); return null;
        }
        if (null==filePath || filePath.trim().isEmpty()) {
            resMsg.put("reason", "filePath is empty"); return null;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            resMsg.put("reason", "src file can not read"); return null;
        }

        dir = dir.replace('\\', '/');
        if (!dir.endsWith("/")) { dir = dir + "/";}

        String suffix       = null;
        String fileName     = file.getName();
        int    lastDotIdx   = fileName.lastIndexOf(".");
        if (lastDotIdx>0) { suffix = fileName.substring(lastDotIdx+1); }
        if (lastDotIdx>0) { fileName = fileName.substring(0, lastDotIdx); }

        String sha1         = null;
        long   milliTime    = System.currentTimeMillis();
        long   nanoTime     = System.nanoTime();
        String strNanoTime  = file.getName()+"_"+milliTime+"_"+nanoTime;
        try {sha1           = stringUtil.sha1(strNanoTime);}
        catch (Exception ex) {
            resMsg.put("reason", "get sha1 failed"); return null;
        }

        fileName += "_"+sha1;

//        OSSClient ossClient = newOSSClient();
//        if (null==ossClient) {
//            resMsg.put("reason", "new oss client failed"); return null;
//        }

        StringBuilder error = new StringBuilder();
        String key = null;
//        try {
//            key = dir + subDir + "/" + fileName + "." + suffix;
//
//            try { ossClient.getObject(bucketName, dir); }
//            catch (Exception ex) {
//                ossClient.putObject(bucketName, dir, new ByteArrayInputStream(new byte[0]));
//            }
//
//            try { ossClient.getObject(bucketName, dir + subDir + "/"); }
//            catch (Exception ex) {
//                ossClient.putObject(bucketName, dir + subDir + "/", new ByteArrayInputStream(new byte[0]));
//            }
//
//            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
//            // 待上传的本地文件
//            uploadFileRequest.setUploadFile(filePath);
//            // 设置并发下载数，默认1
//            uploadFileRequest.setTaskNum(5);
//            // 设置分片大小，默认100KB
//            uploadFileRequest.setPartSize(1024 * 1024 * 1);
//            // 开启断点续传，默认关闭
//            uploadFileRequest.setEnableCheckpoint(true);
//
//            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);
//
//            CompleteMultipartUploadResult multipartUploadResult = uploadResult.getMultipartUploadResult();
//            System.out.println("put file ETag="+multipartUploadResult.getETag());
//
//        } catch (OSSException oe) {
//            error.append("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            error.append("Error Message: " + oe.getErrorCode());
//            error.append("Error Code:    " + oe.getErrorCode());
//            error.append("Request ID:    " + oe.getRequestId());
//            error.append("Host ID:       " + oe.getHostId());
//            resMsg.put("reason", error.toString());
//            key = null;
//        } catch (ClientException ce) {
//            error.append("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            error.append("Error Message: " + ce.getMessage());
//            resMsg.put("reason", error.toString());
//            key = null;
//        } catch (Throwable e) {
//            e.printStackTrace();
//            resMsg.put("reason", e.getMessage());
//            key = null;
//        } finally {
//            ossClient.shutdown();
//        }
        return key;
    }

    /**
     * 注意重名的会删掉已有的，重新上传新的
     * @param dir         bucket里文件夹全路径，前后不要带 '/'（如：images/aaaa/bbbb）
     * @param fileName    bucket里的文件名，不要带后缀（如：name111）
     * @param fileSuffix  bucket里的文件后缀（如：pdf）
     * @param filePath    本地要上传的文件（如：/home/abcd/bbbbb.pdf）
     * @param resMsg      错误成功，返回信息；
     *
     * @return bucket里文件对象的 Key（如：images/aaaa/bbbb/name111.pdf）
     */
    public String putObjectByName(String dir, String fileName, String fileSuffix, String filePath, Map<String, String> resMsg) {
        if (null==filePath || filePath.trim().isEmpty()) {
            resMsg.put("reason", "filePath is empty"); return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            resMsg.put("reason", "file not exist"); return null;
        }
        if (file.isDirectory()) {
            resMsg.put("reason", filePath + " not a file, is directory"); return null;
        }

        String[] dirs = stringUtil.isEmpty(dir) ? null : dir.split("/");

//        OSSClient ossClient = newOSSClient();
//        if (null==ossClient) {
//            resMsg.put("reason", "new oss client failed"); return null;
//        }

        StringBuilder error     = new StringBuilder();
        StringBuilder dirKey    = new StringBuilder();
        String        key       = null;
//        try {
//            // created directory
//            for (int i = 0, count= null==dirs ? 0 : dirs.length; i < count; i ++) {
//                String d = dirs[i];
//                if (stringUtil.isEmpty(d)) { d = "unknown"; }
//
//                dirKey.append(d).append("/");
//
//                try { ossClient.getObject(bucketName, dirKey.toString()); }
//                catch (Exception ex) {
//                    ossClient.putObject(bucketName, dirKey.toString(), new ByteArrayInputStream(new byte[0]));
//                }
//            }
//
//            key = dirKey.toString() + fileName + "." + fileSuffix;
//            try {
//                OSSObject exist = ossClient.getObject(bucketName, key);
//                if (null!=exist) { ossClient.deleteObject(exist.getBucketName(), exist.getKey()); }
//            }
//            catch (Exception ex) {}
//
//            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
//            // 待上传的本地文件
//            uploadFileRequest.setUploadFile(filePath);
//            // 设置并发下载数，默认1
//            uploadFileRequest.setTaskNum(5);
//            // 设置分片大小，默认100KB
//            uploadFileRequest.setPartSize(1024 * 1024 * 1);
//            // 开启断点续传，默认关闭
//            uploadFileRequest.setEnableCheckpoint(true);
//
//            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);
//
//            CompleteMultipartUploadResult multipartUploadResult = uploadResult.getMultipartUploadResult();
//            System.out.println("put file ETag="+multipartUploadResult.getETag());
//            resMsg.put("status", "OK");
//
//        } catch (OSSException oe) {
//            error.append("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            error.append("Error Message: " + oe.getErrorCode());
//            error.append("Error Code:    " + oe.getErrorCode());
//            error.append("Request ID:    " + oe.getRequestId());
//            error.append("Host ID:       " + oe.getHostId());
//            resMsg.put("reason", error.toString());
//            key = null;
//        } catch (ClientException ce) {
//            error.append("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            error.append("Error Message: " + ce.getMessage());
//            resMsg.put("reason", error.toString());
//            key = null;
//        } catch (Throwable e) {
//            e.printStackTrace();
//            resMsg.put("reason", e.getMessage());
//            key = null;
//        } finally {
//            ossClient.shutdown();
//        }
        return key;
    }

    /**
     * @param fileName    bucket里的文件名，不要带后缀（如：name111）
     * @param filePath    本地要上传的文件（如：/home/abcd/bbbbb.pdf）
     * @param resMsg      错误成功，返回信息；
     *
     * @return bucket里文件对象的 Key（如：images/aaaa/bbbb/name111.pdf）
     */
    public String putObjectByPath(String fileName, String filePath, Map<String, String> resMsg) {
        if (null==filePath || filePath.trim().isEmpty()) {
            resMsg.put("reason", "filePath is empty");
            resMsg.put("status", "error");
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            resMsg.put("reason", "file not exist");
            resMsg.put("status", "error");
            return null;
        }
        if (file.isDirectory()) {
            resMsg.put("reason", filePath + " not a file, is directory");
            resMsg.put("status", "error");
            return null;
        }

//        OSSClient ossClient = newOSSClient();
//        if (null==ossClient) {
//            resMsg.put("reason", "new oss client failed");
//            resMsg.put("status", "error");
//            return null;
//        }
//
//        StringBuilder error     = new StringBuilder();
//        try {
//            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, fileName);
//            // 待上传的本地文件
//            uploadFileRequest.setUploadFile(filePath);
//            // 设置并发下载数，默认1
//            uploadFileRequest.setTaskNum(5);
//            // 设置分片大小，默认100KB
//            uploadFileRequest.setPartSize(1024 * 1024 * 1);
//            // 开启断点续传，默认关闭
//            uploadFileRequest.setEnableCheckpoint(true);
//
//            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);
//
//            CompleteMultipartUploadResult multipartUploadResult = uploadResult.getMultipartUploadResult();
//            System.out.println("put file ETag="+multipartUploadResult.getETag());
//            resMsg.put("status", "OK");
//
//        } catch (OSSException oe) {
//            error.append("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            error.append("Error Message: " + oe.getErrorCode());
//            error.append("Error Code:    " + oe.getErrorCode());
//            error.append("Request ID:    " + oe.getRequestId());
//            error.append("Host ID:       " + oe.getHostId());
//            resMsg.put("reason", error.toString());
//            resMsg.put("status", "error");
//            fileName = null;
//        } catch (ClientException ce) {
//            error.append("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            error.append("Error Message: " + ce.getMessage());
//            resMsg.put("reason", error.toString());
//            resMsg.put("status", "error");
//            fileName = null;
//        } catch (Throwable e) {
//            e.printStackTrace();
//            resMsg.put("reason", e.getMessage());
//            resMsg.put("status", "error");
//            fileName = null;
//        } finally {
//            ossClient.shutdown();
//        }
        return fileName;
    }

    public String deleteObject(String key, Map<String, String> resMsg) {
        boolean failed = true;
//        OSSClient ossClient = newOSSClient();
//        if (null==ossClient) {
//            return null;
//        }
//
//        StringBuilder error = new StringBuilder();
//        try {
//            ossClient.deleteObject(getBucketName(), key);
//            failed = false;
//        } catch (ClientException ce) {
//            error.append("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            error.append("Error Message: " + ce.getMessage());
//            resMsg.put("reason", error.toString());
//        } catch (Throwable e) {
//            e.printStackTrace();
//            resMsg.put("reason", e.getMessage());
//        } finally {
//            ossClient.shutdown();
//        }
        if (failed) { return null; }
        else { return "OK"; }
    }

    public String computeSignature(String policy) {
        try {
            //String policy = "{\"Expires\": \"2120-01-01T12:00:00.000Z\",\"conditions\": [[\"content-length-range\", 0, 104857600]]}";
            String encodePolicy = new String(Base64.encodeBase64(policy.getBytes()));

            // convert to UTF-8
            byte[] key = accessKeySecret.getBytes("UTF-8");
            byte[] data = encodePolicy.getBytes("UTF-8");

            // hmac-sha1
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] sha = mac.doFinal(data);

            // base64
            return new String(Base64.encodeBase64(sha));
        }
        catch (Exception ex) {
            String exception = ex.getMessage();
            return exception;
        }

    }

    public String getSignatureUrl(String key){
        URL url = null;
        // Generate a presigned URL
//        Date expires = new Date (new Date().getTime() + 3 * 60 *1000 * 60); // 3 hours minute to expire
//        GeneratePresignedUrlRequest generatePresignedUrlRequest =
//
//                new GeneratePresignedUrlRequest(bucketName, key);
//
//        generatePresignedUrlRequest.setExpiration(expires);
//        OSSClient ossClient = newOSSClient();
//        if (null == ossClient) {
//            return null;
//        }
//        url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

}
