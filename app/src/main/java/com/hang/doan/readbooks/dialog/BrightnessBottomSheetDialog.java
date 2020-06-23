package com.hang.doan.readbooks.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.data.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrightnessBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.seekbar_light)
    SeekBar sbBrightness;

    private Callback<Float> callback;

    public void setCallback(Callback<Float> callback) {
        this.callback = callback;
    }

    public BrightnessBottomSheetDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.bottom_sheet_dialog_light);
        ButterKnife.bind(this);

        float defaultValue = new Prefs(context).getBrightness();
        sbBrightness.setMax(10);
        sbBrightness.setProgress((int) (defaultValue * 10));
        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float value = progress / 10f;
                    callback.callback(value);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}
