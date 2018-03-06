package com.changcai.buyer.im.main.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.changcai.buyer.CompatTouchBackUIActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.im.main.reminder.ReminderManager;
import com.changcai.buyer.im.session.SessionHelper;
import com.changcai.buyer.im.session.extension.GuessAttachment;
import com.changcai.buyer.im.session.extension.RTSAttachment;
import com.changcai.buyer.im.session.extension.RedPacketAttachment;
import com.changcai.buyer.im.session.extension.RedPacketOpenedAttachment;
import com.changcai.buyer.im.session.extension.SnapChatAttachment;
import com.changcai.buyer.im.session.extension.StickerAttachment;
import com.changcai.buyer.util.LogUtil;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lufeisong on 2017/12/20.
 */

public class NotifactionSessionListActivity extends CompatTouchBackUIActivity {
    private RecentContactsFragment fragment;
    private ImageView rightImage;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void injectFragmentView() {
        rightImage = (ImageView) findViewById(R.id.iv_btn_right);
        rightImage.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lp = rightImage.getLayoutParams();
        lp.height = getResources().getDimensionPixelSize(R.dimen.dim36);
        lp.width = getResources().getDimensionPixelSize(R.dimen.dim36);
        rightImage.setLayoutParams(lp);
        rightImage.setImageResource(R.drawable.icon_wtdf_search);
        initData();
        initListener();
        addRecentContactsFragment();
    }

    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected int getNavigationVisible() {
        return View.VISIBLE;
    }

    @Override
    protected int getToolBarBackgroundColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.icon_nav_back;
    }

    @Override
    protected int getTitleText() {
        return R.string.notifaction_answer;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notifaction_session;
    }


    private void initData() {

    }

    private void initListener() {
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamMemberAddActivity.start(NotifactionSessionListActivity.this, "");
            }
        });
    }

    // 将最近联系人列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addRecentContactsFragment() {

        fragment = new RecentContactsFragment();
        fragment.setContainerId(R.id.ll_nt_session);

        final UI activity = (UI) this;

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (RecentContactsFragment) activity.addFragment(fragment);

        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
                LogUtil.d("SessionListFragment", "onRecentContactsLoaded");
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
                LogUtil.d("SessionListFragment", "onUnreadCountChange");
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            @Override
            public void onItemClick(RecentContact recent) {
                LogUtil.d("SessionListFragment", "onItemClick");
                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                switch (recent.getSessionType()) {
                    case P2P:
                        SessionHelper.startP2PSession(NotifactionSessionListActivity.this, recent.getContactId());
                        break;
                    case Team:
                        SessionHelper.startTeamSession(NotifactionSessionListActivity.this, recent.getContactId());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                LogUtil.d("SessionListFragment", "getDigestOfAttachment");
                // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
                // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
                if (attachment instanceof GuessAttachment) {
                    GuessAttachment guess = (GuessAttachment) attachment;
                    return guess.getValue().getDesc();
                } else if (attachment instanceof RTSAttachment) {
                    return "[白板]";
                } else if (attachment instanceof StickerAttachment) {
                    return "[贴图]";
                } else if (attachment instanceof SnapChatAttachment) {
                    return "[阅后即焚]";
                } else if (attachment instanceof RedPacketAttachment) {
                    return "[红包]";
                } else if (attachment instanceof RedPacketOpenedAttachment) {
                    LogUtil.d("SessionListFragment", "content2 = " + ((RedPacketOpenedAttachment) attachment).getDesc(recentContact.getSessionType(), recentContact.getContactId()));
                    return ((RedPacketOpenedAttachment) attachment).getDesc(recentContact.getSessionType(), recentContact.getContactId());
                }

                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                LogUtil.d("SessionListFragment", "getDigestOfTipMsg");
                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        LogUtil.d("SessionListFragment", "content = " + (String) content.get("content"));
                        return (String) content.get("content");
                    }
                }

                return null;
            }
        });
    }
}
