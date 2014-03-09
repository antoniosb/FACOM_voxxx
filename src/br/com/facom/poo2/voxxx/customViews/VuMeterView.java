package br.com.facom.poo2.voxxx.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VuMeterView extends View {
    private int mCurrentValue = 0;
    private int mPeakValue = 0;
    private static final Paint paint = new Paint();

    // Max value returned by Recorder getMaxAmplitude
    final static int mMaxValue = 32767;

    public VuMeterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VuMeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VuMeterView(Context context) {
        super(context);
    }

    public void setValue(int value) {
        if (value == 0)
            return; // if we poll audiorecorder too fast, we have a zero value,
                    // making meter flickering

        mCurrentValue = value;

        if (value > mPeakValue)
            mPeakValue = value;
    }

    public void resetPeak() {
        mPeakValue = mCurrentValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);

        // draw meter line
        paint.setColor(0xFF99CC00);
        canvas.drawLine(0, 0, mCurrentValue * getWidth() / mMaxValue, 0, paint);

        // draw peak
        paint.setColor(0xFFCC0000);
        canvas.drawLine(mPeakValue * getWidth() / mMaxValue - 10, 0, mPeakValue
                * getWidth() / mMaxValue, 0, paint);
    }
}