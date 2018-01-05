package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * 资讯分类栏目属性
 * */
public class NewsClassify  implements Serializable, IKeepFromProguard {
    /** id */
    public String folderId;
    /** 名称 */
    public String name;

    public List<SubClassified> subFolders;

    public List<SubClassified> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<SubClassified> subFolders) {
        this.subFolders = subFolders;
    }

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

        NewsClassify that = (NewsClassify) o;

        if (folderId != null ? !folderId.equals(that.folderId) : that.folderId != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return subFolders != null ? subFolders.equals(that.subFolders) : that.subFolders == null;

    }

    @Override
    public int hashCode() {
        int result = folderId != null ? folderId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (subFolders != null ? subFolders.hashCode() : 0);
        return result;
    }
}
