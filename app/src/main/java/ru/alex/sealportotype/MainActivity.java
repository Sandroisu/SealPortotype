package ru.alex.sealportotype;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static ru.alex.sealportotype.SignatureActivity.FILE_NAME;

public class MainActivity extends AppCompatActivity {
    public static final String EDIT = "edit_mode";
    private ImageView ivSign;
    private Button btnSign;
    private RelativeLayout rlSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSign = findViewById(R.id.btnSign);
        ivSign = findViewById(R.id.ivSign);
        rlSign = findViewById(R.id.rlSign);
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getFilesDir();
                File file1 = new File(file, FILE_NAME);
                boolean deleted = file1.delete();
                rlSign.setVisibility(View.GONE);
                btnSign.setVisibility(View.VISIBLE);
            }
        });
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
        getSignFromFile(ivSign, rlSign, btnSign);

    }

    public void getSignFromFile(final ImageView view, RelativeLayout layout, Button signBut) {
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
            layout.setVisibility(View.GONE);
            signBut.setVisibility(View.VISIBLE);
        } finally {

            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ignored) {
            }
        }
    }

}

