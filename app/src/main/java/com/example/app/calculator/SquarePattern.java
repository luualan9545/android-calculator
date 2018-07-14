package com.example.app.calculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class SquarePattern extends View {
    Shape[][] shapeArr = new Shape[17][9];
    Paint paint = new Paint();

    public SquarePattern(Context context) {
        super(context);
        for (int i = 0; i < shapeArr.length; i++) {
            for (int j = 0; j < shapeArr[i].length; j++) {
                shapeArr[i][j] = new Square(j * 120, i * 110, 80, android.graphics.Color.parseColor(Color.getRandomColor()));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Shape[] s : shapeArr) {
            for (Shape shape : s) {
                shape.draw(canvas, paint);
            }
        }
    }
}