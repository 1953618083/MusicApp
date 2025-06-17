package com.music.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {
    public static String generateFilePath(String fileName) {
        // 根据日期生成路径——2022/09/06/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = sdf.format(new Date());
        // uuid作为文件名，并替换掉其中的 “-”
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 后缀名和文件后缀一样
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg
        // test.png -> .png
        String fileType = fileName.substring(index);
        // 拼接cos中的文件路径
        String filePath = new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
        return filePath;
    }
}