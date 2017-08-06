# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\darken\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

# Glide
# https://github.com/bumptech/glide/wiki/Configuration#keeping-a-glidemodule
# http://bumptech.github.io/glide/doc/download-setup.html#proguard
#
# Note: class com.bumptech.glide.module.LibraryGlideModule is not kept.
#       Not sure if this is intentional, or if Glide devs forgot it.
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}