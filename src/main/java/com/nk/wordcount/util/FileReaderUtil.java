package com.nk.wordcount.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

/**
 * 文件读取工具类 线程不安全
 *
 * @author Created by niuyang on 2018/5/10.
 */
public class FileReaderUtil {

    /**
     * 读文件,返回文件单词的计数
     *
     * @param filePath 文件路径
     * @return
     */
    public static void readFile(Map<String, Integer> map, String filePath) {
        if (map == null || StringUtil.isBlank(filePath)) {
            return;
        }

        ClassLoader classLoader = FileReaderUtil.class.getClassLoader();
        URL url = classLoader.getResource(filePath);

        FileReader fileReader = null;
        Scanner scanner = null;
        try {
            fileReader = new FileReader(url.getFile());
            scanner = new Scanner(fileReader);
            while (scanner.hasNext()) {
                String s = scanner.next();
                // 单线程场景可以这样计数
                Integer count = map.get(s);
                if (count == null) {
                    count = 1;
                } else {
                    count++;
                }
                map.put(s, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (scanner != null) {
                    scanner.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
