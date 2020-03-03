# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#databinding混淆
-keep class android.databinding.** { *; }
#aspectjx混淆
-keep class com.jy.commonlibrary.aspect.** { *; }
#反射机制
-keep class com.jy.baselibrary.utils.ObjectUtils
-keepclassmembers class com.jy.baselibrary.utils.ObjectUtils {
  public *;
}

#协程
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.flow.**inlined**


#Rxbus混淆
-dontwarn com.jy.commonlibrary.rxbus.**
-keep class com.jy.commonlibrary.rxbus.** { *;}
-keepattributes *Annotation
-keep @com.jy.commonlibrary.rxbus.Subscribe class * {*;}
-keep class * {
    @com.jy.commonlibrary.rxbus.Subscribe <fields>;
}
-keepclassmembers class * {
    @com.jy.commonlibrary.rxbus.Subscribe <methods>;
}
#Rxbus混淆end


#********************************第三方混淆*************************************

#微信
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
#微信end

#支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
-renamesourcefileattribute SourceFile
-keep class android.net.http.SslError
-keep class android.webkit.** {*;}
-keep class com.alipay.sdk.** {*; }
#支付宝end


#qq分享
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-dontwarn com.baidu.**
-dontwarn com.tencent.**
#qq分享 end

#微信分享
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#微信分享 end

#glide#
#glide4.0
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# 从glide4.0开始，GifDrawable没有提供getDecoder()方法，
# 需要通过反射获取gifDecoder字段值，所以需要保持GifFrameLoader和GifState类不被混淆
-keep class com.bumptech.glide.load.resource.gif.GifDrawable$GifState{*;}
-keep class com.bumptech.glide.load.resource.gif.GifFrameLoader {*;}
#glide end#

#********************************第三方混淆 end*************************************



#********************************APP混淆*************************************

#不混淆资源类
-keep public class [com.jy.simple].R$*{
    public static final int *;
}
#一般model最好避免混淆
-keep class com.jy.simple.http.bean.** { *; }
-keep class com.jy.commonlibrary.http.bean.** { *; }
-keep class com.jy.sociallibrary.wx.WXPayBean