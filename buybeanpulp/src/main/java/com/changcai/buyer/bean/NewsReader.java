package com.changcai.buyer.bean;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by liuxingwei on 2017/2/27.
 */
public class NewsReader {

    private volatile static NewsReader instance = null;

    private HashMap<Integer, String> newsArticleIds;

    // 获得对象实例的方法
    public static NewsReader getSingleton() {
        if (instance == null) {
            synchronized (NewsReader.class) {
                if (instance == null)
                    instance = new NewsReader();
            }
        }
        return instance;
    }

    private NewsReader() {
        newsArticleIds = new HashMap<>();
    }


    public boolean isReadNews(@NonNull String newsArticleId) {
        return newsArticleIds.containsValue(newsArticleId);
    }

    public void addAlreadyNewsId(@NonNull int position, @NonNull String newsArticleId) {
        if (!newsArticleIds.containsValue(newsArticleId)) {
            newsArticleIds.put(position, newsArticleId);
        }
    }
}
