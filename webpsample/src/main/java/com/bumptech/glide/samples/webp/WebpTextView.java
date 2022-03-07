package com.bumptech.glide.samples.webp;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * A TextView support displaying WebpDrawable in ImageSpan
 */
public class WebpTextView extends TextView {
    // save ImageSpan Drawable with callbacks
    private HashMap<Drawable, WeakReference<Drawable.Callback>> mCallBacks;

    public WebpTextView(Context context) {
        super(context);
    }

    public WebpTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WebpTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebpTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

        CharSequence nText = getText();
        if (nText instanceof Spanned) {
            if (mCallBacks == null) {
                mCallBacks = new HashMap<>();
            } else {
                mCallBacks.clear();
            }

            final ImageSpan[] spans = ((Spanned) nText).getSpans(0, nText.length(), ImageSpan.class);
            for (ImageSpan span : spans) {
                Drawable drawable = span.getDrawable();
                if (drawable == null) {
                    continue;
                }

                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }

                Drawable.Callback callback = drawable.getCallback();
                mCallBacks.put(drawable, new WeakReference<Drawable.Callback>(callback));
                drawable.setCallback(this);
            }
        }
    }

    private Drawable.Callback getCallBack(@NonNull Drawable drawable) {
        WeakReference<Drawable.Callback> callback = mCallBacks.get(drawable);
        return callback != null ? callback.get() : null;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {

        if (mCallBacks.containsKey(drawable)) {
            postInvalidate();
        } else {
            super.invalidateDrawable(drawable);
        }

        Drawable.Callback callback = getCallBack(drawable);
        if (callback != null) {
            callback.invalidateDrawable(drawable);
        }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        super.scheduleDrawable(who, what, when);

        Drawable.Callback callback = getCallBack(who);
        if (callback != null) {
            callback.scheduleDrawable(who, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        super.unscheduleDrawable(who, what);

        Drawable.Callback callback = getCallBack(who);
        if (callback != null) {
            callback.unscheduleDrawable(who, what);
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || mCallBacks.containsKey(who);
    }
}
