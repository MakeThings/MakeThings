package com.makethings.communication.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;

public class FileHelper {

    public static String readFromFilename(String classPathFileName) {
        InputStream resourceAsStream = FileHelper.class.getResourceAsStream(classPathFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
        String line = null;
        String content = "";
        try {
            while ((line = br.readLine()) != null) {
                content += line;
            }
        }
        catch (IOException e) {
            Assert.fail("Cannot read message from file, name: " + classPathFileName + ", error: " + e.getMessage());
        }
        return content;
    }
}
