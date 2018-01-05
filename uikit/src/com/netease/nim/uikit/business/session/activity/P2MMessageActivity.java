//package com.netease.nim.uikit.business.session.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.netease.nim.uikit.R;
//import com.netease.nim.uikit.api.NimUIKit;
//import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
//import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
//import com.netease.nim.uikit.api.model.session.SessionCustomization;
//import com.netease.nim.uikit.api.model.user.UserInfoObserver;
//import com.netease.nim.uikit.business.session.CustomVipView;
//import com.netease.nim.uikit.business.session.constant.Extras;
//import com.netease.nim.uikit.business.session.fragment.MessageFragment;
//import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
//import com.netease.nim.uikit.common.util.log.LogUtil;
//import com.netease.nim.uikit.impl.NimUIKitImpl;
//import com.netease.nimlib.sdk.NIMClient;
//import com.netease.nimlib.sdk.Observer;
//import com.netease.nimlib.sdk.msg.MsgServiceObserve;
//import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
//import com.netease.nimlib.sdk.msg.model.CustomNotification;
//import com.netease.nimlib.sdk.msg.model.IMMessage;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
///**
// * Created by lufeisong on 2017/12/18.
// */
//
//public class P2MMessageActivity extends BaseMessageActivity {
//    private boolean isResume = false;
//    private static String EXTRA_ACCOUNTS = "accounts";
//    private ArrayList<String> contactIds;
//
//    private RelativeLayout rl_offline_info;
//    private ImageView btnBack;
//    private TextView tvTitle;
//
//    private List<CustomVipView> customVipViews = new ArrayList<>();
//
//    public static void start(Context context, ArrayList<String> contactIds, SessionCustomization customization, IMMessage anchor) {
//        LogUtil.d("contactId", "P2MMessageActivity contactId = " + contactIds.get(0));
//        Intent intent = new Intent();
//        intent.putExtra(Extras.EXTRA_ACCOUNT, contactIds.get(0));//第一次进入，显示第一个人的会话
//        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
//        if (anchor != null) {
//            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
//        }
//        intent.putStringArrayListExtra(EXTRA_ACCOUNTS, contactIds);
//        intent.setClass(context, P2MMessageActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        context.startActivity(intent);
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        initView();
//        initListener();
//        initData();
//        initHeader();
//        // 单聊特例话数据，包括个人信息，
//        requestBuddyInfo();
//        displayOnlineState();
//        registerObservers(true);
//        registerOnlineStateChangeListener(true);
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        registerObservers(false);
//        registerOnlineStateChangeListener(false);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        isResume = true;
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        isResume = false;
//    }
//
//    private void parseIntent() {
//        contactIds = getIntent().getExtras().getStringArrayList(EXTRA_ACCOUNTS);
//    }
//
//    private void requestBuddyInfo() {
//        setTitle("顾问服务：" + UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//    }
//
//    private void registerObservers(boolean register) {
//        if (register) {
//            registerUserInfoObserver();
//        } else {
//            unregisterUserInfoObserver();
//        }
//        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
//        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
//    }
//
//    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
//        @Override
//        public void onAddedOrUpdatedFriends(List<String> accounts) {
//            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        }
//
//        @Override
//        public void onDeletedFriends(List<String> accounts) {
//            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        }
//
//        @Override
//        public void onAddUserToBlackList(List<String> account) {
//            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        }
//
//        @Override
//        public void onRemoveUserFromBlackList(List<String> account) {
//            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        }
//    };
//    private UserInfoObserver uinfoObserver;
//
//    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
//        @Override
//        public void onlineStateChange(Set<String> accounts) {
//            LogUtil.d("contactId", accounts.toString());
//            // 更新 toolbar
////            if (accounts.contains(sessionId)) {
//            // 按照交互来展示
//            displayOnlineState();
////            }
//        }
//    };
//
//    private void registerOnlineStateChangeListener(boolean register) {
//        if (!NimUIKitImpl.enableOnlineState()) {
//            return;
//        }
//        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
//    }
//
//    private void displayOnlineState() {
//        for (int i = 0; i < contactIds.size(); i++) {
//            if (onLine(contactIds.get(i))) {
//                customVipViews.get(i).click();
//                if(contactIds.get(i) == sessionId){
//                    messageFragment.showInputPanel();
//                }
//            } else {
//                customVipViews.get(i).unClick();
//                if(contactIds.get(i) == sessionId){
//                    messageFragment.hideInputPanel();
//                }
//            }
//            customVipViews.get(i).setName(UserInfoHelper.getUserName(contactIds.get(i)));
//
//
//        }
//    }
//
//    private boolean onLine(String account) {
//        if (!NimUIKitImpl.enableOnlineState()) {
//            return false;
//        }
//        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(account);
//        if (detailContent == null || detailContent.equals("离线")) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private void registerUserInfoObserver() {
//        if (uinfoObserver == null) {
//            uinfoObserver = new UserInfoObserver() {
//                @Override
//                public void onUserInfoChanged(List<String> accounts) {
//                    if (accounts.contains(sessionId)) {
//                        requestBuddyInfo();
//                    }
//                }
//            };
//        }
//        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
//    }
//
//    private void unregisterUserInfoObserver() {
//        if (uinfoObserver != null) {
//            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
//        }
//    }
//
//    /**
//     * 命令消息接收观察者
//     */
//    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
//        @Override
//        public void onEvent(CustomNotification message) {
//            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
//                return;
//            }
//            showCommandMessage(message);
//        }
//    };
//
//    protected void showCommandMessage(CustomNotification message) {
////        if (!isResume) {
////            return;
////        }
////
////        String content = message.getContent();
////        try {
////            JSONObject json = JSON.parseObject(content);
////            int id = json.getIntValue("id");
////            if (id == 1) {
////                // 正在输入
////                Toast.makeText(P2MMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
////            } else {
////                Toast.makeText(P2MMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
////            }
////
////        } catch (Exception e) {
////
////        }
//    }
//
//    @Override
//    protected MessageFragment fragment() {
//        Bundle arguments = getIntent().getExtras();
//        arguments.putBoolean(Extras.EXTRA_ONLINE, onLine(sessionId));
//        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
//        MessageFragment fragment = new MessageFragment();
//        fragment.setArguments(arguments);
//        fragment.setContainerId(R.id.message_fragment_container);
//        return fragment;
//    }
//
//    @Override
//    protected int getContentViewId() {
//        return R.layout.nim_message_activity;
//    }
//
//    @Override
//    protected void initToolBar() {
////        ToolBarOptions options = new NimToolBarOptions();
////        setToolBar(R.id.toolbar, options);
//
//    }
//
//    private void initView() {
//        rl_offline_info = (RelativeLayout) findViewById(R.id.rl_offline_info);
//        btnBack = (ImageView) findViewById(R.id.btnBack);
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//    }
//
//    private void initListener() {
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                P2MMessageActivity.this.finish();
//                onBackPressed();
//            }
//        });
//    }
//
//    private void initData() {
//        tvTitle.setText("会员顾问");
//    }
//
//    private void initHeader() {
//        parseIntent();
//        LinearLayout container = (LinearLayout) findViewById(R.id.ll_vip_list);
//        View view = LayoutInflater.from(this).inflate(R.layout.nim_message_activity_header, null);
//        LinearLayout ll_container_header = view.findViewById(R.id.ll_container_header);
//        for (int i = 0; i < contactIds.size(); i++) {
//            CustomVipView customVipView = new CustomVipView(this);
//            customVipViews.add(customVipView);
//            ll_container_header.addView(customVipView);
//            final int finalI = i;
//            customVipView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    sessionId = contactIds.get(finalI);
//                    Bundle arguments = getIntent().getExtras();
//                    arguments.putString(Extras.EXTRA_ACCOUNT, sessionId);
//                    messageFragment = (MessageFragment) switchContent(fragment());
//
//                }
//            });
//        }
//        container.addView(view);
//    }
//
//
//}
