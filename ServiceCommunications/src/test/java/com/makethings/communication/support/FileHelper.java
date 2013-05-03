package com.makethings.communication.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHelper {
    
    public static String readFromFilename(String classPathFileName) throws IOException {
        InputStream resourceAsStream = FileHelper.class.getResourceAsStream(classPathFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
        String line = null;
        String content = "";
        while ((line = br.readLine()) != null) {
            content += line;
        }
        return content;
    }
}
