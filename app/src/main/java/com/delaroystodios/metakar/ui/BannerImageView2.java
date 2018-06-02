package com.delaroystodios.metakar.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BannerImageView2 extends ImageView {

    private Paint mRibbonPaint;
    private Paint mTextPaint;
    private float mScale;
    private String mBannerText;
    private boolean isSpecial = false;

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
        invalidate();
    }


    public BannerImageView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPainters(attrs);
    }

    public BannerImageView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPainters(attrs);
    }

    private void initPainters(AttributeSet attrs) {

        mBannerText = "ویژه";
        mRibbonPaint = new Paint();
        mRibbonPaint.setColor(Color.parseColor("#EA4C6F"));
        mRibbonPaint.setAntiAlias(true);
        mRibbonPaint.setStyle(Paint.Style.STROKE);
        mRibbonPaint.setStrokeCap(Paint.Cap.BUTT);
        mScale = getResources().getDisplayMetrics().density;
        mRibbonPaint.setStrokeWidth(dp(25));
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize( dp(15) );
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

    }


    private float dp(float dp) {
        return dp * mScale + 0.5f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ( !TextUtils.isEmpty(mBannerText) ) {
            if (isSpecial) {
            canvas.drawLine(-dp(16), dp(64), dp(64), -dp(16), mRibbonPaint);
            canvas.save();
            canvas.rotate(-45, 0, 0);
            canvas.drawText(mBannerText, -dp(16), dp(38), mTextPaint);
            canvas.restore();
            }


        }
    }


}