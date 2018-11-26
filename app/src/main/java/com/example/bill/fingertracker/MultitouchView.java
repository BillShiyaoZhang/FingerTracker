package com.example.bill.fingertracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MultitouchView extends View {

    private SparseArray<CountedPoint> mActivePointers;
    private Paint mPaint;
    private ColorArraySet colorsL;
    private static final int[] COLORS = {Color.BLUE, Color.GREEN, Color.BLACK, Color.CYAN, Color.GRAY};
    private CountedPoint[] points;
    private boolean status = false;
    private FileOutputStream fileOutputStream;

    public MultitouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private void initView() {
        mActivePointers = new SparseArray<CountedPoint>();
        mPaint = new Paint();
        points = new CountedPoint[mActivePointers.size()];
        colorsL = new ColorArraySet();
        fileOutputStream = null;
    }

    class ColorArraySet {
        private int[] colors;

        public ColorArraySet() {
            colors = COLORS.clone();
        }

        int get() {
            int result = colors[0];
            for (int length = colors.length, i = 0; i < length - 1; i++) {
                colors[i] = colors[i + 1];
            }
            return result;
        }

        void reset() {
            colors = COLORS.clone();
        }

        void remove(int color) {
            for (int length = colors.length, i = 0; i < length; i++) {
                if (colors[i] == color) {
                    for (int j = i; j < length - 1; j++) {
                        colors[j] = colors[j + 1];
                    }
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        points = new CountedPoint[mActivePointers.size()];

        for (int size = mActivePointers.size(), i = 0; i < size; i++) {
            CountedPoint point = mActivePointers.valueAt(i);
            points[i] = point;
            if (point != null)
                mPaint.setColor(points[i].color);
            canvas.drawCircle(point.point.x, point.point.y, 100, mPaint);
            if (status) {
                // try FileOutputStream
                if (fileOutputStream == null) {
                    try {
                        fileOutputStream = getContext().openFileOutput(((Button) findViewById(R.id.button_person)).getText() + ((Button) findViewById(R.id.button_task)).getText().toString(), Context.MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                // store data
                try {
                    fileOutputStream.write((point.point.x + "," + point.point.y + ",").getBytes());
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // close FileOutputStream
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    class CountedPoint {
        boolean status;
        PointF point;
        int color;

        public CountedPoint(PointF p) {
            status = false;
            point = p;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                CountedPoint cp = new CountedPoint(f);
                cp.color = colorsL.get();
                mActivePointers.put(pointerId, cp);
                if (fileOutputStream == null) {
                    try {
                        fileOutputStream = getContext().openFileOutput(((Button) findViewById(R.id.button_person)).getText() + ((Button) findViewById(R.id.button_task)).getText().toString(), Context.MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    CountedPoint point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.point.x = event.getX(i);
                        point.point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);
                colorsL.reset();
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    CountedPoint point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        colorsL.remove(point.color);
                    }
                }
                break;
            }
            default:
                break;
        }
        invalidate();
        return true;
    }

}
