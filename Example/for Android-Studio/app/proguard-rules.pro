# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/CiferChan/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-dontpreverify
# Reduce the size of the output some more.
-repackageclasses ''
-allowaccessmodification
# Switch off some optimizations that trip older versions of the Dalvik VM.
-optimizations !code/simplification/arithmetic
#下面这行代码是 忽略警告，避免打包时某些警告出现
-ignorewarnings
# Keep a fixed source file attribute and all line number tables to get line
# numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable
# RemoteViews might need annotations.
-keepattributes *Annotation*
# Preserve all fundamental application classes.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View

-keep public class com.payssion.android.sdk.* {*; }
-keep public class * extends com.payssion.android.sdk.model.PayssionRequest
-keep public class * extends com.payssion.android.sdk.model.PayssionResponse
# Preserve all View implementations, their special context constructors, and
# their setters.
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    *** get*();
}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-ignorewarnings
-dontpreverify
-verbose


# Preserve all classes that have special context constructors, and the
# constructors themselves.
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
# Preserve all classes that have special context constructors, and the
# constructors themselves.
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# Preserve the special fields of all Parcelable implementations.
-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}
# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}
# Preserve the required interface from the License Verification Library
# (but don't nag the developer if the library is not used at all).
-keep public interface com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-dontwarn android.support.**
# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

#枚举
-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# PayssionSDK
-libraryjars libs/PayssionSDK.jar -keep class com.payssion.android.sdk.** {*; }
