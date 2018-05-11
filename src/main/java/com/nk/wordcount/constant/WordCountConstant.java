package com.nk.wordcount.constant;

/**
 * 单词统计常量类
 *
 * @author Created by niuyang on 2018/5/10.
 */
public class WordCountConstant {

    /**
     * 工作线程的数量 5
     */
    public static final int WORK_THREAD_NUM = 5;

    /**
     * 文件数量 100
     */
    public static final int INPUT_FILE_NUM = 100;

    /**
     * 单词一万左右，设定为12000个，容器初始化时用此容量，避免resize造成的性能开销
     */
    public static final int WORD_MAX_CAPACITY = 12000;

    /**
     * 统计结果数量 前100
     */
    public static final int RESULT_TOP_N = 100;
}
