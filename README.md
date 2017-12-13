# GlideWebpDecoder

GlideWebpDecoder is a [Glide](https://github.com/bumptech/glide) integration library for decode and show animated webp images. It is based on [libwebp](https://github.com/webmproject/libwebp) project and takes some implementation from [Fresco](https://github.com/facebook/fresco) and [GlideWebpSupport](https://github.com/roths/GlideWebpSupport) as references.

## Dependency Integration

Library is availble in jcenter. If you are build with Gradle, simple add the following dependencies to your `build.gradle` file

```gradle
// webpdecoder
compile 'com.zlc.glide:webpdecoder:0.0.1'
// glide 
compile 'com.github.bumptech.glide:glide:4.2.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.2.0'
```

Then you are free to use GlideWebpDecoder just like use Glide library.

## Proguard

The library use native code to decode webp, so you should put the following lines to your proguard.cfg and keep the jni interface.

```pro
-keep public class com.bumptech.glide.integration.webp.WebpImage { *; }
-keep public class com.bumptech.glide.integration.webp.WebpFrame { *; }
-keep public class com.bumptech.glide.integration.webp.WebpBitmapFactory { *; }
```

## Acknowledgement

* [libwebp project](https://github.com/webmproject/libwebp)
* [Fresco](https://github.com/facebook/fresco)
* [GlideWebpSupport](https://github.com/roths/GlideWebpSupport)

## License

The Library is [Apache-2.0](https://github.com/zjupure/GlideWebpDecoder/blob/master/LICENSE), part code is [BSD-licensed](https://github.com/facebook/fresco/blob/master/LICENSE) see [Fresco](https://github.com/facebook/fresco) for detail.

