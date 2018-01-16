package com.changcai.buyer.common;

/**
 * @author zhoujun
 * @version 1.0
 * @description url地址
 * @date 2014年6月22日 下午3:43:59
 */
public class Urls {
    //    public static final String BASE_URL = "http://139.196.218.32:8080/mobile/";//新架构
//    public static  final  String BASE_URL = "http://192.168.2.137:8080/mobile/";//小灰灰
//    public static final String BASE_URL = "http://192.168.2.17:8080/mobile/";//袁琴本机ip
    public static final String BASE_URL = "http://192.168.5.52:8080/mobile/";//UAT环境
//    public static final String BASE_URL = "http://192.168.5.15:8080/mobile/";//测试
//    public static final String BASE_URL = "http://139.224.219.149:8080/mobile/";//生产环境


    public static final String BACK_SERVER = "http://139.196.209.147:8080/monitor/initApp.action";
    /**
     * 登录注册模块
     */
    public static final String USER_LOGIN = BASE_URL + "login.do";
    public static final String REGISTER = BASE_URL + "register.do";
    public static final String SEND_VERIFY_CODE = BASE_URL + "sendVerifyCode.do";
    public static final String CHECK_VERIFY_CODE = BASE_URL + "checkVerifyCode.do";

    public static final String SEND_FORGET_PWD = BASE_URL + "sendForgetPwd.do";
    public static final String CHECK_FORGET_PWD = BASE_URL + "checkForgetPwd.do";
    public static final String UPDATE_PWD_BY_FORGET = BASE_URL + "updatePwdByForget.do";

    /**
     * 资讯
     */
    public static final String GET_INFO_TAB = BASE_URL + "getInfoTab.do";
    public static final String GET_INFO_RECOMMEND = BASE_URL + "getInfoRecommend.do";//获取轮播图
    public static final String GET_ARTICLE_LIST = BASE_URL + "getArticleList.do";

    /**
     * 报价
     */
    public static final String GET_PRODUCT_FILTER = BASE_URL + "getProductFilter.do";//获取报价筛选条件
    public static final String GET_META_REGION = BASE_URL + "getMetaRegion.do";//获取交货地点
    public static final String GET_PRODUCT_LIST = BASE_URL + "getProductList.do";//获取报价列表
    public static final String GET_PRODUCT_DETAIL = BASE_URL + "getProductDetail.do";//获取产品报价详情页信息
    public static final String CREATOR_ORDER = BASE_URL + "createOrder.do";//下单接口

    /**
     * 我的
     */
    public static final String GET_MY_SPACE_STATISTIC = BASE_URL + "getMySpaceStatistic.do";//获取个人中心概要统计信息
    public static final String GET_ORDER_LIST = BASE_URL + "getOrderList.do";//获取订单列表
    public static final String GET_ORDER_INFO = BASE_URL + "getOrderInfo.do";//获取订单详情
    public static final String GET_DELIVERY_LIST = BASE_URL + "getDeliveryList.do";//获取提货单列表
    public static final String GET_DELIVERY_INFO = BASE_URL + "getDeliveryInfo.do";//获取提货单详情

    public static final String GET_MESSAGE_LIST = BASE_URL + "getMsgList.do";//我的消息列表
    public static final String READ_MESSAGE = BASE_URL + "readMsg.do";//我的消息列表
    public static final String SET_MY_DEVICEID =BASE_URL+ "setMyDeviceid";//上传我的设备
    public static final String GET_MY_EXT = BASE_URL + "getMyExt.do";//获取推送设置
    public static final String SET_MY_EXT = BASE_URL + "setMyExt.do";//推送设置


    /**
     * 身份认证
     */

    public static final String POST_COMMIT_INTRODUCE = BASE_URL + "updateEnterIntroduce.do";
    public static final String ASSIGN_PLATFORM_CONTRACT = BASE_URL + "signSiteEnterContract.do";

    /**
     * restful api
     * 身份认证
     */
    public static final String UPLOAD_FILE = "uploadFile.do";
    public static final String DO_IDENTITY = "doIdentity.do";
    public static final String UPDATE_ENTER_INTRODUCE = "updateEnterIntroduce.do";
    public static final String SET_PASSWORD = "setPayPass.do";
    public static final String SEND_SMS_CODE = "sendSmsResetPayPass.do";
    public static final String RESET_PAY_PASSWORD = "resetPayPass.do";
    public static final String REFRESH_USERINFO = "refreshUserInfo.do";
    public static final String IDENTITY_DETAILD = "getIdentityDetails.do";
    public static final String INIT = "init.do";
    public static final String GETMYPUSHTAGS="getMyPushTags";
    public static final String GET_SUMMARY="getCalendarSummaryByDay.do";
    public static final String GET_DOMAINSANDTYPE="getDomainsAndType";
    public static final String GET_ALLQUOTE="getAllQuote";
    public static final String GET_SALES_AMOUNT="getSalesAmount";
    public static final String GET_SALES_AMOUNT_ITEM="getSalesAmountItem";
    public static final String GET_IS_EXIST="isExist";
    /**
     * 易宝pay - - - - - - start
     */
    public static final String GET_EBAO_BALANCE="getEbaoAccBalanceInfo";
    public static final String VALIDATE_PAY_PASS="validatePaypass";
    public static final String EBAO_RECHARGE="ebaoReCharge";
    public static final String GET_ID_TYPE="getIdType";

    /**
     * 2.2.0 下单验证优化
     */
    public static final String VALIDCREATEORDER = "validCreateOrder.do";
    public static  final  String CREATEORDER = "createOrder.do";


    /**
     * 5.3   获取订单详情
     */
    public static  final  String GET_ORDER_INFO_REST = "getOrderInfo.do";

    /**
     * 买卖双方签合同 - - - 签约
     */

    public static final String SIGN_CONTRACT  = "signContract.do";
    public static  final String PREVIEW_SIGN_CONTRACT = "previewContract.do";


    /**
     * 易宝full_pay
     */
    public static final String EBAO_FULL_PAY ="ebaoFullPay";

    /**
     * 买家同意直接打款给卖家
     */
    public static final String DIRECT_PAY = "buyerAgreeFastPay.do";

    /**
     * 获取更多筛选条件
     */

    public static final String MORE_FILTRATE = "getMoreFilter.do";
    public static final String USER_ACCOUNT = "getUserAccountList.do";
    public static final String PAY_DELIVERY = "payOrdDelivery.do";

    public static final String PAY_FRONT_MONEY ="payFrontMoney.do" ;


    /**
     * 策略
     */
    public static  final  String STRATEGY_INIT = "strategyInit";
    public static  final  String GETARBITRAGESTRATEGY = "getArbitrageStrategy";
    public static  final  String GETSPOTSTRATEGY = "getSpotStrategy";

    /**
     * 获取APP首页报价资源
     */
    public static final String  GET_QUOTE_PRICE_FOR_APP = "getQuotePriceForApp";
    /**
     * 获取APP首页公告
     */
    public static final String GET_NOTICE = "getNotice";
    /**
     *获取APP首页推荐
     */
    public static final String  GET_RECOMMEND="getRecommend";

    /**
     * im即使通讯
     */
    //获取顾问信息
    public static final String GET_COUNSELORS="getCounselors";
    //获取会员等级信息
    public static final String GET_USER_LEVEL="getUserLevel";
    //更新会员等级信息
    public static final String UPDATE_COUNSELOR="updateCounselor";
    public static final String GET_IM_TEAMS="getImTeams";
}
