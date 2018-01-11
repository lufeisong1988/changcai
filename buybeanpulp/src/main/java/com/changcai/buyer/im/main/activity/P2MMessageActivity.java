package com.changcai.buyer.im.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.im.DemoCache;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.CustomVipView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.session.activity.BaseMessageActivity;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lufeisong on 2017/12/21.
 */

public class P2MMessageActivity extends BaseMessageActivity {
    private boolean isDsetory = false;
    private boolean isResume = false;
    private static String EXTRA_ACCOUNTS = "accounts";


    private LinearLayout rl_offline_info;
    private ImageView btnBack;
    private TextView tvTitle;
    private TextView tv_info;

    private List<CustomVipView> customVipViews = new ArrayList<>();//在线顾问团队
    private int currentCustomVipView = 0;//当前选中的视图
    private final Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<P2MMessageActivity> mActivity;

        public MyHandler(P2MMessageActivity activity) {
            mActivity = new WeakReference<P2MMessageActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    mActivity.get().rl_offline_info.setVisibility(View.VISIBLE);
                    mActivity.get().messageFragment.hideInputPanel();
                    break;
                case 2:
                    mActivity.get().rl_offline_info.setVisibility(View.GONE);
                    mActivity.get().messageFragment.showInputPanel();
                    break;
            }

        }
    }

    public static void start(Context context, ArrayList<GetCounselorsModel.InfoBean> infoBeens, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, infoBeens.size() > 0 ? infoBeens.get(0).getAccid() : 0);//第一次进入，显示第一个人的会话
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.putExtra(EXTRA_ACCOUNTS, infoBeens);
        intent.setClass(context, P2MMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initListener();
        initData();

        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
        registerOnlineStateChangeListener(true);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDsetory = true;
        registerObservers(false);
        registerOnlineStateChangeListener(false);
        registerMessageObserver(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    private void parseIntent() {
        ArrayList<GetCounselorsModel.InfoBean> infoBeens = (ArrayList<GetCounselorsModel.InfoBean>) getIntent().getExtras().getSerializable(EXTRA_ACCOUNTS);
        initHeader(infoBeens);
    }

    private void requestBuddyInfo() {
        setTitle("顾问服务：" + UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };
    private UserInfoObserver uinfoObserver;

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            LogUtil.d("contactId", accounts.toString());
            // 更新 toolbar
//            if (accounts.contains(sessionId)) {
            // 按照交互来展示
            displayOnlineState();
            onLine(DemoCache.getAccount());
//            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void displayOnlineState() {
//        for (int i = 0; i < infoBeens.size(); i++) {
//            String accId = infoBeens.get(i).getAccid();
//            if (onLine(accId)) {
//                customVipViews.get(i).click();
//                if(accId == sessionId){
//                    messageFragment.showInputPanel();
//                }
//            } else {
//                customVipViews.get(i).unClick();
//                if(accId == sessionId){
//                    messageFragment.hideInputPanel();
//                }
//            }
//            customVipViews.get(i).setName(UserInfoHelper.getUserName(accId));
//
//
//        }
    }

    private boolean onLine(String account) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return false;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(account);
        if (detailContent == null || detailContent.equals("离线")) {
            return false;
        } else {
            return true;
        }
    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
//        if (!isResume) {
//            return;
//        }
//
//        String content = message.getContent();
//        try {
//            JSONObject json = JSON.parseObject(content);
//            int id = json.getIntValue("id");
//            if (id == 1) {
//                // 正在输入
//                Toast.makeText(P2MMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(P2MMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (Exception e) {
//
//        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putString(Extras.EXTRA_ACCOUNT, sessionId);
        arguments.putBoolean(Extras.EXTRA_ONLINE, true);
//        arguments.putBoolean(Extras.EXTRA_ONLINE, onLine(sessionId));//暂时不根据网易云讯的状态来判断，根据服务器返回的状态
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
//        ToolBarOptions options = new NimToolBarOptions();
//        setToolBar(R.id.toolbar, options);

    }

    private void initView() {
        rl_offline_info = (LinearLayout) findViewById(R.id.rl_offline_info);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tv_info = (TextView) findViewById(R.id.tv_info);
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2MMessageActivity.this.finish();
                onBackPressed();
            }
        });
    }

    private void initData() {
        tvTitle.setText("会员顾问");
        parseIntent();
    }

    private void initHeader(ArrayList<GetCounselorsModel.InfoBean> infoBeens) {
        LinearLayout container = (LinearLayout) findViewById(R.id.ll_vip_list);
        View view = LayoutInflater.from(this).inflate(R.layout.view_ptom_header, null);
        LinearLayout ll_container_header = view.findViewById(R.id.ll_container_header);
        for (int i = 0; i < infoBeens.size(); i++) {
            final GetCounselorsModel.InfoBean infoBean = infoBeens.get(i);
            final CustomVipView customVipView = new CustomVipView(this);
            ll_container_header.addView(customVipView);
            customVipView.setInfoBean(infoBean);
            //设置服务等级
            String levelStr = "";
            int serviceLevel = infoBean.getServiceLevel();
            if (serviceLevel == 0) {
                levelStr = "青铜及以上会员";
                customVipView.setGuard(R.drawable.icon_qtabove);
            } else if (serviceLevel == 100) {
                levelStr = "白银及以上会员";
                customVipView.setGuard(R.drawable.icon_byabove);
            } else if (serviceLevel == 150) {
                levelStr = "白银PLUS及以上会员";
                customVipView.setGuard(R.drawable.icon_yjabove);
            } else if (serviceLevel == 200) {
                levelStr = "黄金及以上会员";
                customVipView.setGuard(R.drawable.icon_hjabove);
            } else if (serviceLevel == 300) {
                levelStr = "钻石及以上会员";
                customVipView.setGuard(R.drawable.icon_zsabove);
            } else if (serviceLevel == 400) {
                levelStr = "VIP会员";
                customVipView.setGuard(R.drawable.icon_vipabove);
            } else {
                customVipView.hideGurad();
            }
            //设置是否在线
            //默认第一个被选中
            if (i == currentCustomVipView) {
                customVipView.click();
            }
            final String status = infoBean.getCounselorStatus();
            boolean statusAble = false;
            if (status.equals("ONLINE")) {
                statusAble = true;
            } else {
                statusAble = false;
            }
            final boolean finalStatusAble = statusAble;
            final String finalLevelStr = levelStr;
            if (i == 0) {//初始化第一个
                updateClickHead(infoBean, finalStatusAble, finalLevelStr);
            }
            customVipView.setTag(i);
            customVipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentCustomVipView == (int) v.getTag()) {
                        return;
                    }
                    customVipViews.get(currentCustomVipView).unClick();
                    currentCustomVipView = (int) v.getTag();
                    customVipViews.get(currentCustomVipView).click();
                    P2MMessageActivity.this.sessionId = customVipView.getInfoBean().getAccid();
                    messageFragment = (MessageFragment) switchContent(fragment());
                    updateClickHead(infoBean, finalStatusAble, finalLevelStr);
                }
            });
            customVipViews.add(customVipView);
        }
        container.addView(view);
        initDot();
        requestUserInfo(customVipViews);
        registerMessageObserver(true);
    }

    private void updateClickHead(GetCounselorsModel.InfoBean infoBean, boolean finalStatusAble, String finalLevelStr) {

        if (Integer.parseInt(UserDataUtil.getGrade()) < infoBean.getServiceLevel() || !finalStatusAble) {
            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);
            if (!finalStatusAble) {
                tv_info.setText("该顾问当前不在线，请线下联系或咨询其他在线顾问");
            } else if (Integer.parseInt(UserDataUtil.getGrade()) < infoBean.getServiceLevel()) {
                tv_info.setText("该顾问当前服务" + finalLevelStr + "，会员升级请咨询服务窗。");
            }
        } else {
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 从本地获取用户信息
     *
     * @param customVipViews
     */


    private void requestUserInfo(List<CustomVipView> customVipViews) {
        if (customVipViews.size() == 0) {
            return;//没有顾问
        }
        ArrayList<String> accounts = new ArrayList<>();
        for (int i = 0; i < customVipViews.size(); i++) {
            accounts.add(customVipViews.get(i).getInfoBean().getAccid());
        }
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(accounts);
        //如果都不存在会返回null，防止nullpointerror ，赋值初始化
        if (users == null) {
            users = new ArrayList<>();
        }
        updateLocalUserInfo(customVipViews, users, true);
    }

    /**
     * 刷新用户信息
     *
     * @param users
     */
    private void updateLocalUserInfo(List<CustomVipView> customVipViews, List<NimUserInfo> users, boolean requestNet) {

        //刷新顾问信息，同时获取剩余在本地没信息的顾问们，把剩余顾问的数据扔给网络请求
        ArrayList<CustomVipView> unLocalCustomVipViews = new ArrayList<>();//本地不存在缓存的顾问账号
        unLocalCustomVipViews.addAll(customVipViews);
        for (int i = 0; i < customVipViews.size(); i++) {
            final CustomVipView customVipView = customVipViews.get(i);
            for (int j = 0; j < users.size(); j++) {
                final NimUserInfo userInfo = users.get(j);
                if (customVipView.getInfoBean().getAccid().equals(userInfo.getAccount())) {
                    unLocalCustomVipViews.remove(customVipViews.get(i));
                    if (userInfo.getName() != null && !userInfo.getName().equals("")) {
                        customVipView.setName(userInfo.getName());
                    } else {
                        if (userInfo.getMobile() != null && !userInfo.getMobile().equals("")) {
                            customVipView.setName(userInfo.getMobile());
                        } else {
                            if (userInfo.getAccount() != null && !userInfo.getAccount().equals("")) {
                                customVipView.setName(userInfo.getAccount());
                            }
                        }
                    }
                    customVipView.setHeader(userInfo.getAvatar());
                    break;
                }
            }
        }
        if (unLocalCustomVipViews.size() > 0 && requestNet) {
            updateNetLocalUserInfo(unLocalCustomVipViews);
        }
    }

    /**
     * 从服务器获取顾问信息
     */
    private void updateNetLocalUserInfo(final List<CustomVipView> customVipViews) {
        final ArrayList<String> accounts = new ArrayList<>();
        for (int i = 0; i < customVipViews.size(); i++) {
            accounts.add(customVipViews.get(i).getInfoBean().getAccid());
        }
        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallback<List<NimUserInfo>>() {
            @Override
            public void onSuccess(List<NimUserInfo> nimUserInfos) {
                //如果都不存在会返回null，防止nullpointerror ，赋值初始化
                if (nimUserInfos == null) {
                    nimUserInfos = new ArrayList<>();
                }
                if (!isDsetory)
                    updateLocalUserInfo(customVipViews, nimUserInfos, false);
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });

    }

    /**
     * 初始化dot状态
     */
    private void initDot() {
        List<RecentContact> recentContacts = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
        updateDot(recentContacts);
    }

    /**
     * 注册消息未读观察者
     *
     * @param register
     */
    private void registerMessageObserver(boolean register) {

        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            updateDot(recentContacts);
        }
    };

    private void updateDot(List<RecentContact> recentContacts) {
        for (int i = 0; i < customVipViews.size(); i++) {
            String sessiondId = customVipViews.get(i).getInfoBean().getAccid();
            boolean haveUnReadMessage = false;
            for (int j = 0; j < recentContacts.size(); j++) {
                RecentContact recentContact = recentContacts.get(j);
                if (sessiondId.equals(recentContact.getContactId()) && recentContact.getUnreadCount() > 0) {
                    haveUnReadMessage = true;
                    break;
                }
            }
            if (haveUnReadMessage) {
                customVipViews.get(i).showDot();
            } else {
                customVipViews.get(i).hideDot();
            }
        }
    }
}
