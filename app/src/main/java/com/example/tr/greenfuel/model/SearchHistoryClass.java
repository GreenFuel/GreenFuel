package com.example.tr.greenfuel.model;

/**
 * Created by tangpeng on 2017/2/20.
 */

public class SearchHistoryClass implements Comparable<SearchHistoryClass>{//保存到SQLite的搜索历史
    private String content; //搜索内容
    private long recentTime;//搜索的时间

    public SearchHistoryClass(String content, long recentTime) {
        this.content = content;
        this.recentTime = recentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getRecentTime() {
        return recentTime;
    }

    public void setRecentTime(int recentTime) {
        this.recentTime = recentTime;
    }

    @Override
    public int compareTo(SearchHistoryClass another) {
        long i = this.recentTime - another.getRecentTime();
        return (int)-i;
    }

    @Override
    public String toString() {
        return "时间："+recentTime+",内容："+content;
    }
}
