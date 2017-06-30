package com.cooltoo.happy.gene.pdf.template.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhaolisong on 31/03/2017.
 */
public class FileUtil {

    public InputStream getFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            try {
                return new FileInputStream(file);
            }
            catch (IOException ioe) {
                return null;
            }
        }
        return null;
    }
}
