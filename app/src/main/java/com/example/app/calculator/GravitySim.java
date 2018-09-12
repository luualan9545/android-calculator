package com.example.app.calculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.drawable.*;
import com.util.Color;
import com.util.Gravity;
import com.util.Utility;

public class GravitySim extends View implements Runnable {
    private Drawable[] shapeArr = {
            new Circle(100, 100, 40, Color.parseColor("#33c446"), 2, 0),
            new Circle(200, 100, 40, Color.RED, 2, 0),
            new Square(300, 100, 50, Color.MAGENTA),
            new RightTriangle(100, 100, new double[]{75, 75}, Color.parseColor("#0675d1"), 2, 0)
    };
    private Paint paint = new Paint();
    private long start = System.currentTimeMillis();
    private float distance = 0;
    private int count = 0;
    private Gravity gravityManager;

    public GravitySim(Context context) {
        super(context);
        gravityManager = new Gravity(shapeArr, 0.1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        long start;
        long stop;

        if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE) {
            start = System.currentTimeMillis();
            if (this.count < 1) this.start = start;
            for (Drawable d : shapeArr) {
                d.setX(x);
                d.setY(y);
                if (d instanceof ComplexDrawable) {
                    ((ComplexDrawable) d).setX2(d.getX() + (((ComplexDrawable) d).getX2() - d.getX()));
                    ((ComplexDrawable) d).setY2(d.getY() + (((ComplexDrawable) d).getY2() - d.getY()));
                }
            }
            if (e.getAction() == MotionEvent.ACTION_MOVE) {
                this.distance += 1000;
            }
            this.count++;
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            stop = System.currentTimeMillis();
            long timeElapsed = stop - this.start + 1000;
            int temp = Utility.getRandomInt(1, 10);
            for (Drawable d : shapeArr) {
                if (temp > 5) {
                    d.setXVelocity(this.distance / timeElapsed);
                    temp = Utility.getRandomInt(1, 5);
                } else {
                    d.setXVelocity(-(this.distance / timeElapsed));
                    temp = Utility.getRandomInt(6, 10);
                }
                d.setYVelocity(Utility.getRandomInt(1, 10) > 5 ? this.distance / timeElapsed : -(this.distance / timeElapsed));
            }
            this.distance = 0;
            this.count = 0;
        }
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Drawable d : shapeArr) {
            d.draw(canvas, paint);
            gravityManager.start(getWidth(), getHeight());
        }
    }

    @SuppressWarnings({"InfiniteLoopStatement", "EmptyCatchBlock"})
    @Override
    public void run() {
        for (;;) {
            postInvalidate();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {}
        }
    }
}