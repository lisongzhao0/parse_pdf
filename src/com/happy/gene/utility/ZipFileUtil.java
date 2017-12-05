package com.happy.gene.utility;

//import org.apache.commons.compress.archivers.ArchiveEntry;
//import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
//import org.apache.commons.compress.archivers.sevenz.SevenZFile;
//import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
//import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
//import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
//import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
//import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
//import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.*;


/**
 * Created by zhaolisong on 07/06/2017.
 */
public class ZipFileUtil {

    private FileUtil fileUtil = FileUtil.newInstance();

    public static ZipFileUtil newInstance() {
        return new ZipFileUtil();
    }

    public boolean compress7Z(String srcDir , String[] srcFiles , String destFile)  {
        if (!fileUtil.directoryExist(srcDir)) {
            return false;
        }
        if (fileUtil.fileExist(destFile)) {
            return false;
        }

//        SevenZOutputFile archive = null;
//        try {
//            archive = new SevenZOutputFile(new File(destFile));
//
//            for( String fileName : srcFiles ) {
//                File file = new File(srcDir +"/" + fileName);
//
//                if(file.isDirectory()) {
//                    SevenZArchiveEntry entry = archive.createArchiveEntry(file, fileName);
//                    archive.putArchiveEntry( entry );
//                    entry.setDirectory(true);
//                    archive.closeArchiveEntry();
//                    continue;
//                }
//
//                SevenZArchiveEntry entry = archive.createArchiveEntry(file, fileName);
//                archive.putArchiveEntry( entry );
//
//                FileInputStream     fis = new FileInputStream( srcDir + "/" + fileName );
//                BufferedInputStream bis = new BufferedInputStream( fis );
//
//                int     size    = 0;
//                byte[]  buf     = new byte[ 1024*512 ];
//                while((size = bis.read(buf)) > 0) {
//                    archive.write( buf , 0 , size );
//                }
//
//                bis.close();
//                fis.close();
//
//                archive.closeArchiveEntry();
//            }
//
//            archive.finish();
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//            return false;
//        }
//        finally {
//            if (null!=archive) {
//                try { archive.close(); } catch (IOException ioe) {}
//            }
//        }

        return true;
    }

    public boolean decompress7Z(String src , String destDir) {
        if (!fileUtil.fileExist(src)) {
            return false;
        }
        if (!fileUtil.directoryExist(destDir)) {
            return false;
        }

//        SevenZFile archive = null;
//        try { archive = new SevenZFile(new File( src )); } catch (IOException ex) { return false; }
//
//        try { writeToDest(archive, destDir); }
//        catch (IOException ex) { return false; }
//        finally {
//            if (null!=archive) {
//                try { archive.close(); } catch (IOException ioe) {}
//            }
//        }

        return true;
    }

    public boolean compressTarGz(String srcDir, String[] srcFiles, String destFile)  {
        boolean res = true;
        if (!fileUtil.directoryExist(srcDir)) {
            return false;
        }
        if (fileUtil.fileExist(destFile)) {
            return false;
        }

        // 压 tar
        String outputTarFile    = destFile.replace( ".tar.gz" , ".tar" );

//        FileOutputStream       fos1  = null;
//        TarArchiveOutputStream taos = null;
//        try {
//            fos1 = new FileOutputStream( outputTarFile );
//            taos = new TarArchiveOutputStream( fos1 );
//
//            for( String fileName : srcFiles )
//            {
//                if( fileName.endsWith("/") )
//                {
//                    File             file    = new File( srcDir + "/" + fileName );
//                    TarArchiveEntry  entry   = new TarArchiveEntry( file , fileName );
//                    taos.putArchiveEntry( entry );
//                    taos.closeArchiveEntry();
//                    continue;
//                }
//
//                File            file   = new File( srcDir + "/" + fileName );
//                TarArchiveEntry entry  = new TarArchiveEntry( file , fileName );
//                taos.setLongFileMode( TarArchiveOutputStream.LONGFILE_POSIX );
//                taos.putArchiveEntry( entry );
//
//                FileInputStream         fis     = null;
//                BufferedInputStream     bis     = null;
//                try {
//                    fis     = new FileInputStream( srcDir + "/" + fileName );
//                    bis     = new BufferedInputStream( fis );
//
//                    int     size    = 0;
//                    byte[]  buf     = new byte[ 1024 ];
//                    while( ( size = bis.read( buf ) ) > 0 )
//                    {
//                        taos.write( buf , 0 , size );
//                    }
//                }
//                catch (IOException ex) {
//                    res = false;
//                }
//                finally {
//                    if (null!=fis) { try {fis.close();} catch (Exception ex){} }
//                    if (null!=bis) { try {bis.close();} catch (Exception ex){} }
//                }
//
//                taos.closeArchiveEntry();
//            }
//        }
//        catch (IOException ex) {
//            res = false;
//        }
//        finally {
//            if (null!=fos1) { try {fos1.close();} catch (Exception ex){} }
//            if (null!=taos) { try {taos.close();} catch (Exception ex){} }
//        }
//
//        // gzip圧縮
//        FileInputStream            fis     = null;
//        BufferedInputStream        bis     = null;
//        FileOutputStream           fos     = null;
//        GzipCompressorOutputStream archive = null;
//        try {
//            fis     = new FileInputStream( outputTarFile );
//            bis     = new BufferedInputStream( fis );
//            fos     = new FileOutputStream( destFile );
//            archive = new GzipCompressorOutputStream(fos);
//
//            int     size    = 0;
//            byte[]  buf     = new byte[ 1024 ];
//            while( ( size = bis.read( buf ) ) > 0 )
//            {
//                archive.write( buf , 0 , size );
//            }
//            archive.flush();
//            archive.close();
//        }
//        catch (IOException ex) {
//            res = false;
//        }
//        finally {
//            if (null!=fis) { try {fis.close();} catch (Exception ex){} }
//            if (null!=bis) { try {bis.close();} catch (Exception ex){} }
//            if (null!=fos) { try {fos.close();} catch (Exception ex){} }
//            if (null!=archive) { try {archive.close();} catch (Exception ex){} }
//        }
//
//        fileUtil.deleteFile(outputTarFile);
//        if (!res) {
//            fileUtil.deleteFile(destFile);
//        }
        return res;
    }

    public boolean decompressTarGz(String src, String destDir) {
        boolean res = true;
        if (!fileUtil.fileExist(src)) {
            return false;
        }
        if (!fileUtil.directoryExist(destDir)) {
            return false;
        }


        String outputTarFile = src.substring(src.lastIndexOf("/" )).replace(".tar.gz", ".tar.tmp");
        String outputTarDir  = (destDir.endsWith("/")) ? destDir.substring(0 , destDir.lastIndexOf("/")) : destDir;
        outputTarDir         = outputTarDir.substring( 0 , outputTarDir.lastIndexOf( "/" ) );
        String outputTarPath = outputTarDir + "/" + outputTarFile;


        FileInputStream            fis1    = null;
        FileOutputStream           fos1    = null;
//        GzipCompressorInputStream  archive = null;
//
//        try { fis1 = new FileInputStream( src ); } catch (IOException ex) { return false; }
//        try { fos1 = new FileOutputStream( outputTarPath ); } catch (IOException ex) { return false; }
//        try { archive = new GzipCompressorInputStream( fis1 ); } catch (IOException ex) { return false; }
//
//        // gzip 解凍
//        try {
//            int     size    = 0;
//            byte[]  buf     = new byte[ 1024*512 ];
//            while((size = archive.read(buf)) > 0) {
//                fos1.write( buf , 0 , size );
//            }
//            fos1.flush();
//        }
//        catch (IOException ex) {
//            res = false;
//        }
//        finally {
//            if (null!=fis1) { try { fis1.close(); } catch (IOException ioe) {} }
//            if (null!=fos1) { try { fos1.close(); } catch (IOException ioe) {} }
//            if (null!=archive) { try { archive.close(); } catch (IOException ioe) {} }
//        }
//
//        // tar 解除
//        FileInputStream         fis2 = null;
//        TarArchiveInputStream   tais = null;
//
//        try {
//            fis2 = new FileInputStream( outputTarPath );
//            tais = new TarArchiveInputStream( fis2 );
//
//            writeToDest(tais, destDir);
//        }
//        catch (IOException ex) {
//            res = false;
//        }
//        finally {
//            if (null!=fis2) { try { fis2.close(); } catch (IOException ioe) {} }
//            if (null!=tais) { try { tais.close(); } catch (IOException ioe) {} }
//        }
//
//        fileUtil.deleteFile(outputTarPath);
//        if (!res) {
//            fileUtil.deleteDirectory(destDir);
//        }
        return res;
    }

    private void writeToDest(Object archive, String destDir) throws IOException {
//        ArchiveEntry entry = null;
//        if (archive instanceof SevenZFile) {
//            entry = ((SevenZFile) archive).getNextEntry();
//        }
//        if (archive instanceof TarArchiveInputStream) {
//            entry = ((TarArchiveInputStream) archive).getNextEntry();
//        }
//
//        while(entry != null) {
//            File file = new File(destDir + "/" + entry.getName());
//
//            if(entry.isDirectory()) {
//                file.mkdirs();
//            }
//            else {
//                if (!file.getParentFile().exists()) {
//                    file.getParentFile().mkdirs();
//                }
//
//                FileOutputStream fos = new FileOutputStream(file);
//                BufferedOutputStream bos = new BufferedOutputStream(fos);
//
//                int size = 0;
//                byte[] buf = new byte[1024 * 512];
//                while ((size = readBytes(archive, buf)) > 0) {
//                    bos.write(buf, 0, size);
//                }
//                bos.flush();
//                bos.close();
//                fos.close();
//            }
//
//            if (archive instanceof SevenZFile) {
//                entry = ((SevenZFile) archive).getNextEntry();
//            }
//            else if (archive instanceof TarArchiveInputStream) {
//                entry = ((TarArchiveInputStream) archive).getNextEntry();
//            }
//            else {
//                entry = null;
//            }
//        }
    }

    private int readBytes(Object archive, byte[] content) throws IOException {
//        if (archive instanceof SevenZFile) {
//            return ((SevenZFile) archive).read(content);
//        }
//        if (archive instanceof TarArchiveInputStream) {
//            return ((TarArchiveInputStream) archive).read(content);
//        }
        return -1;
    }

}
