package com.saurabh.doodle.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.saurabh.doodle.R;

import java.util.ArrayList;

public class DoodleCanvas extends View {

    private ArrayList<Paint> paintPenList =new ArrayList<>();
    private ArrayList<Path> pathPenList =new ArrayList<>();
    private ArrayList<Paint> removedPaintPenList =new ArrayList<>();
    private ArrayList<Path> removedPathPenList =new ArrayList<>();
    private Path latestPath;
    private Paint latestPaint;
    private int lineWidth =15;
    private static int DEFAULT_COLOR;
    private int currentColor;
    private GetButtonCallback callbackForButton;


    public DoodleCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        DEFAULT_COLOR= ContextCompat.getColor(getContext(), R.color.colorAccent);
        currentColor=DEFAULT_COLOR;

        initPaintPen(currentColor);
    }

    private void initPaintPen(int color){
        latestPaint=getNewPaintPen(color);
        latestPath=getNewPathPen();

        paintPenList.add(latestPaint);
        pathPenList.add(latestPath);
    }

    private Path getNewPathPen(){
        Path path=new Path();
        return path;
    }

    private Paint getNewPaintPen(int color){
        Paint mPaintPen =new Paint();

        mPaintPen.setStrokeWidth(lineWidth);
        mPaintPen.setAntiAlias(true);
        mPaintPen.setDither(true);
        mPaintPen.setStyle(Paint.Style.STROKE);
        mPaintPen.setStrokeJoin(Paint.Join.MITER);
        mPaintPen.setStrokeCap(Paint.Cap.ROUND);
        mPaintPen.setColor(color);

        return mPaintPen;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i=0;i<paintPenList.size();i++){
            canvas.drawPath(pathPenList.get(i),paintPenList.get(i));
        }
        callbackForButton.undoStatus(pathPenList.size()!=1);
        callbackForButton.redoStatus(removedPathPenList.size()!=0);
        super.onDraw(canvas);
    }

    public void setCallback(GetButtonCallback callback){
        this.callbackForButton=callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startPath(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                updatePath(x,y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();

        return true;
    }

    private void startPath(float x, float y) {
        initPaintPen(currentColor);
        latestPath.moveTo(x,y);
    }

    private void updatePath(float x, float y) {
        latestPath.lineTo(x,y);
    }

    public void undoView(){
        if (paintPenList.size()>1){
            latestPaint=paintPenList.get(paintPenList.size()-2);
            latestPath=pathPenList.get(pathPenList.size()-2);

            removedPaintPenList.add(paintPenList.get(paintPenList.size()-1));
            removedPathPenList.add(pathPenList.get(pathPenList.size()-1));

            paintPenList.remove(paintPenList.size()-1);
            pathPenList.remove(pathPenList.size()-1);

            currentColor=latestPaint.getColor();
            lineWidth= (int) latestPaint.getStrokeWidth();
        }
        invalidate();
    }

    public void redoView(){
        if (removedPathPenList.size()>=1){
            latestPaint=removedPaintPenList.get(removedPaintPenList.size()-1);
            latestPath=removedPathPenList.get(removedPathPenList.size()-1);

            paintPenList.add(latestPaint);
            pathPenList.add(latestPath);

            removedPaintPenList.remove(removedPaintPenList.size()-1);
            removedPathPenList.remove(removedPathPenList.size()-1);

            currentColor=latestPaint.getColor();
            lineWidth= (int) latestPaint.getStrokeWidth();
        }
        invalidate();
    }

    public interface GetButtonCallback{
        void undoStatus(boolean status);
        void redoStatus(boolean status);
    }
}
