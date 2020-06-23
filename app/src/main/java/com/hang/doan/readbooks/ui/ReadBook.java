package com.hang.doan.readbooks.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Fragment.AccountFragment;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.data.Prefs;
import com.hang.doan.readbooks.dialog.BrightnessBottomSheetDialog;
import com.hang.doan.readbooks.dialog.FontBottomSheetDialog;
import com.hang.doan.readbooks.dialog.FontSizeBottomSheetDialog;
import com.hang.doan.readbooks.models.Font;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadBook extends AppCompatActivity {
    final String TAG = "HANG_DEBUG";
//    static int txtSize = 16;
//    final int MIN_TEXTSIZE = 12;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("storyDetail");

    String id;
    String id_tac_pham;

    String data;
    String chapterName;

    TextView read_book_chapter_name;
    TextView read_book_data;
    TextView read_book_btn_back;
    TextView read_book_btn_next;



    List<Integer> chapterIDbuyed = new ArrayList<>();

    String storyName;
    int chapterPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_item);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        id = (String) bundle.get("INDEX");
        Log.d(TAG, "onCreate: INDEX" + id);
        id_tac_pham = (String) bundle.get("id_tac_pham");

        read_book_chapter_name = findViewById(R.id.read_book_chapter_name);
        read_book_chapter_name.setTypeface(null, Typeface.BOLD);

        read_book_data = findViewById(R.id.read_book_data);

        read_book_btn_back = findViewById(R.id.read_book_btn_back);
        read_book_btn_next = findViewById(R.id.read_book_btn_next);

        applyFont(new Prefs(this).getFont());
        applyBrightness(new Prefs(this).getBrightness());
        applyFontSize(new Prefs(this).getFontSize());

        getChapterBuy();
        reloadData();

    }

    void getChapterBuy() {
        String path = "authorDetail/" + AccountFragment.userID + "/lstBuy/" + id_tac_pham;
        DatabaseReference authorRef = FirebaseDatabase.getInstance().getReference(path);
        Log.d(TAG, "path: " + path);

        authorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    int chapterID = ds.child("chapters").getValue(Integer.class);
                    chapterIDbuyed.add(chapterID);
                    Log.d(TAG, "getChapterBuy: chapters" + chapterID);
                }

                reloadData();

                read_book_btn_back.setOnClickListener(v -> {
                    if (Integer.parseInt(id) > 0) {
                        int id_new = Integer.parseInt(id) - 1;
                        id = String.valueOf(id_new);
                        reloadData();
                    }
                });

                read_book_btn_next.setOnClickListener(v -> {
                    int id_new = Integer.parseInt(id) + 1;
                    id = String.valueOf(id_new);
                    reloadData();
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void reloadData() {
        if (Integer.parseInt(id) <= 0) {
            read_book_btn_back.setVisibility(View.INVISIBLE);
        } else {
            read_book_btn_back.setVisibility(View.VISIBLE);
        }

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.child(id_tac_pham).child("chapters").child(id).child("data").getValue(String.class);
                chapterName = dataSnapshot.child(id_tac_pham).child("chapters").child(id).child("chapterName").getValue(String.class);
                chapterPrice = Integer.parseInt(dataSnapshot.child(id_tac_pham).child("chapters").child(id).child("price").getValue(String.class));
                storyName = dataSnapshot.child(id_tac_pham).child("generalInformation").child("name").getValue(String.class);
                if (data != null && chapterName != null) {
                    read_book_chapter_name.setText(chapterName);
                    if (chapterIDbuyed.contains(Integer.parseInt(id)) || chapterPrice < 1000) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            read_book_data.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            read_book_data.setText(Html.fromHtml(data));
                        }
                    } else {
                        Bundle data = new Bundle();
                        Intent intent = new Intent(ReadBook.this, PaymentActivity.class);
                        data.putString("storyID", id_tac_pham);
                        data.putString("storyName", storyName);
                        data.putString("userID", AccountFragment.userID);
                        data.putInt("storyChapter", Integer.parseInt(id));
                        data.putString("storyChapterName", chapterName);
                        data.putInt("storyChapterPrice", (chapterPrice / 1000) * 1000);
                        intent.putExtras(data);
                        startActivity(intent);
                    }

                } else {
                    read_book_chapter_name.setText("end chap");
                    read_book_data.setText("Hết rồi");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @OnClick(R.id.btnFont)
    void selectFont() {
        FontBottomSheetDialog dialog = new FontBottomSheetDialog(this);
        dialog.setCallback(this::applyFont);
        dialog.show();
    }

    private void applyFont(Font font) {
        read_book_data.setTypeface(font.getRegular(this));
        read_book_chapter_name.setTypeface(font.getBold(this));
        new Prefs(this).saveFont(font);
    }

    @OnClick(R.id.btnLight)
    void changeBrightness() {
        BrightnessBottomSheetDialog dialog = new BrightnessBottomSheetDialog(this);
        dialog.setCallback(this::applyBrightness);
        dialog.show();
    }

    private void applyBrightness(float brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.screenBrightness = brightness;
        window.setAttributes(params);
        new Prefs(this).saveBrightness(brightness);
    }

    @OnClick(R.id.btn_txt_size)
    void changeTextSize() {
        FontSizeBottomSheetDialog dialog = new FontSizeBottomSheetDialog(this);
        dialog.setCallback(this::applyFontSize, this::saveFontSize);
        dialog.show();
    }

    private void applyFontSize(int size) {
        read_book_data.setTextSize(size);
        read_book_chapter_name.setText(chapterName);
        read_book_chapter_name.setTextSize(size + 3);
    }

    private void saveFontSize(int size) {
        new Prefs(this).saveFontSize(size);
    }
}
