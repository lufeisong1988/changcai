package com.netease.nim.uikit.common.util;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lufeisong on 2018/1/24.
 */

public class MsgUtil {
    /**
     * 是否过滤掉消息
     *
     * 1.消息ext有msgStatus 未 init
     * 2.过滤掉群消息
     * @param loadedRecent
     * @return
     */
    public static boolean fliteMessage(RecentContact loadedRecent) {
        if(loadedRecent.getSessionType().getValue() != SessionTypeEnum.P2P.getValue()){
            return true;
        }
        List<String> uuid = new ArrayList<>(1);
        uuid.add(loadedRecent.getRecentMessageId());
        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);
        if (messages != null && messages.size() > 0) {
            Map<String, Object> extStr = messages.get(0).getRemoteExtension();
            LogUtil.d("message","extStr = " + (extStr != null ? extStr.toString() : "null") + " id = " +  messages.get(0).getSessionId() + " SessionTypeEnum = " + messages.get(0).getSessionType().getValue() + " MsgTypeEnum = " + messages.get(0).getMsgType().getValue());
            if (extStr != null && extStr.containsKey("msgStatus") && extStr.get("msgStatus").equals("init")) {
                return true;
            }
        }
        return false;
    }
}
