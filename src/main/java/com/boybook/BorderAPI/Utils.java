package com.boybook.BorderAPI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String readStringFromFile(InputStream stream) {
        //读取文件
        BufferedReader br = null;
        StringBuffer sb;
        try {
            br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)); //这里可以控制编码
            sb = new StringBuffer();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


}
