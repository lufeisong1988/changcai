package com.changcai.buyer.ui.message.bean;

import com.changcai.buyer.IKeepFromProguard;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 16/6/25 下午3:48
 */
public class MessageInfo  implements IKeepFromProguard {

//    "content": "单价 200 数量 300 总价6000000",
//            "extras": {
//        "order": "673"
//    },
//            "id": "1",
//            "title": "你有新订单需要签约"

    private String id;
    private String title;
    private String content;
    private ExtrasInfo extras;
    private String Status;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ExtrasInfo getExtras() {
        return extras;
    }

    public void setExtras(ExtrasInfo extras) {
        this.extras = extras;
    }
}
