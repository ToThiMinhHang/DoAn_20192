package com.hang.doan.readbooks.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.FontAdapter;
import com.hang.doan.readbooks.data.FontManager;
import com.hang.doan.readbooks.models.Font;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FontBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.rvFonts)
    RecyclerView rvFonts;

    private Callback<Font> callback;

    public FontBottomSheetDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_font);
        ButterKnife.bind(this);
        List<Font> fonts = FontManager.getInstance().getFonts();
        FontAdapter adapter = new FontAdapter(context, fonts);
        rvFonts.setAdapter(adapter);
        rvFonts.setLayoutManager(new LinearLayoutManager(context));
        adapter.setOnItemClickListener(i -> {
            if (callback != null) {
                callback.callback(fonts.get(i));
            }
            cancel();
        });
    }

    public void setCallback(Callback<Font> callback) {
        this.callback = callback;
    }
}
