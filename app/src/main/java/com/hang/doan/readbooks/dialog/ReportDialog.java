package com.hang.doan.readbooks.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportDialog extends Dialog {

    @BindView(R.id.edtReport)
    EditText edtReport;


    private Callback<String> callback;

    public ReportDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_report);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.95);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        window.setAttributes(params);
    }

    public void setCallback(Callback<String> callback) {
        this.callback = callback;
    }

    @OnClick(R.id.tvCancel)
    void cancelDialog() {
        super.cancel();
    }

    @OnClick(R.id.tvSend)
    void send() {
        super.cancel();
        if (callback == null) return;
        String report = edtReport.getText().toString().trim();
        callback.callback(report);
    }

}
