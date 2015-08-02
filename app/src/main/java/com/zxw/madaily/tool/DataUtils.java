package com.zxw.madaily.tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xzwszl on 7/26/2015.
 */
public class DataUtils {

    public static List<String> filterString(String tag, String attr, String content){

        if (content == null ||  tag == null || attr == null) return null;

        List<String> list = new ArrayList<String>();

        int tagIndex = 0, attrIndex = 0;

        do {

            tagIndex = content.indexOf(tag, tagIndex);

            if (tagIndex < 0) break;

            attrIndex = content.indexOf(attr, tagIndex);

            int start = content.indexOf("\"", attrIndex + 1);

            if (start < 0) break;

            int end = content.indexOf("\"", start+1);

            if (end < 0) break;

            tagIndex = end;

            String item = content.substring(start+1, end);

            list.add(item);

         } while (true);

        return list;
    }

    public static String replaceString(String content, List<String> oldList, List<String> newList) {
        if (oldList == null || newList == null || content == null) return content;

        String rp = new String(content);

        for (int i=0; i<oldList.size(); i++) {

            if (i >= newList.size()) break;

            rp = rp.replace(oldList.get(i), newList.get(i));
        }

        return rp;
    }

    public static String getDate(long time) {

        Date date = new Date(time * 1000);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return format.format(date);
    }
}
