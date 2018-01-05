package com.changcai.buyer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayFragment;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/5/31.
 */

public class CalendarViewFragment extends BaseBottomDialog {


    private final String TAG = CalendarViewFragment.class.getSimpleName();

    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.ll_calendar)
    LinearLayout llCalendar;
    @BindView(R.id.ll_view_father)
    LinearLayout llViewFather;
    Unbinder unbinder;

    private SimpleDateFormat dateFormat2;


    public String endFixString = "235959";
    private List<MyCalendar2> calendarList = new ArrayList<>();
    private String nowday;
    private long nd = 1000 * 24L * 60L * 60L;//一天的毫秒数
    private SimpleDateFormat simpleDateFormat, sd1, sd2;
    private String inday, outday;
    private String deliveryStartTime;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle _arguments = getArguments();
        initCalender(_arguments.getLong("block"),_arguments.getString("time"), (Date) _arguments.getParcelable("date"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.pick_pay_time_calendar;
    }

    @Override
    public void bindView(View v) {
        unbinder = ButterKnife.bind(this, v);
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }


    private void initCalender(final long block, final String deliveryStartTime2, Date currentDate) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat2 = simpleDateFormat;
        nowday = simpleDateFormat.format(new Date());
        sd1 = new SimpleDateFormat("yyyy");
        sd2 = new SimpleDateFormat("dd");
        List<String> listDate = getDateList();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //添加week_view
        View _weekView = LayoutInflater.from(getActivity()).inflate(R.layout.week_view, null, false);
        llCalendar.addView(_weekView);
        for (int i = 0; i < listDate.size(); i++) {
            MyCalendar2 c1 = new MyCalendar2(getActivity(), block, deliveryStartTime2, currentDate);
            c1.setLayoutParams(params);
            Date date = null;
            try {
                date = simpleDateFormat.parse(listDate.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(deliveryStartTime)) {
                c1.setInDay(deliveryStartTime);
            }

            c1.setTheDay(date);
            c1.setOnDaySelectListener(new MyCalendar2.OnDaySelectListener() {
                @Override
                public void onDaySelectListener(View view, String date) {
                    //若日历日期小于当前日期，或日历日期-当前日期超过三个月，则不能点击
                    try {
                        if (simpleDateFormat.parse(date).getTime() < simpleDateFormat.parse(nowday).getTime()) {
                            Toast.makeText(getActivity(), "日期不可选", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        long dayxc = (simpleDateFormat.parse(date).getTime() - simpleDateFormat.parse(nowday).getTime()) / nd;
                        if (dayxc > block) {
                            Toast.makeText(getActivity(), "日期不可选", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    deliveryStartTime = date;
                    setInDay(date);
                    notifyCalenderChange();
                    Observable.empty()
                            .timer(1, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.immediate())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    LogUtil.d(TAG, "call along");
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    LogUtil.d(TAG, "call throwable");
                                }
                            }, new Action0() {
                                @Override
                                public void call() {
                                    Date dateTemp = null;
                                    try {
                                        dateTemp = dateFormat2.parse(deliveryStartTime);
                                    } catch (ParseException e) {
                                        LogUtil.d(TAG, "parse----exception");
                                        e.printStackTrace();
                                    }
                                    if (dateTemp == null) return;
                                    FullPayFragment fullPayFragment = (FullPayFragment) getParentFragment();
                                    fullPayFragment.tvPickUpTime.setText(DateUtil.getDateAndWeekOfDate("yyyy年MM月dd日", dateTemp));
                                    fullPayFragment.deliveryTime = DateUtil.dateToString("yyyyMMdd", dateTemp).concat(endFixString);
                                    if (CalendarViewFragment.this.isAdded()) {
                                        CalendarViewFragment.this.dismiss();
                                    }
                                }
                            });
                }
            });
            llCalendar.addView(c1);
            calendarList.add(c1);
        }
    }


    private void notifyCalenderChange() {
        for (int i = 0; i < calendarList.size(); i++) {
            MyCalendar2 myCalendar = calendarList.get(i);
            myCalendar.notifyDateChange();
        }
    }

    private void setInDay(String inDay) {
        for (int i = 0; i < calendarList.size(); i++) {
            MyCalendar2 myCalendar = calendarList.get(i);
            myCalendar.setInDay(inDay);
        }
    }

    public List<String> getDateList() {
        List<String> list = new ArrayList<>();
        Date date = new Date();
        int nowMon = date.getMonth() + 1;
        String yyyy = sd1.format(date);
        String dd = sd2.format(date);
        if (nowMon == 9) {
            list.add(simpleDateFormat.format(date));
            list.add(yyyy + "-10-" + dd);
            list.add(yyyy + "-11-" + dd);
            if (!dd.equals("01")) {
                list.add(yyyy + "-12-" + dd);
            }
        } else if (nowMon == 10) {
            list.add(yyyy + "-10-" + dd);
            list.add(yyyy + "-11-" + dd);
            list.add(yyyy + "-12-" + dd);
            if (!dd.equals("01")) {
                list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
            }
        } else if (nowMon == 11) {
            list.add(yyyy + "-11-" + dd);
            list.add(yyyy + "-12-" + dd);
            list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
            if (!dd.equals("01")) {
                list.add((Integer.parseInt(yyyy) + 1) + "-02-" + dd);
            }
        } else if (nowMon == 12) {
            list.add(yyyy + "-12-" + dd);
            list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
            list.add((Integer.parseInt(yyyy) + 1) + "-02-" + dd);
            if (!dd.equals("01")) {
                list.add((Integer.parseInt(yyyy) + 1) + "-03-" + dd);
            }
        } else {
            list.add(yyyy + "-" + getMon(nowMon) + "-" + dd);
            list.add(yyyy + "-" + getMon((nowMon + 1)) + "-" + dd);
            list.add(yyyy + "-" + getMon((nowMon + 2)) + "-" + dd);
            if (!dd.equals("01")) {
                list.add(yyyy + "-" + getMon((nowMon + 3)) + "-" + dd);
            }
        }
        return list;
    }

    public String getMon(int mon) {
        String month = "";
        if (mon < 10) {
            month = "0" + mon;
        } else {
            month = "" + mon;
        }
        return month;
    }

    @OnClick({R.id.iv_cancel, R.id.ll_calendar, R.id.ll_view_father})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
            case R.id.ll_view_father:
                dismiss();
                break;
            case R.id.ll_calendar:
                break;
        }
    }
}
