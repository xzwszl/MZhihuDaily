package com.zxw.madaily.tool;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by xzwszl on 2015/8/8.
 */
public class FileUtils {

    private static final String base = "/MADaily/cache/file/";

    public static String getResponse(String path) {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        String external = Environment.getExternalStorageDirectory().toString() + base + path;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {

            File file = new File(external);

            if (!file.exists()) {
                return null;
            }

             reader = new BufferedReader(new FileReader(file));

            // file may not exist.
            if (reader == null) return null;
            char[] buff = new char[1024];

            int count = 0;
            while ((count = reader.read(buff)) != -1) {
                sb.append(buff, 0, count);
            }

            return sb.toString();

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void saveResonse(String path, String content) {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        String external_base = Environment.getExternalStorageDirectory().toString() + base;

        BufferedWriter bw = null;

        try {

            File file = new File(external_base);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(external_base + path);

            if (!file.exists()) {
                file.createNewFile();
            }

            bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
