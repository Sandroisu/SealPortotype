package ru.alex.sealportotype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.END;

public class MainActivity extends AppCompatActivity implements IRunFinishCallback {

    private DrawingView dv;
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        Bitmap bitmap = null;
        if (savedInstanceState!=null){
            bitmap = savedInstanceState.getParcelable("BitmapImage");
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);
        dv = new DrawingView(this, mPaint, bitmap);
        FrameLayout frameLayout = new FrameLayout(this);

        final FloatingActionButton fabErase = new FloatingActionButton(this);
        final FloatingActionButton fabDone = new FloatingActionButton(this);
        tuneFloatingActionButton(fabErase, getDrawable(R.drawable.ic_erase), 8);
        ViewTreeObserver vto = fabErase.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tuneFloatingActionButton(fabDone, getDrawable(R.drawable.ic_done_black_24dp), fabErase.getWidth() + 16);

            }
        });

        frameLayout.addView(dv);
        frameLayout.addView(fabErase);
        frameLayout.addView(fabDone);

        setContentView(frameLayout);

        fabErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.onSizeChanged(getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight(),0, 0);
            }
        });
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.getBase64();
            }
        });
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("BitmapImage", dv.getBitmap());
    }

    @Override
    public void onFinishThread(String base64) {
        Toast.makeText(this, base64, Toast.LENGTH_SHORT).show();
    }

    private void tuneFloatingActionButton(FloatingActionButton fab, Drawable icon, int bottomMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = BOTTOM | END;
        params.bottomMargin = bottomMargin;
        params.rightMargin = 8;
        fab.setElevation(2);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
        fab.setImageDrawable(icon);
        fab.setRippleColor(Color.RED);
        fab.setLayoutParams(params);
    }
}

