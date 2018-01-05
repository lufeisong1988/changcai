package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2016/11/7.
 */
public class SubClassified implements Serializable,IKeepFromProguard{

    private String folderId;

    private String name;


    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubClassified that = (SubClassified) o;

        if (folderId != null ? !folderId.equals(that.folderId) : that.folderId != null)
            return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = folderId != null ? folderId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
