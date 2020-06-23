package com.hang.doan.readbooks.dialog;

import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.data.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FontSizeBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.tvSize)
    TextView tvSize;

    @BindView(R.id.seekbar)
    SeekBar seekBar;

    private Callback<Integer> onChangedListener;
    private Callback<Integer> onFinishListener;

    public void setCallback(Callback<Integer> onChangedListener, Callback<Integer> onFinishListener) {
        this.onChangedListener = onChangedListener;
        this.onFinishListener = onFinishListener;
    }

    public FontSizeBottomSheetDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.bottom_sheet_dialog);
        ButterKnife.bind(this);

        int defaultValue = new Prefs(context).getFontSize();
        seekBar.setMax(20);
        seekBar.setProgress(defaultValue - 12);
        tvSize.setText(context.getString(R.string.font_size, defaultValue));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int size = progress + 12;
                    tvSize.setText(context.getString(R.string.font_size, size));
                    if (onChangedListener != null) {
                        onChangedListener.callback(size);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (onFinishListener != null) onFinishListener.callback(seekBar.getProgress() + 12);
            }
        });
    }
}
