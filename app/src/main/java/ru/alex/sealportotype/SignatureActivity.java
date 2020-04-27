package ru.alex.sealportotype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static ru.alex.sealportotype.MainActivity.EDIT;


public class SignatureActivity extends AppCompatActivity implements IRunFinishCallback {
    public final static String FILE_NAME = "content.txt";
    private DrawingView dv;
    private MenuItem itemDone;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean mode = getIntent().getBooleanExtra(EDIT, false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Подпись на документе");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        dv = new DrawingView(this);
        if (mode) {
            dv.setBitmap(openText());
        }
        setContentView(dv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        itemDone = menu.findItem(R.id.itemDone);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDone:
                dv.getBase64();
                break;
            case R.id.itemPlus:
                dv.plusStroke();
                break;
            case R.id.itemMinus:
                dv.minusStroke();
                break;
            case R.id.itemErase:
                dv.createBitmap(getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight());
                File file = getFilesDir();
                File file1 = new File(file, FILE_NAME);
                boolean deleted = file1.delete();
                itemDone.setVisible(false);
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishThread(String base64, Bitmap bitmap) {
        saveText(base64);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Signature", "signature");
        startActivity(intent);
    }

    @Override
    public void onSign() {
        if (!itemDone.isVisible()) {
            itemDone.setVisible(true);
        }
    }


    public void saveText(String base64) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(base64.getBytes());
            Toast.makeText(this, "Подпись сохранена", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap openText() {
        Bitmap bitmap = null;
        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            byte[] decodedString = Base64.decode(text, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {

            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ignored) {
            }
        }
        return bitmap;
    }

}
