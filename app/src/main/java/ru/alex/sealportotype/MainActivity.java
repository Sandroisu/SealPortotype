package ru.alex.sealportotype;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static ru.alex.sealportotype.SignatureActivity.FILE_NAME;

public class MainActivity extends AppCompatActivity {
    public static final String EDIT = "edit_mode";
    private ImageView ivSign;
    private ImageButton btnDelete;
    private boolean isEdit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivSign = findViewById(R.id.ivSign);
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.signature_delete)
                        .setMessage(R.string.delete_confirmation)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = getFilesDir();
                                File file1 = new File(file, FILE_NAME);
                                boolean deleted = file1.delete();
                                btnDelete.setVisibility(View.GONE);
                                ivSign.setImageDrawable(getDrawable(R.drawable.ic_pen));
                            }
                        })
                        .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });
        final Intent intent = new Intent(this, SignatureActivity.class);
        ivSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(EDIT, isEdit);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSignFromFile(ivSign);

    }

    public void getSignFromFile(final ImageView view) {
        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            byte[] decodedString = Base64.decode(text, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            view.setImageBitmap(bitmap);
            btnDelete.setVisibility(View.VISIBLE);
            isEdit=true;
        } catch (IOException ex) {
            view.setImageDrawable(getDrawable(R.drawable.ic_pen));
            btnDelete.setVisibility(View.GONE);
            isEdit=false;
        } finally {

            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ignored) {
            }
        }
    }

}

