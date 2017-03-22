package com.example.leesangyoon.wear;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class KeyboardView extends View {

    char[][] keyboardChar = {
            {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'},
            {'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'},
            {'z', 'x', 'c', 'v', 'b', 'n', 'm'}
    };

    Paint keyboardPaint;

    public KeyboardView(Context context) {
        super(context);
        init(null, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.KeyboardView, defStyle, 0);

        a.recycle();

        keyboardPaint = new Paint();
        keyboardPaint.setAntiAlias(true);
        keyboardPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        double viewWidth = getWidth();
        double viewHeight = getHeight();

        double keyWidth = (viewWidth / 11);
        double keyHeight = viewHeight / 4;

        double keyboardPaddingLeft = (viewWidth - (10 * keyWidth)) * 0.5;
        double keyboardPaddingTop = (viewHeight - (3 * keyHeight)) * 0.5;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                if (j < keyboardChar[i].length) {
                    String content = String.valueOf(keyboardChar[i][j]);

                    float leftPadding = (float) (keyboardPaddingLeft + j * keyWidth + (keyWidth * 0.5));
                    float topPadding = (float) (keyboardPaddingTop + i * keyHeight + (keyHeight * 0.5));
                    if (i == 1) {
                        leftPadding += keyWidth * 0.5;
                    } else if (i == 2) {
                        leftPadding += keyWidth * 1.5;
                    }

                    canvas.drawText(content,
                            leftPadding,
                            topPadding,
                            keyboardPaint);
                }
            }
        }

    }


}
