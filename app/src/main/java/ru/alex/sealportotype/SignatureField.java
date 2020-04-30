package ru.alex.sealportotype;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SignatureField extends LinearLayout implements View.OnClickListener {

    private TextView tvSignatureLabel;
    private ImageView ivSignature;
    private ImageButton btnSignatureClear;
    private OnSignatureListener mListener;
    private String mBase64 = "";
    private int mDefaultSrc;

    public SignatureField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SignatureField, 0, 0);

        String src = a.getString(R.styleable.SignatureField_src);
        String label = a.getString(R.styleable.SignatureField_label);
        int removeIcon = a.getResourceId(R.styleable.SignatureField_removeIcon, 0);
        mDefaultSrc = a.getResourceId(R.styleable.SignatureField_defaultSrc, 0);
        a.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.signature_field, this, true);

        tvSignatureLabel = findViewById(R.id.tvSignatureLabel);
        ivSignature = findViewById(R.id.ivSignature);
        ivSignature.setOnClickListener(this);
        btnSignatureClear = findViewById(R.id.btnSignatureClear);
        btnSignatureClear.setOnClickListener(this);

        if(label != null) {
            setLabel(label);
        }

        if(mDefaultSrc != 0) {
            setDefaultSrc(mDefaultSrc);
        }

        if(removeIcon != 0) {
            setRemoveIcon(removeIcon);
        }

        if(src != null) {
            setSrc(src);
        }
    }

    /**
     * Добавление обработчика
     * @param listener обработчик
     */
    public void setOnSignatureListener(OnSignatureListener listener) {
        mListener = listener;
    }

    /**
     * Устнаовка подписи
     * @param base64 подпись в фомате base64
     */
    public void setSrc(String base64) {
        mBase64 = base64;

        if(mBase64 != null) {
            ivSignature.setImageBitmap(BitmapUtil.toBitmap(mBase64));
            btnSignatureClear.setVisibility(VISIBLE);
        } else {
            setDefaultSrc(mDefaultSrc);
            btnSignatureClear.setVisibility(GONE);
        }
    }

    /**
     * Установка наименования поля
     * @param label наименование
     */
    public void setLabel(String label) {
        tvSignatureLabel.setText(label);
    }

    /**
     * Иконка для очистки подписи
     * @param resId иден. подписи
     */
    public void setRemoveIcon(int resId) {
        btnSignatureClear.setImageResource(resId);
    }

    /**
     * Иконка для подписи по умолчанию
     * @param resId иден. ресурса
     */
    public void setDefaultSrc(int resId) {
        ivSignature.setImageResource(resId);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null) {
            switch (v.getId()) {
                case R.id.btnSignatureClear:
                    mListener.onClickSignature(OnSignatureListener.REMOVE, mBase64);
                    break;

                case R.id.ivSignature:
                    mListener.onClickSignature((mBase64 == null || mBase64.isEmpty()) ? OnSignatureListener.ADD : OnSignatureListener.UPDATE, mBase64);
                    break;
            }
        }
    }
}
