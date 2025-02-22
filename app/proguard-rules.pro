# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes LineNumberTable,SourceFile

-ignorewarnings

#google
-keep public class com.google.** {*;}

#in-app billing
-keep class com.android.vending.billing.**

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#retrofit
-keep class com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn rx.**

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

#facebook
-keep class com.facebook.** {
   *;
}

# Google Clound message
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.

#Progress button
-keep class com.dd.** { *; }

#Gson
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers enum * { *; }

#rx
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#for eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#end for eventbus

#for realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-dontwarn javax.**
-dontwarn io.realm.**
#for realm

##for loading view
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }
##for loading view

# Basic ProGuard rules for Firebase Android SDK 2.0.0+
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**

##pdf view
-keep class com.shockwave.**

#renderscript
-dontwarn androidx.renderscript.*
-keepclassmembers class androidx.renderscript.RenderScript {
  native *** rsn*(...);
  native *** n*(...);
}

#https://github.com/mmin18/RealtimeBlurView
-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }

#-keep class com.veyo.** { *; }
#-keep interface com.veyo.** { *; }

-keep class com.huxq17.download.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keepclassmembers class com.dd.StrokeGradientDrawable {
    public void setStrokeColor(int);
}

-keep class com.google.android.gms.ads.identifier.** { *; }


##glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

##jsoup
-keep public class org.jsoup.** {
public *;
}

# Preserve all Dexter classes and method names

#-keepattributes InnerClasses, Signature, *Annotation*
#-keep class com.karumi.dexter.** { *; }
#-keep interface com.karumi.dexter.** { *; }
#-keepclasseswithmembernames class com.karumi.dexter.** { *; }
#-keepclasseswithmembernames interface com.karumi.dexter.** { *; }

# uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
-dontwarn com.app.imagepickerlibrary.BR
-dontwarn com.daimajia.androidanimations.library.Techniques
-dontwarn com.daimajia.androidanimations.library.YoYo$AnimationComposer
-dontwarn com.daimajia.androidanimations.library.YoYo$AnimatorCallback
-dontwarn com.daimajia.androidanimations.library.YoYo$YoYoString
-dontwarn com.daimajia.androidanimations.library.YoYo

-ignorewarnings

#google
-keep public class com.google.** {*;}

#in-app billing
-keep class com.android.vending.billing.**

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#retrofit
-keep class com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn rx.**

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

#facebook
-keep class com.facebook.** {
   *;
}

# Google Clound message
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.

#Progress button
-keep class com.dd.** { *; }

#Gson
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers enum * { *; }

#rx
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#for eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#end for eventbus

#for realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-dontwarn javax.**
-dontwarn io.realm.**
#for realm

##for loading view
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }
##for loading view

# Basic ProGuard rules for Firebase Android SDK 2.0.0+
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**

##pdf view
-keep class com.shockwave.**

#renderscript
-dontwarn androidx.renderscript.*
-keepclassmembers class androidx.renderscript.RenderScript {
  native *** rsn*(...);
  native *** n*(...);
}

#https://github.com/mmin18/RealtimeBlurView
-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }

#-keep class com.veyo.** { *; }
#-keep interface com.veyo.** { *; }

-keep class com.huxq17.download.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keepclassmembers class com.dd.StrokeGradientDrawable {
    public void setStrokeColor(int);
}

-keep class com.google.android.gms.ads.identifier.** { *; }


##glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

##jsoup
-keep public class org.jsoup.** {
public *;
}

# Preserve all Dexter classes and method names

#-keepattributes InnerClasses, Signature, *Annotation*
#-keep class com.karumi.dexter.** { *; }
#-keep interface com.karumi.dexter.** { *; }
#-keepclasseswithmembernames class com.karumi.dexter.** { *; }
#-keepclasseswithmembernames interface com.karumi.dexter.** { *; }

# uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#https://github.com/mmin18/RealtimeBlurView
-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }

#-keep class com.veyo.** { *; }
#-keep interface com.veyo.** { *; }


-keepclassmembers class com.dd.StrokeGradientDrawable {
    public void setStrokeColor(int);
}

-keep class com.google.android.gms.ads.identifier.** { *; }

