package com.hang.doan.readbooks.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.R;

import java.nio.Buffer;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigFeeDialog extends Dialog {
    @BindView(R.id.edtFee)
    EditText edtFee;
    @BindView(R.id.edtCommision)
    EditText edtCommision;

    private Callback<String> callback;

    public ConfigFeeDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_config_fee);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.95);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        window.setAttributes(params);

        edtFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Locale localeVN = new Locale("vi", "VN");
                NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
                currencyVN.setCurrency(Currency.getInstance(localeVN));
                String str1 = currencyVN.format(Integer.parseInt(edtFee.getText().toString()) *3/10);
                edtCommision.setText(str1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setCallback(Callback<String> callback) {
        this.callback = callback;
    }


    @OnClick(R.id.tvCancel)
    void cancelDialog() {
        super.cancel();
    }
//
//    @OnClick(R.id.tvSend)
//    void send() {
//        super.cancel();
//        if (callback == null) return;
//        String config = edtFee.getText().toString().trim();
//        callback.callback(config);
//    }
}
