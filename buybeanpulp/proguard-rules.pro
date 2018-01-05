# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/apple/Documents/Development/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn javax.servlet.**
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes *Annotation*

-keep public class * extends android.util.FloatMath
-keep class android.support.v4.**
-dontwarn android.support.v4.**
-keep class android.support.v7.**
-dontwarn android.support.v7.**
-keep class com.tangdong.sa.**
-dontwarn com.tangdong.sa.**

-keep class android.util.**
-dontwarn android.util.**
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.Notification
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View {
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
     public void set*(...);
     public void get*(...);
 }

-keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);

 }

 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);

 }

 -keepclasseswithmembers class * {
    public <init>(android.content.Context);
 }

 -keepclassmembers class * implements android.os.Parcelable {
     static android.os.Parcelable$Creator CREATOR;
 }

 -keepclasseswithmembernames class * {
     native <methods>;
 }

 -keep public class * implements java.io.Serializable {
         public *;
         static final long serialVersionUID;
         private static final java.io.ObjectStreamField[] serialPersistentFields;
         private void writeObject(java.io.ObjectOutputStream);
         private void readObject(java.io.ObjectInputStream);
         java.lang.Object writeReplace();
         java.lang.Object readResolve();
 }

 -keepclassmembers class * implements java.io.Serializable {

     static final long serialVersionUID;

     private static final java.io.ObjectStreamField[] serialPersistentFields;

     private void writeObject(java.io.ObjectOutputStream);

     private void readObject(java.io.ObjectInputStream);

     java.lang.Object writeReplace();

     java.lang.Object readResolve();

 }

-keep class com.baidu.** { *; }
#混淆后导致地图不可见
-keep class com.baidu.mapapi.map.MapView{*;}
#混淆后导致地图缩放地图显示问题
-keep class com.baidu.mapapi.map.MapController{*;}
-keep class vi.com.gdi.bgl.android.**{*;}
-dontwarn com.baidu.mapapi.**
-dontwarn com.weibo.net.**
-dontskipnonpubliclibraryclassmembers
-dontwarn android.webkit.**
-dontwarn org.codehaus.jackson.jaxrs.**

-keepattributes Signature
#新浪微博
-keep class com.sina.**{*;}
-dontwarn com.sina.**
#微信
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#扫描二维码
-keep class java.awt.image.** { *;}
-dontwarn java.awt.image.**
-keep class java.awt.** { *;}
-dontwarn java.awt.**
-keep class java.awt.geom.** { *;}
-dontwarn java.awt.geom.**
-keep class javax.imageio.** { *;}
-dontwarn javax.imageio.**
-keep class javax.swing.** { *;}
-dontwarn javax.swing.**
-dontwarn com.google.zxing.**

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**


-dontshrink
-dontoptimize

#不混淆捕获异常类，便于后期找错
-keep public class com.changcai.buyer.CrashHandler{*;}

-dontwarn au.com.bytecode.opencsv.**
-keep class au.com.bytecode.opencsv.** { *;}
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *;}
-dontwarn com.baidu.android.pushservice.**
-keep class com.baidu.android.pushservice.** { *;}
-dontwarn com.baidu.frontia.**
-keep class com.baidu.frontia.** { *;}
-dontwarn com.slidingmenu.lib.app.**
-keep class com.slidingmenu.lib.app.** { *;}
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}
-dontwarn com.android.volley.**
-keep class com.android.volley.** { *;}
-dontwarn org.apache.http.**
-keep class org.apache.http.** { *;}
-dontwarn org.apache.http.**
-keep class org.apache.http.** { *;}



#webview js
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
#友盟更新
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }
-keep class com.ta.utdid2.** { *; }
-keep class com.ut.device.** { *; }
-dontwarn com.umeng.update.**
-dontwarn com.umeng.**
#友盟
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep public class com.changcai.buyer.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#百度推送
-dontwarn com.baidu.**

#butterknife混淆
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

#支付宝混淆
-keep class com.alipay.android.app.IALiPay{*;}
-keep class com.alipay.android.app.IALixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}

-dontwarn com.alipay.android.app.**
-dontwarn com.alipay.**

#极光推送
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class com.google.** { *; }

# 分享相关
-dontwarn com.umeng.socialize.**
-keep class com.umeng.socialize.** {*;}

# 微信
-keep class com.tencent.mm.sdk.** {*;}
# QQ
-dontwarn com.tencent.**
-keep class com.tencent.** {*;}
-keep class com.tencent.stat.** {*;}
#微博
-dontwarn com.sina.**
-keep class com.sina.** {*;}
# 分享相关

# 禁用混淆的类
-keep interface com.changcai.buyer.IKeepFromProguard
-keep class * implements com.changcai.buyer.IKeepFromProguard {*;}
# 禁用混淆的类 end


# rxjava
-dontwarn sun.misc.Unsafe

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}
# rxjava end
-dontwarn retrofit2.**
-keep class retrofit2.**{*;}

# rxdownload start
-dontwarn zlc.season.rxdownload.**
-keep public class zlc.season.rxdownload.**{*;}
#rxdownload end
# tencent webview broser

#baidu start

-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
#end baidu

#pickerview start
-keep class com.bruce.pickerview.**{*;}
-dontwarn com.bruce.pickerview.**
#pickerview end

#pulltoRefreshlibrary start
-keep class com.jingchen.pulltorefresh.**{*;}
-dontwarn com.jingchen.pulltorefresh.**
#pulltoRefreshlibrary end


#权限
-keep class kr.co.namee.permissiongen.**{*;}
-dontwarn kr.co.namee.permissiongen.**
-keepclassmembers class ** {
    public void doSomething*();
}
-keep public class * extends android.support.v4.app.Fragment {
    private void permission*();
}

#竖滑viewpager
-keep class fr.castorflex.android.verticalviewpage.**{*;}
-dontwarn fr.castorflex.android.verticalviewpage.**


#x5 webview start

#-optimizationpasses 7
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontwarn dalvik.**
#-overloadaggressively

#@proguard_debug_start
# ------------------ Keep LineNumbers and properties ---------------- #
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-renamesourcefileattribute TbsSdkJava
-keepattributes SourceFile,LineNumberTable
#@proguard_debug_end

# --------------------------------------------------------------------------
# Addidional for x5.sdk classes for apps

-keep class com.tencent.smtt.export.external.**{
    *;
}

-keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
	*;
}

-keep class com.tencent.smtt.sdk.CacheManager {
	public *;
}

-keep class com.tencent.smtt.sdk.CookieManager {
	public *;
}

-keep class com.tencent.smtt.sdk.WebHistoryItem {
	public *;
}

-keep class com.tencent.smtt.sdk.WebViewDatabase {
	public *;
}

-keep class com.tencent.smtt.sdk.WebBackForwardList {
	public *;
}

-keep public class com.tencent.smtt.sdk.WebView {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
	public static final <fields>;
	public java.lang.String getExtra();
	public int getType();
}

-keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$PictureListener {
	public <fields>;
	public <methods>;
}


-keepattributes InnerClasses

-keep public enum com.tencent.smtt.sdk.WebSettings$** {
    *;
}

-keep public enum com.tencent.smtt.sdk.QbSdk$** {
    *;
}

-keep public class com.tencent.smtt.sdk.WebSettings {
    public *;
}


-keep public class com.tencent.smtt.sdk.ValueCallback {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebViewClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
	public <fields>;
	public <methods>;
}

-keep class com.tencent.smtt.sdk.SystemWebChromeClient{
	public *;
}
# 1. extension interfaces should be apparent
-keep public class com.tencent.smtt.export.external.extension.interfaces.* {
	public protected *;
}

# 2. interfaces should be apparent
-keep public class com.tencent.smtt.export.external.interfaces.* {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebIconDatabase {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebStorage {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
	public <fields>;
	public <methods>;
}
-keep public class com.tencent.smtt.sdk.CookieSyncManager {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.Tbs* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.LogFileUtils {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLog {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLogClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.CookieSyncManager {
	public <fields>;
	public <methods>;
}

# Added for game demos
-keep public class com.tencent.smtt.sdk.TBSGamePlayer {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.Apn {
	public <fields>;
	public <methods>;
}
# end


-keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
	public <fields>;
	public <methods>;
}

-keep class MTT.ThirdAppInfoNew {
	*;
}

-keep class com.tencent.mtt.MttTraceEvent {
	*;
}

# Game related
-keep public class com.tencent.smtt.gamesdk.* {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.TBSGameBooter {
        public <fields>;
        public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {
	public protected *;
}

-keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {
	public *;
}
#---------------------------------------------------------------------------


#------------------  下方是android平台自带的排除项，这里不要动         ----------------

#-keep public class * extends android.app.Activity{
#	public <fields>;
#	public <methods>;
#}
#-keep public class * extends android.app.Application
#{
#	public <fields>;
#	public <methods>;
#}
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference

#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}

#-keepclasseswithmembers class * {
#	public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
#	public <init>(android.content.Context, android.util.AttributeSet, int);
#}

#-keepattributes *Annotation*

#-keepclasseswithmembernames class *{
#	native <methods>;
#}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#------------------  下方是共性的排除项目         ----------------
# 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

-keepclasseswithmembers class * {
    ... *JNI*(...);
}

-keepclasseswithmembernames class * {
	... *JRI*(...);
}

-keep class **JNI* {*;}

#x5 webview end


#网易云讯 start
### APP 3rd party jars
-dontwarn com.amap.**
-keep class com.amap.** {*;}
-dontwarn com.baidu.location.**
-keep class com.baidu.location.** {*;}
-dontwarn com.autonavi.amap.**
-keep class com.autonavi.amap.** {*;}
-dontwarn com.alibaba.**
-keep class com.alibaba.fastjson.** {*;}

### APP 3rd party jars(xiaomi push)
-dontwarn com.xiaomi.push.**
-keep class com.xiaomi.** {*;}
### APP 3rd party jars(huawei push)

-ignorewarning
-keepattributes Exceptions
-keepattributes Signature
# hmscore-support: remote transport
-keep class * extends com.huawei.hms.core.aidl.IMessageEntity { *; }
# hmscore-support: remote transport
-keepclasseswithmembers class * implements com.huawei.hms.support.api.transport.DatagramTransport {
<init>(...); }
# manifest: provider for updates
-keep public class com.huawei.hms.update.provider.UpdateProvider { public *; protected *; }

### APP 3rd party jars(glide)
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

### org.json xml
-dontwarn org.json.**
-keep class org.json.**{*;}

### face unity
-keep class com.faceunity.auth.AuthPack {*;}

### jrmf wx
-dontwarn com.jrmf360.**
-keep class com.jrmf360.** {*;}
#微信
-dontwarn com.switfpass.pay.**
-keep class com.switfpass.pay.**{*;}
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}
#支付宝
-dontwarn com.alipay.**
-keep class com.alipay.**{*;}
-dontwarn org.json.alipay.**
-keep class org.json.alipay.**{*;}
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
#okio
-dontwarn okio.**
-keep class okio.**{*;}

### glide 3
-keepnames class com.netease.nim.uikit.support.glide.NIMGlideModule

### glide 4
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

### NIM SDK
-dontwarn com.netease.**
-keep class com.netease.** {*;}
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}
-keep class net.sqlcipher.** {*;}



-keep class **.R$* {
 *;

}
#网易云讯 end