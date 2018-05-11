package com.nk.wordcount;

import com.nk.wordcount.core.TopNHeap;
import com.nk.wordcount.core.model.WordCount;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * topN堆用例 验证筛选topN的功能
 *
 * @author Created by niuyang on 2018/5/11.
 */
public class TopNHeadTest {

    @Test
    public void getRankListDescTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 10);
        map.put("3", 5);
        map.put("4", 1100);
        map.put("5", 89);
        map.put("6", 800);
        map.put("7", 800);
        map.put("8", 50);
        map.put("9", 30);
        map.put("10", 300);

        TopNHeap topNHeap = new TopNHeap(5, map);
        List<WordCount> rankListDesc = topNHeap.getRankListDesc();
        System.out.println(rankListDesc);
        Assert.assertNotNull(rankListDesc);
    }

}
