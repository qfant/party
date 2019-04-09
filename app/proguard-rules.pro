# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\chenxi.cui\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.gyf.barlibrary.* {*;}
-keep class com.facebook.* {*;}
-dontwarn com.facebook.**
-keep class com.squareup.* {*;}
-dontwarn com.squareup.**
-keep class okhttp3.* {*;}
-dontwarn okhttp3.**
-keep class javax.annotation.* {*;}
-dontwarn javax.**
-keep class com.alibaba.* {*;}
-dontwarn com.alibaba.**
-dontwarn retrofit2.**
-dontwarn okio.**
-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}
