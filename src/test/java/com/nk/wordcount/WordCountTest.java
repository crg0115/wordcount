package com.nk.wordcount;

import com.nk.wordcount.core.model.WordCount;
import com.nk.wordcount.util.ThreadPoolUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * WordCount功能测试
 *
 * @author Created by niuyang on 2018/5/11.
 */
public class WordCountTest {

    @Test
    public void test() {
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add("test1.txt");
        fileNameList.add("test2.txt");
        fileNameList.add("test3.txt");
        fileNameList.add("test4.txt");
        fileNameList.add("test5.txt");

        WordCountHandler wordCountHandler = new WordCountHandler();

        // 测试严格要求reduce操作等待异步线程map操作全部执行完的情况
        {
            long startTime = System.currentTimeMillis();
            List<WordCount> wordCountList = wordCountHandler.doWordCount(5, fileNameList);
            System.out.println("spend time is " + (System.currentTimeMillis() - startTime));
            System.out.println(wordCountList);
            Assert.assertNotNull(wordCountList);
        }

        // 测试不严格要求reduce操作等待异步线程map操作全部执行完的情况
        {
            long startTime = System.currentTimeMillis();
            boolean reduceUntilAllWorkThreadFinish = false;
            List<WordCount> wordCountList = wordCountHandler.doWordCount(5, fileNameList, reduceUntilAllWorkThreadFinish);
            System.out.println("spend time is " + (System.currentTimeMillis() - startTime));
            System.out.println(wordCountList);
            Assert.assertNotNull(wordCountList);
        }

        ThreadPoolUtil.getInstance().shutdown();
    }

}
