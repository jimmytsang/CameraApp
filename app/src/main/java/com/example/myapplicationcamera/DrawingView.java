package com.example.myapplicationcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {

    public int color = Color.BLACK;
    private float width = 4f;
    private List<Holder> holderList = new ArrayList<Holder>();
    public boolean isErasemode = false;

    private class Holder {
        Path path;
        Paint paint;

        Holder(int color, float width) {
            path = new Path();
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(width);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            if (color == Color.TRANSPARENT) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                paint.setAlpha(0x00);
            }
        }
    }

    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        holderList.add(new Holder(Color.BLACK, width));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Holder holder : holderList) {
            canvas.drawPath(holder.path, holder.paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                holderList.add(new Holder(color,width));
                holderList.get(holderList.size() - 1).path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                holderList.get(holderList.size() - 1).path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void onEraser(){
        if(!isErasemode){
            isErasemode = true;
            this.color = Color.TRANSPARENT;
        }else{
            isErasemode = false;
            this.color = Color.BLACK;
        }
    }

//    public void onEraser()
//    {
//        isErasemode = !isErasemode;
//        if (isErasemode) {
//            color = android.R.color.transparent;
////                    .setColor(getResources().getColor(android.R.color.transparent));
////            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//
//        }
//        else {
//            color = Color.BLACK;
////            drawPaint.setColor(mPaint.getColor());
////            drawPaint.setXfermode(null);
//        }
//    }

//    public void onClickUndo() {
//        if (paths.size() > 0) {
//            undonePaths.add(paths.remove(paths.size() - 1));
//            invalidate();
//        } else {
//            //Util.Imageview_undo_redum_Status=false;
//        }
//        //toast the user
//    }
//
//    public void onClickRedo() {
//        if (undonePaths.size() > 0) {
//            paths.add(undonePaths.remove(undonePaths.size() - 1));
//            invalidate();
//        } else {
//            // Util.Imageview_undo_redum_Status=false;
//        }
//        //toast the user
//    }

    public void resetPaths() {
        for (Holder holder : holderList) {
            holder.path.reset();
        }
        invalidate();
    }

    public void setBrushColor(int color) {
        this.color = color;
    }

    public void setWidth(float width) {
        this.width = width;
    }


//    private List<Holder> holderList = new ArrayList<Holder>();
//
//    private int color = Color.BLACK;
//    private float width = 4f;
//
//    private class Holder {
//        Path path;
//        Paint paint;
//
//        Holder(int color, float width) {
//            path = new Path();
//            paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setStrokeWidth(width);
//            paint.setColor(color);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeJoin(Paint.Join.ROUND);
//            paint.setStrokeCap(Paint.Cap.ROUND);
//        }
//    }
//
//    private void init() {
//        holderList.add(new Holder(color, width));
//    }
//
//    //drawing path
//    public Path drawPath = new Path();
//    //drawing and canvas paint
//    private Paint drawPaint = new Paint();
//    //initial color
//    private int paintColor = 0xFF660000;
//    //canvas
//    private Canvas drawCanvas;
//    //canvas bitmap
//    private Bitmap canvasBitmap;
//
//
//    public DrawingView(Context context){
//        super(context);
//        setupDrawing();
//    }
//
//    //setup drawing
//    private void setupDrawing(){
//
//        //prepare for drawing and setup paint stroke properties
//        drawPaint.setColor(paintColor);
//        drawPaint.setAntiAlias(true);
//        drawPaint.setStrokeWidth(15.0f);
//        drawPaint.setStyle(Paint.Style.STROKE);
//        drawPaint.setStrokeJoin(Paint.Join.ROUND);
//        drawPaint.setStrokeCap(Paint.Cap.ROUND);
//        init();
//    }
//
//    //size assigned to view
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        drawCanvas = new Canvas(canvasBitmap);
//    }
//
//    //draw the view - will be called after touch event
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawPath(drawPath, drawPaint);
//    }
//
//    //register user touches as drawing action
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float touchX = event.getX();
//        float touchY = event.getY();
//        //respond to down, move and up events
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                drawPath.moveTo(touchX, touchY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                drawPath.lineTo(touchX, touchY);
//                break;
//            case MotionEvent.ACTION_UP:
//                drawPath.lineTo(touchX, touchY);
////                drawCanvas.drawPath(drawPath, drawPaint);
////                drawPath.reset();
//                break;
//            default:
//                return false;
//        }
//        //redraw
//        invalidate();
//        return true;
//
//    }
//
//    //update color
//    public void setColor(String newColor){
//        invalidate();
//        paintColor = Color.parseColor(newColor);
//        drawPaint.setColor(paintColor);
//    }
//
//    //start new drawing
//    public void startNew(){
//        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
//        drawPath = new Path();
//        Log.d("startNew", "startNew: hmm");
//        invalidate();
//    }
//
//    public void changeSize(float progress) {
//        drawPaint.setStrokeWidth(progress);
//    }
}