package com.nk.wordcount;

import com.nk.wordcount.constant.WordCountConstant;
import com.nk.wordcount.core.MapReduceTask;
import com.nk.wordcount.core.TopNHeap;
import com.nk.wordcount.core.model.WordCount;
import com.nk.wordcount.util.CollectionUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 单词统计处理类
 *
 * @author Created by niuyang on 2018/5/10.
 */
public class WordCountHandler {

    /**
     * 获取词频topN的单词及对应次数,默认严格要求reduce操作等待异步线程map操作全部执行完
     *
     * @param topN
     * @param fileNameList resource目录下文件名列表
     * @return
     */
    public List<WordCount> doWordCount(int topN, List<String> fileNameList) {
        return doWordCount(topN, fileNameList, true);
    }

    /**
     * 获取词频topN的单词及对应次数
     *
     * @param topN
     * @param fileNameList resource目录下文件名列表
     * @param reduceUntilAllWorkThreadFinish 是否严格要求reduce操作等待异步线程map操作全部执行完
     * @return
     */
    public List<WordCount> doWordCount(int topN, List<String> fileNameList, boolean reduceUntilAllWorkThreadFinish) {
        // 参数校验
        if (topN <= 0 || CollectionUtil.isEmpty(fileNameList) || fileNameList.size() > WordCountConstant.INPUT_FILE_NUM) {
            throw new IllegalArgumentException("input params error");
        }

        // 进行map-reduce计算
        MapReduceTask mapReduceTask = new MapReduceTask(fileNameList, WordCountConstant.WORK_THREAD_NUM,
                reduceUntilAllWorkThreadFinish);

        Map<String, Integer> resultMap = null;
        try {
            resultMap = mapReduceTask.run();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取topN结果
        if (resultMap == null) {
            System.out.println("result is empty");
        }
        TopNHeap topNHeap = new TopNHeap(topN, resultMap);
        return topNHeap.getRankListDesc();
    }

}
