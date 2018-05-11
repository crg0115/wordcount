package com.nk.wordcount.core;

import com.nk.wordcount.constant.WordCountConstant;
import com.nk.wordcount.util.CollectionUtil;
import com.nk.wordcount.util.FileReaderUtil;
import com.nk.wordcount.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Map任务执行者，负责进行文件中单词个数的统计
 *
 * @author Created by niuyang on 2018/5/10.
 */
public class MapReduceTask {

    /**
     * 待处理的文件
     */
    private List<String> fileNameList;

    /**
     * 处理文件创建的线程数
     */
    private int nThread;

    /**
     * 等到所有线程都执行完之后才执行reduce操作
     */
    private boolean reduceUntilAllWorkThreadFinish = false;

    public MapReduceTask(List<String> fileNameList, int nThread) {
        this.fileNameList = fileNameList;
        this.nThread = nThread;
    }

    public MapReduceTask(List<String> fileNameList, int nThread,
                         boolean reduceUntilAllWorkThreadFinish) {
        this(fileNameList, nThread);
        this.reduceUntilAllWorkThreadFinish = reduceUntilAllWorkThreadFinish;
    }

    /**
     * 任务执行
     *
     * @return
     */
    public Map<String, Integer> run() throws ExecutionException, InterruptedException {
        if (nThread <= 0 || CollectionUtil.isEmpty(fileNameList)) {
            return Collections.emptyMap();
        }

        // 进行map计算
        final List<Future<Map<String, Integer>>> mapResultFutureList = map();

        // 获取并合并异步计算的结果
        return reduce(mapResultFutureList);
    }

    /**
     * map计算
     *
     * @return
     */
    private List<Future<Map<String, Integer>>> map() {
        // 获取每页处理的文件数量
        int pageSize = (fileNameList.size() + nThread - 1) / nThread;

        // 根据条件确定是否创建闭锁，以保证严格的所有线程异步任务执行完之后才开始执行reduce操作,不过不建议这么使用
        CountDownLatch countDownLatch = null;
        if (reduceUntilAllWorkThreadFinish) {
            countDownLatch = getCountDownLatchByRealThreadNum(fileNameList.size(), pageSize);
        }

        // 分页创建线程进行任务处理
        final List<Future<Map<String, Integer>>> mapResultFutureList = new ArrayList<>(fileNameList.size());
        final CountDownLatch finalCountDownLatch = countDownLatch;
        CollectionUtil.split(fileNameList, pageSize, new CollectionUtil.PageProcess<String>() {
            @Override
            public void process(List<String> subFileNameList) {
                Future<Map<String, Integer>> mapFutureTask = ThreadPoolUtil.getInstance().submit(new MapCallableTask(subFileNameList, finalCountDownLatch));
                mapResultFutureList.add(mapFutureTask);
            }
        });

        // 如果需要严格保证线程执行完再进行reduce则要await
        try {
            if (finalCountDownLatch != null) {
                finalCountDownLatch.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mapResultFutureList;
    }

    /**
     * 根据异步任务数量创建闭锁
     *
     * @param fileSize
     * @param pageSize
     * @return
     */
    private CountDownLatch getCountDownLatchByRealThreadNum(int fileSize, int pageSize) {
        // 获取实际分页数
        int totalPageNum = (fileSize + pageSize - 1) / pageSize;
        return new CountDownLatch(totalPageNum);
    }

    /**
     * 规约map计算的结果
     *
     * @param mapResultFutureList
     * @return
     */
    private Map<String, Integer> reduce(List<Future<Map<String, Integer>>> mapResultFutureList) throws ExecutionException, InterruptedException {
        if (CollectionUtil.isEmpty(mapResultFutureList)) {
            return Collections.emptyMap();
        }

        // 获取总的单词列表
        Set<String> keySet = new HashSet<>(WordCountConstant.WORD_MAX_CAPACITY);
        List<Map<String, Integer>> wordCountMapList = new ArrayList<>(mapResultFutureList.size());
        for (Future<Map<String, Integer>> future : mapResultFutureList) {
            Map<String, Integer> wordCountMap = future.get();
            if (wordCountMap != null) {
                keySet.addAll(wordCountMap.keySet());
                wordCountMapList.add(wordCountMap);
            }
        }

        // 结果汇总
        Map<String, Integer> resultMap = new HashMap<>(WordCountConstant.WORD_MAX_CAPACITY);
        for (String key : keySet) {
            int countSum = 0;
            for (Map<String, Integer> wordCountMap : wordCountMapList) {
                Integer count = wordCountMap.get(key);
                if (count != null) {
                    countSum += count;
                }
            }
            resultMap.put(key, countSum);
        }

        return resultMap;
    }

    /**
     * Map异步任务
     */
    private class MapCallableTask implements Callable<Map<String, Integer>> {

        /**
         * 待处理的文件名列表
         */
        private List<String> subFileNameList;

        /**
         * 闭锁，保证异步任务在全部执行完后才开始reduce的操作
         */
        private CountDownLatch countDownLatch;

        public MapCallableTask(List<String> subFileNameList, CountDownLatch countDownLatch) {
            this.subFileNameList = subFileNameList;
            this.countDownLatch = countDownLatch;
        }

        /**
         * 循环处理文件
         *
         * @return
         * @throws Exception
         */
        @Override
        public Map<String, Integer> call() throws Exception {
            if (CollectionUtil.isEmpty(subFileNameList)) {
                return Collections.emptyMap();
            }
            Map<String, Integer> map = new HashMap<>(WordCountConstant.WORD_MAX_CAPACITY);
            for (String fileName : subFileNameList) {
                FileReaderUtil.readFile(map, fileName);
            }
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
            return map;
        }
    }

}
