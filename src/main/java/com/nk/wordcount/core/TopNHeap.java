package com.nk.wordcount.core;

import com.nk.wordcount.core.model.WordCount;
import com.nk.wordcount.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * topN堆 线程不安全
 *
 * @author Created by niuyang on 2018/5/10.
 */
public class TopNHeap {

    /**
     * 保存堆数据
     */
    private Map.Entry<String, Integer>[] tables;

    /**
     * 堆大小
     */
    private int topN;

    public TopNHeap(int topN) {
        if (topN <= 0) {
            throw new IllegalArgumentException("input param error");
        }
        this.topN = topN;
        this.tables = new Map.Entry[this.topN];
    }

    public TopNHeap(int topN, Map<String, Integer> map) {
        this(topN);
        if (CollectionUtil.isEmpty(map)) {
            return;
        }
        addAll(map);
    }

    /**
     * 返回按降序排列的对象
     *
     * @return
     */
    public List<WordCount> getRankListDesc() {
        if (tables == null || tables.length == 0) {
            return Collections.emptyList();
        }

        List<WordCount> wordCountList = new ArrayList<>(tables.length);
        for (Map.Entry<String, Integer> entry : tables) {
            if (entry != null && entry.getKey() != null) {
                WordCount wordCount = new WordCount();
                wordCount.setWord(entry.getKey());
                if (entry.getValue() == null) {
                    wordCount.setCount(0);
                } else {
                    wordCount.setCount(entry.getValue());
                }
                wordCountList.add(wordCount);
            }
        }

        // 降序排列
        Collections.sort(wordCountList, new Comparator<WordCount>() {
            @Override
            public int compare(WordCount o1, WordCount o2) {
                return o2.getCount() - o1.getCount();
            }
        });

        return wordCountList;
    }

    /**
     * 批量添加元素
     *
     * @param map
     */
    private void addAll(Map<String, Integer> map) {
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry e = iterator.next();
            if (i <= topN - 1) {
                this.tables[i] = e;
                if (i == topN - 1) {
                    buildHeap();
                }
                i++;
            } else {
                add(e);
            }
        }
    }

    /**
     * 单个添加元素
     *
     * @param e
     */
    private void add(Map.Entry<String, Integer> e) {
        if (e.getValue() > tables[0].getValue()) {
            tables[0] = e;
            buildHeap(0, tables.length);
        }
    }

    /**
     * 构造堆
     */
    private void buildHeap() {
        for (int i = tables.length / 2 - 1; i >= 0; i--) {
            buildHeap(i, tables.length);
        }
    }

    /**
     * 构造堆
     *
     * @param i
     * @param length
     */
    private void buildHeap(int i, int length) {
        int min = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left <= length - 1 && compare(tables[left].getValue(), tables[i].getValue()) < 0) {
            min = left;
        }
        if (right <= length - 1 && compare(tables[right].getValue(), tables[min].getValue()) < 0) {
            min = right;
        }

        if (min != i) {
            Map.Entry<String, Integer> temp = tables[i];
            tables[i] = tables[min];
            tables[min] = temp;
            buildHeap(min, length);
        }
    }

    /**
     * 比较整型包装类大小
     *
     * @param a
     * @param b
     * @return
     */
    private int compare(Integer a, Integer b) {
        if (a == null) {
            a = 0;
        }
        if (b == null) {
            b = 0;
        }
        return a - b;
    }

}
