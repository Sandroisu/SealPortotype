package ru.alex.sealportotype;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
        implements OnSignatureListener {
    private SignatureField mSignatureField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignatureField = findViewById(R.id.signature);
        mSignatureField.setOnSignatureListener(this);
    }

    @Override
    public void onClickSignature(int mode, String signature) {
        Intent intent = new Intent(this, SignatureActivity.class);
        intent.putExtra(OnSignatureListener.MODE_NAME, mode);
        intent.putExtra(OnSignatureListener.TITLE, getString(R.string.sign));

        switch (mode) {
            case OnSignatureListener.ADD:
                startActivityForResult(intent, SignatureActivity.SIGNATURE_REQUEST_CODE);
                break;

            case OnSignatureListener.UPDATE:
                intent.putExtra(OnSignatureListener.IMAGE, signature);
                startActivityForResult(intent, SignatureActivity.SIGNATURE_REQUEST_CODE);
                break;

            case OnSignatureListener.REMOVE:
                confirmDialog(getString(R.string.signature_delete), getString(R.string.delete_confirmation), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // таким образом очищаем содержимое
                        mSignatureField.setSrc(null);
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SignatureActivity.SIGNATURE_REQUEST_CODE && resultCode == RESULT_OK) {
            String signature = data.getStringExtra(OnSignatureListener.IMAGE);
            mSignatureField.setSrc(signature);
        }
    }

    private void confirmDialog(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(getString(R.string.yes), listener);
        dialog.setNegativeButton(getString(R.string.no), null);
        dialog.create().show();
    }
}

