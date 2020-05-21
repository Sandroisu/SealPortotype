package ru.alex.sealportotype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SignatureActivity extends AppCompatActivity
        implements SignatureDrawingView.OnDrawingListener {
    public final static int SIGNATURE_REQUEST_CODE = 1;

    private SignatureDrawingView mSignatureDrawingView;
    private MenuItem mItemDone;
    private BitmapToStringTask mBitmapToStringTask;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSignatureDrawingView = new SignatureDrawingView(this);
        setContentView(mSignatureDrawingView);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        String title = getIntent().getStringExtra(OnSignatureListener.TITLE);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(getIntent().hasExtra(OnSignatureListener.IMAGE)) {
            String base64 = getIntent().getStringExtra(OnSignatureListener.IMAGE);
            Bitmap bitmap = BitmapUtil.toBitmap(base64);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            Bitmap resizeBmp = BitmapUtil.scaleToFitWidth(bitmap, width);
            mSignatureDrawingView.setBitmap(resizeBmp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mItemDone = menu.findItem(R.id.itemDone);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDone:
                Bitmap bitmap = mSignatureDrawingView.getBitmap();
                if(bitmap != null) {
                    mBitmapToStringTask = new BitmapToStringTask();
                    mBitmapToStringTask.execute(bitmap);
                }
                break;

            case R.id.itemErase:
                mSignatureDrawingView.createBitmap(getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight());
                mItemDone.setVisible(false);
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWrite() {
        if (!mItemDone.isVisible()) {
            mItemDone.setVisible(true);
        }
    }

    @Override
    protected void onDestroy() {
        if(mBitmapToStringTask != null) {
            mBitmapToStringTask.cancel(true);
        }
        super.onDestroy();
    }

    @SuppressLint("StaticFieldLeak")
    private class BitmapToStringTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            Bitmap resize = BitmapUtil.scaleToFitWidth(bitmaps[0], BitmapUtil.QUALITY_240p);
            return BitmapUtil.toBase64(resize, BitmapUtil.IMAGE_QUALITY);
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent();
            intent.putExtra(OnSignatureListener.IMAGE, s);

            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
