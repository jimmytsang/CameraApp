package com.example.myapplicationcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    int TAKE_PHOTO_CODE = 1;
    public static int count = 0;
    public boolean isErasemode = false;
    ImageView imageView;
    RelativeLayout layout;
    SeekBar seekBar;

    DrawingView drawingView;
    Button clear;
    Button erase;
    Button undo;
    Button redo;
    Button capture;
    TextView brushSize;

    Bitmap bmp;
    Bitmap alteredBitmap;
    Canvas canvas;
    Paint paint;
    Path path;
    Matrix matrix;
    float downx = 0;
    float downy = 0;
    float upx = 0;
    float upy = 0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        brushSize = findViewById(R.id.brushSize);

        layout = (RelativeLayout) findViewById(R.id.container);
        imageView = (ImageView) findViewById(R.id.imageView);
        drawingView = new DrawingView(this);
        clear = new Button(this);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.resetPaths();
            }
        });
        erase = (Button) findViewById(R.id.erase);
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isErasemode) {
                    isErasemode = false;
                    erase.setText("ERASE");
                } else {
                    isErasemode = true;
                    erase.setText("DRAW");
                }
                drawingView.onEraser();
            }
        });

        undo = new Button(this);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        redo = new Button(this);
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.bringToFront();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawingView.setWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        capture = (Button) findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Matrix matrix = new Matrix();

            matrix.postRotate(270);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth(), imageBitmap.getHeight(), true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);


            imageView.setImageBitmap(rotatedBitmap);
            LinearLayout draw = new LinearLayout(this);
            clear.setText("Clear All");
            layout.addView(clear);
            layout.addView(drawingView);
            seekBar.setVisibility(View.VISIBLE);
            erase.setVisibility(View.VISIBLE);
            brushSize.setVisibility(View.VISIBLE);
            undo.setVisibility(View.VISIBLE);
            redo.setVisibility(View.VISIBLE);
            capture.setVisibility(View.INVISIBLE);

            drawingView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            setContentView(layout);
            seekBar.bringToFront();
//            Bitmap mutableBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);
//            alteredBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap
//                    .getHeight(), imageBitmap.getConfig());
//            canvas = new Canvas(alteredBitmap);
//            paint = new Paint();
//            path = new Path();
//            paint.setColor(Color.GREEN);
//            paint.setStrokeWidth(20);
//            matrix = new Matrix();
//            canvas.drawBitmap(imageBitmap, matrix, paint);
//
//            imageView.setImageBitmap(alteredBitmap);
//            imageView.setOnTouchListener(this);
        }
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        Log.d("please", "onTouch: i'm touching" + action);
//        switch(action) {
//            case MotionEvent.ACTION_DOWN:
//                Log.d("please", "onTouch: i'm touching action down");
//                downx = event.getX();
//                downy = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.d("please", "onTouch: action move" + paint);
//                upx = event.getX();
//                upy = event.getY();
//                canvas.drawPath(path, paint);
//                imageView.invalidate();
//                downx = upx;
//                downy = upy;
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.d("please", "onTouch: i'm touching action up");
//                upx = event.getX();
//                upy = event.getY();
//                canvas.drawPath(path, paint);
//                imageView.invalidate();
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d("please", "onTouch: i'm touching action cancel");
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
}
