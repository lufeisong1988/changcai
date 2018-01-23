package com.changcai.buyer.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.bean.GetImTeamsBean;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.im.main.activity.NotifactionListActivity;
import com.changcai.buyer.im.main.model.NotifactionListModelInterface;
import com.changcai.buyer.im.main.model.imp.NotifactionListModelImp;
import com.changcai.buyer.im.session.SessionHelper;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseAbstraceFragment;
import com.changcai.buyer.ui.news.bean.NewsClassify;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.indicator.FragmentContainerHelper;
import com.changcai.buyer.view.indicator.MagicIndicator;
import com.changcai.buyer.view.indicator.ViewPagerHelper;
import com.changcai.buyer.view.indicator.commonnavigator.CommonNavigator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.CommonNavigatorAdapter;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerTitleView;
import com.changcai.buyer.view.indicator.commonnavigator.indicators.LinePagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.titles.SimplePagerTitleView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionSuccess;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by lufeisong on 2017/10/12.
 */

public class NewMainFragment extends BaseAbstraceFragment implements View.OnClickListener, NotifactionListModelImp.NotifactionListModelCallback {


    @BindView(R.id.navigation_indicator)
    MagicIndicator navigationIndicator;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.emptyView)
    ImageView emptyView;
    @BindView(R.id.tv_empty_action)
    TextView tvEmptyAction;
    @BindView(R.id.rl_reload_root_view)
    LinearLayout rlReloadRootView;
    @BindView(R.id.iv_resource_phone)
    ImageView ivResourcePhone;
    @BindView(R.id.iv_dot)
    ImageView ivDot;

    private NotifactionListModelInterface model;
    private List<GetCounselorsModel.InfoBean> info;

    //cms 资讯分类目适配器
    private CommonNavigator commonNavigator;
    private CommonNavigatorAdapter commonNavigatorAdapter;
    private FragmentContainerHelper mFragmentContainerHelper;

    //cms 资讯分类目
    private List<NewsClassify> newsClassify = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private Observable<Integer> switchPageEvent;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("newsClassify", (Serializable) newsClassify);
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onResume() {
        getCMSData();
        model.getCounselorsModel();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        RxBus.get().unregister("switchPage", switchPageEvent);
        registerRecentContact(false);
        super.onDestroy();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int setResId() {
        return R.layout.fragment_news_main;
    }

    @Override
    protected void initListener() {
        rlReloadRootView.setOnClickListener(this);
        ivResourcePhone.setOnClickListener(this);
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    /**
     * 刷新消息红点
     */
    public void updateDot() {

        model.getCounselorsModel();
    }

    @Override
    public void initData() {
        model = new NotifactionListModelImp(this);
        registerRecentContact(true);

//        getCMSData();
        switchPageEvent = RxBus.get().register("switchPage", Integer.class);
        switchPageEvent.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer index) {
                clickTitleSwitchPage(index);
            }
        });
    }

    //初始化数据(获取csm 资讯分类目)
    private void getCMSData() {
        HashMap<String, String> params = new HashMap<String, String>();
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_INFO_TAB, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                disMissReloadView();
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    List<NewsClassify> newsClassifyTemp = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<List<NewsClassify>>() {
                            }.getType());
                    if (!equalCache(newsClassifyTemp)) {
                        //手动增加首页model
                        NewsClassify newsClassifyOne = new NewsClassify();
                        newsClassifyOne.setFolderId("-1");
                        newsClassifyOne.setName("推荐");
                        newsClassify.clear();
                        newsClassify.add(newsClassifyOne);
                        newsClassify.addAll(newsClassifyTemp);
                        initFragments();
                        initViewPage();
                        initIndicator();
                    } else {
                        ((NewIndexFragment) fragments.get(0)).checkData();
                    }
                } else {
                    if (newsClassify.size() == 0) {
                        showReloadView(true);
                    } else {
                        ((NewIndexFragment) fragments.get(0)).checkData();
                    }
                    if(isAdded()){
                        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error), errorCode);
                    }
                }

            }

            @Override
            public void onError(String error) {
                super.onError(error);
                if (newsClassify.size() == 0) {
                    showReloadView(true);
                } else {
                    ((NewIndexFragment) fragments.get(0)).checkData();
                }
                if(isAdded()){
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                }
            }
        }, false);
    }

    //检测获取的最新数据是否与缓存相同，避免重复刷新，加载fragment
    private boolean equalCache(List<NewsClassify> newsClassifyTemp) {
        if (newsClassifyTemp != null) {
            if (newsClassifyTemp.size() == newsClassifyTemp.size()) {//长度相等，就判断内容
                newsClassifyTemp.removeAll(newsClassify);
                if (newsClassifyTemp.size() == 0) {
                    return true;
                } else {//长度不等，数据变动了.
                    return false;
                }
            } else {//长度不等，数据变动了。
                return false;
            }
        } else {//数据异常，不做修改
            return true;
        }
    }

    /**
     * 初始化fragments
     */
    private void initFragments() {
        fragments.clear();
        for (int i = 0; i < newsClassify.size(); i++) {
            if (i == 0) {
                NewIndexFragment newIndexFragment = new NewIndexFragment(newsClassify);
                fragments.add(newIndexFragment);
            } else {
                NewFragment newFragment = new NewFragment(newsClassify.get(i), i);
                fragments.add(newFragment);
            }
        }
    }

    /**
     * 初始化viewpager
     */
    private void initViewPage() {
//        mViewPager.setAdapter(new StrategyViewPagerAdapter(getChildFragmentManager(), getContext(), fragments));
        mViewPager.setAdapter(new NewsAdapter(getChildFragmentManager()));
        mViewPager.setCurrentItem(0);
//        mViewPager.setPageTransformer(true, new StereoPagerTransformer());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if (position == 0) {//切换到首页,重新请求数据
                    getCMSData();
                } else {
                    ((NewFragment) fragments.get(position)).checkData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 初始化指示栏
     */
    private void initIndicator() {
        mFragmentContainerHelper = new FragmentContainerHelper();
        commonNavigator = new CommonNavigator(getContext());
        commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return newsClassify.size();
            }

            @SuppressWarnings("deprecation")
            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(getContext());
                simplePagerTitleView.setText(newsClassify.get(index).getName());
                simplePagerTitleView.setPadding(getResources().getDimensionPixelSize(R.dimen.dim15));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.storm_gray));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.membership_color));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickTitleSwitchPage(index);
                    }
                });
                return simplePagerTitleView;
            }

            @SuppressWarnings("deprecation")
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(getContext());
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineWidth(getResources().getDimension(R.dimen.dim20));
                linePagerIndicator.setLineHeight(getResources().getDimension(R.dimen.dim5));
                linePagerIndicator.setStartInterpolator(new AccelerateInterpolator(2.5f));
                linePagerIndicator.setEndInterpolator(new DecelerateInterpolator(2.5f));
                linePagerIndicator.setColors(getResources().getColor(R.color.membership_color));
                return linePagerIndicator;
            }
        };
        commonNavigator.setAdapter(commonNavigatorAdapter);
        navigationIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(navigationIndicator);
        ViewPagerHelper.bind(navigationIndicator, mViewPager);
    }

    void clickTitleSwitchPage(int index) {
        mFragmentContainerHelper.setInterpolator(new LinearInterpolator());
        mFragmentContainerHelper.handlePageSelected(index);
        mViewPager.setCurrentItem(index);
        if (index == 0) {//切换到首页,重新请求数据
            getCMSData();
        } else {
            ((NewFragment) fragments.get(index)).checkData();
        }

        Map<String, String> stringMap = new HashMap<String, String>();
        stringMap.put("account", UserDataUtil.isLogin() ? UserDataUtil.userMobile() : "未登录用户");
        stringMap.put("memberGrade", UserDataUtil.isLogin() ? UserDataUtil.userGradeName() : "未登录用户");
        stringMap.put("tabName", newsClassify.get(index).getName());
        stringMap.put("startClickedTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())).toString());
        MobclickAgent.onEvent(getActivity(), "dailyList_tabFilter", stringMap);
    }

    public void disMissReloadView() {
        rlReloadRootView.setVisibility(View.GONE);
        emptyView.setImageDrawable(null);
        tvEmptyAction.setText(null);
    }

    public void showReloadView(boolean isErrorStatus) {
        rlReloadRootView.setVisibility(View.VISIBLE);
        if (isErrorStatus) {
            emptyView.setImageResource(R.drawable.default_img_404);
            tvEmptyAction.setText("加载失败，请点击刷新");
        } else {
            emptyView.setImageResource(R.drawable.default_img_none);
            tvEmptyAction.setText(R.string.no_cms_dta);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_reload_root_view:
                getCMSData();
                break;
            case R.id.iv_resource_phone:
//                PermissionGen.needPermission(this, 100,
//                        new String[] {
//                                Manifest.permission.CALL_PHONE,
//                        }
//                );
                startActivity(new Intent(getActivity(), NotifactionListActivity.class));

                break;
        }
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomethingSucceed() {
        showChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));

    }

    @PermissionFail(requestCode = 100)
    public void doSomethingFail() {
        Toast.makeText(getActivity(), R.string.perssion_for_call, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取顾问列表
     *
     * @param info
     */
    @Override
    public void getCounselorsModelSucceed(List<GetCounselorsModel.InfoBean> info) {
        this.info = info;
        initMessageObserave();
        SessionHelper.setInfo(info);
    }

    @Override
    public void getCounselorsModelFail(String failStr) {
        this.info = null;
        initMessageObserave();
    }

    @Override
    public void getCounselorsModelError() {
        this.info = null;
        initMessageObserave();
    }

    @Override
    public void getImTeamsSucceed(GetImTeamsBean getImTeamsBeen) {

    }

    @Override
    public void getImTeamsFail(String failStr) {

    }

    @Override
    public void getImTeamsError() {

    }


    class NewsAdapter extends FragmentStatePagerAdapter {

        public NewsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    //主动获取未读消息和message
    private void initMessageObserave() {
        List<RecentContact> contactsBlock = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
        updateUI(contactsBlock);
    }

    //注册获取未读消息和message监听
    private void registerRecentContact(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            updateUI(recentContacts);
        }
    };

    //刷新UI （复杂化了，只要读出有未读消息即可）
    private void updateUI(final List<RecentContact> contactsBlock) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (contactsBlock == null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivDot.setVisibility(View.GONE);
                        }
                    });
                    return;
                }
                int unReadMsgCount = 0;
                int unReadMsgConsultantCount = 0;//顾问未读数目
                List<RecentContact> contactsConsultantBlock = new ArrayList<>();//顾问集合
                UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
                //遍历所有未读消息数量，最近一条消息
                //遍历出顾问集合
                for (int i = 0; i < contactsBlock.size(); i++) {//遍历出所有未读消息数目和message
                    RecentContact recentContact = contactsBlock.get(i);
                    LogUtil.d("NimIM", "所有 ： 第" + i + "位: id = " + recentContact.getContactId() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent());
                    if (info != null) {
                        for (int j = 0; j < info.size(); j++) {//遍历出顾问
                            if (info.get(j).getAccid().equals(recentContact.getContactId())) {
                                contactsConsultantBlock.add(recentContact);
                                break;
                            }
                        }
                    }
                    unReadMsgCount += recentContact.getUnreadCount();

                }
                //遍历顾问未读消息数量，最近一条消息
                for (int i = 0; i < contactsConsultantBlock.size(); i++) {
                    RecentContact recentContact = contactsConsultantBlock.get(i);
                    LogUtil.d("NimIM", "顾问团 ： 第" + i + "位: id = " + recentContact.getContactId() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent());
                    unReadMsgConsultantCount += recentContact.getUnreadCount();
                }
                //不是顾问,筛选顾问团未读信息
                if (userInfo.getServiceLevel() == null && userInfo.getServiceStatus() == null && userInfo.getCounselorStatus() == null) {
                    final int finalUnReadMsgConsultantCount = unReadMsgConsultantCount;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalUnReadMsgConsultantCount > 0) {
                                ivDot.setVisibility(View.VISIBLE);
                            } else {
                                ivDot.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {//是顾问,获取所有未读信息
                    final int finalUnReadMsgCount = unReadMsgCount;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalUnReadMsgCount > 0) {
                                ivDot.setVisibility(View.VISIBLE);
                            } else {
                                ivDot.setVisibility(View.GONE);
                            }
                        }
                    });
                }


            }
        }).start();

    }
}
