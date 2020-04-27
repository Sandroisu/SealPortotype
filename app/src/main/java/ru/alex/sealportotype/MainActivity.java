package ru.alex.sealportotype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PersistableBundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.END;
import static ru.alex.sealportotype.SignatureActivity.FILE_NAME;

public class MainActivity extends AppCompatActivity {

    public static final String EDIT = "edit_mode";
    private ImageView ivSign;
    private Button btnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSign = findViewById(R.id.btnSign);
        ivSign = findViewById(R.id.ivSign);

        final Intent intent = new Intent(this, SignatureActivity.class);
        ivSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(EDIT, true);
                startActivity(intent);
            }
        });
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        openText(ivSign);
    }

    public void openText(final ImageView view) {

        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            byte[] decodedString = Base64.decode(text, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            view.setImageBitmap(bitmap);
        } catch (IOException ex) {
            view.setVisibility(View.GONE);
            btnSign.setVisibility(View.VISIBLE);
        } finally {

            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ignored) {
            }
        }
    }
}

