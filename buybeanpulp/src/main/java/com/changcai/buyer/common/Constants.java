package com.changcai.buyer.common;

import android.os.Environment;

import java.io.File;


/**
 * 常量类
 *
 * @author Zhoujun
 *         说明：	1、一些应用常量在此定义
 *         2、常量包括：一些类型的定义，在其他程序中不能够出现1 2 3之类的值。
 */
public class Constants {

    public static final String KEY_LOCAL_INFO = "local";
    public static final String KEY_PERSON_CERTIFY="person_certify";
    public static final String REQUEST_BASE_PARAMETERS = "baseParameters";

    public static  int TABINDEX = -1;
    public static  int FOLDER = -1;
    /**
     * 应用文件存放目录
     */
    public static final String APP_DIR_NAME = "buybeanpulp";
    public static final String SERVICE_NUMBER = "1064";

    /**
     * ASE加密key
     */
    public static final String AES_KEY = "UITN25LMUQC436IM";

    public static final String APP_ID = "autolinkedacube2";

    public static final String KEY = "577e678f16944509b990";

    //默认用户ID
    public static final String DEFAULT_USER_ID = "-1";

    //图片目录
    public static final String IMAGE_DIR = Environment.getExternalStorageDirectory() + "/" + APP_DIR_NAME + "/image/";
    public static final String CRASH_DIR = Environment.getExternalStorageDirectory() + "/" + APP_DIR_NAME ;


    public static final String BIG_AVATAR = IMAGE_DIR + "big_avatar.jpg";//大头像
    public static final String SMALL_AVATAR = IMAGE_DIR + "small_avatar.jpg";//小头像
    //日志目录
    public static final String LOG_DIR = Environment.getExternalStorageDirectory() + "/" + APP_DIR_NAME + "/log/";
    //文件目录
    public static final String FILE_DIR = Environment.getExternalStorageDirectory() + "/" + APP_DIR_NAME + "/file/";

    public static final String WEB_CACHE = Environment.getExternalStorageDirectory()+ File.separator + APP_DIR_NAME + "web_cache";

    public static final String CONNECT_SERVER_FAILED = "服务器连接失败";
    public static final String JSON_EXCEPTION = "json解析错误";
    public static final String CONNECT_SERVER_TIMEOUT = "服务器超时";
    public static final String internal_storage_h5 =File.separator+ APP_DIR_NAME+"/h5/";

    /**
     * 微博绑定类型，点击账号绑定和新浪微博
     */
    public static final String EXTRA_BIND_FROM = "extra_bind_from";
    public static final String BIND_WEIBO = "bind_weibo";// 微博

    public static final String EXTRA_DATA = "extra_data";
    /**
     * 微博绑定的request code
     */
    public static final int REQUEST_CODE_BIND_WEIBO = 11;
    public static final int REQUEST_CODE_BIND_RENREN = 12;
    public static final int REQUEST_CODE_SELECT_CONTACT = 13;// 选取联系人;

    /**
     * 文件的命名；
     */
    public static final String SCREEN_CAPTURE = "jieping";
    /**
     * 新浪微博配置
     */
    public static final String WEIBO_CONSUMER_KEY = "1444181189";// 替换为开发者的appkey，例如"1646212960";
    public static final String WEIBO_CONSUMER_SECRET = "5d00086b8949cfe583c4b66dd1041590";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";
    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 微博应用回调地址
    /**
     * 微信
     */
    public static final String WEIXIN_APP_ID = "wx7f330838571ace02";// app id
    // public static final String WEIXIN_APP_KEY =
    // "3a780a13e1b956ac956c3845f3a97eb1";//zhou test api key
    public static final String WEIXIN_APP_KEY = "10b6fef6e02bdc05736101693bb19d2c";// api
    // key
    /**
     * 网络请求状态码；
     */
    public static final String REQUEST_SUCCESS_S = "0";//网络请求成功；
    public static final String REQUEST_FAIL = "-1";

    public static final String RESPONSE_CODE = "errorCode";
    public static final String RESPONSE_DESCRIPTION = "errorDesc";
    public static final String RESPONSE_CONTENT = "resultObject";

    /**
     * SharedPreference key
     */
    public static final String KEY_IS_LOGIN = "key_is_login";//是否登录了
    public static final String KEY_IS_FACTORY = "key_is_facotry";//是否工厂了
    public static final String KEY_IS_AUTH = "key_is_auth";//是否认证了
    public static final String KEY_IS_BUYER = "key_is_buyer";//是否购买者

    public static final String KEY_IS_READ_GUIDER = "help1.0";//根据版本号来
    public static final String KEY_SPLASH_IMAGE_URL = "splash_image_url";     //欢迎页的图片地址
    public static final String KEY_TOKEN_ID = "tokenId";
    public static final String NINEVIEW_PASSWORD = "nine_password";
    public static final String KEY_USER_INFO = "user_info";
    public static final String KEY_LAST_USERNAME = "key_last_username";//上一次用户名


    public static final String KEY_ARTICLE_IDS = "key_article_id";//已经阅读过的文章ID

    /**
     * 我的订单
     */
//    待签署合同   NEW_BUY_CONFIRMING
//    待买家支付保证金 NEW_BUY_DEPOSITING
//    待卖家确认 OPEN_SELL_UNDER
//    待卖家签署合同 OPEN_SELL_CONFIRMING
//    待卖家支付保证金 OPEN_SELL_DEPOSITING
//    交易达成，待点价 OPEN_BUY_PRICE
//    点价中 OPEN_BUY_PRICING
//    交易达成，待提货 OPEN_BUY_PICK
//    买家提货中 OPEN_BUY_PICKING
//    交易完成 COMPLATE
//    交易取消 CANCEL
    public static final String NEW_BUY_CONFIRMING = "NEW_BUY_CONFIRMING";
    public static final String NEW_BUY_DEPOSITING = "NEW_BUY_DEPOSITING";
    public static final String OPEN_BUY_PICK = "OPEN_BUY_PICK";
    public static final String OPEN_BUY_PICKING = "OPEN_BUY_PICKING";
    public static final String OPEN_SELL_UNDER = "OPEN_SELL_UNDER";
    public static final  String OPEN_SELL_CONFIRMING = "OPEN_SELL_CONFIRMING";
    public static final  String ALL_PAY = "ALL_PAY";
    public static final  String FAST_PAY = "FAST_PAY";
    public static final  String NEW_BUY_FRONT_MONEY = "NEW_BUY_FRONT_MONEY";//待买家支付定金

    public static final String OPEN_SELL_DEPOSITING ="OPEN_SELL_DEPOSITING";

    public static final String ORDER_CANCEL = "ORDER_CANCEL";


    //    UNPAY("待支付货款"), PARTPAY("货款部分支付"), PAYED("已支付货款"), CONFIRM("待确认收货"), COMPLETE("单次提货已完成"), INVALID("已失效");
    public static final String UNPAY = "UNPAY";
    public static final String CONFIRM = "CONFIRM";
    public static final String PARTPAY = "PARTPAY";
    public static final String PAYED = "PAYED";
    public static final String COMPLETE = "COMPLETE";
    public static final String INVALID = "INVALID";
    public static final String SELLER_CONFIRMED = "SELLER_CONFIRMED";


    //    SPOT：一口价 BASIS：基差
    public static final String SPOT = "SPOT";
    public static final String BASIS = "BASIS";
    public static final String UNKOWN_ERROR = "未知错误";

    //runtime permission
    public static final int PERMISSIONS_CODE_GALLERY = 2001;

    public static final String KEY_CONTACT_PHONE = "service_phone";
    public static final String KEY_NEED_UPDATE = "need_update";
    public static final String KEY_UPDATE_LOG = "update_log";
    /**
     * Observable event name
     */
    public static final String PUSH_MESSAGE ="pushMessage_received" ;


    public static final String KEY_IDENT_DETAILS = "ident_details";
    public static final int REQUEST_PERMISSION_CALL_PHONE = 9522;
    public static final int REQUEST_PERMISSION_GROUP = 9524;
    public static final int REQUEST_PERMISSION_WRITE_EX_STORAGE = 9524;
    public static final String KEY_NOT_FIRST_OPEN_APPLICATION = "not_first_open_application";
    public static final String KEY_NOT_FIRST_GUIDE = "not_first_guide";
    public static final String KEY_JPUSH_QUOTATIONID = "j_push_quotation_id";
    public static final String KEY_H5_RESOURCE = "h5_resource";
    public static final String KEY_INCREMENT_H5 ="h5_add_increment_resource";
    public static final String LOCATION_LAT = "baidu_location_lat";
    public static final String LOCATION_LON = "baidu_location_lon";
    public static final String SERVICE_BUSY = "服务器繁忙，请稍后重试。";
    public static final String LOCATION_X = "location_x";
    public static final String LOCATION_Y = "location_y";


    public final static int PAY_EBAO_INSUFFICIENT_ERROR = 1;
    public final static int FULL_PAY_WAIT_CONFIRM_ERROR = 2;
    public final static int FULL_PAY_CONNECT_ERROR = 3;
    public final static int PAY_PASSWARD_UNCRRECT = 4;
    public final static int TOTAL_BALANCE_INSUFFICIENT_ERROR = 5;
    public static final String KEY_JPUSH_SUCCESS_FROM_NOTIFACATION = "from_notifacation_message";

    public static final String  SAVE_MESSAGE = "save_message_bundle";


    public static final String REPORT_CLEAR = "reprot_clear";//清空头寸报表的个人信息
    public static final String STRATEGY_REFRESH = "strategy_refresh";//刷新策略


    public static final int RESPONSE_SUCCEED = 0;
    public static final int RESPONSE_FAIL = 1;
    public static final int RESPONSE_ERROR = 2;
}
