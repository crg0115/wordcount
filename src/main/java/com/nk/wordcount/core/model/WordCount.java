package com.nk.wordcount.core.model;

/**
 * 单词计算数据对象
 *
 * @author Created by niuyang on 2018/5/10.
 */
public class WordCount {

    /**
     * 单词
     */
    private String word;

    /**
     * 出现次数
     */
    private int count;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WordCount{");
        sb.append("word='").append(word).append('\'');
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
