# GlideWebpDecoder

GlideWebpDecoder is a [Glide](https://github.com/bumptech/glide) integration library for decoding and displaying webp images on Android platforms. It is based on [libwebp](https://github.com/webmproject/libwebp) project and takes some implementation from [Fresco](https://github.com/facebook/fresco) and [GlideWebpSupport](https://github.com/roths/GlideWebpSupport) as references.

## Features

* play animated webp images on all Android versions
* show transparent or lossless webp images on Android devices lower than 4.2.1 (BitmapFactory support webp decode on 4.2+ android system so we have no need to handle)

## Dependency Integration

Library is available in jcenter. If you are build with Gradle, just add the following dependencies to your `build.gradle` file

```gradle
// webpdecoder
compile 'com.zlc.glide:webpdecoder:0.0.4'
// glide, 4.2.0-4.4.0
compile 'com.github.bumptech.glide:glide:4.2.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.2.0'
```

Then you are free to use GlideWebpDecoder just like use other Glide integration library.

## Proguard

The library use native code to decode webp, so you should put the following lines to your `proguard.cfg` and keep the jni interface.

```pro
-keep public class com.bumptech.glide.integration.webp.WebpImage { *; }
-keep public class com.bumptech.glide.integration.webp.WebpFrame { *; }
-keep public class com.bumptech.glide.integration.webp.WebpBitmapFactory { *; }
```

## Acknowledgement

* [libwebp](https://github.com/webmproject/libwebp)
* [Fresco](https://github.com/facebook/fresco)
* [GlideWebpSupport](https://github.com/roths/GlideWebpSupport)

## License

The Library is [Apache-2.0](https://github.com/zjupure/GlideWebpDecoder/blob/master/LICENSE), part code is [BSD-licensed](https://github.com/facebook/fresco/blob/master/LICENSE) see [Fresco](https://github.com/facebook/fresco) for detail.

