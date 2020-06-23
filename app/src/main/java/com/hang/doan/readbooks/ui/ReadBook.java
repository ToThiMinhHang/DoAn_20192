package com.hang.doan.readbooks.ui;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Fragment.AccountFragment;
import com.hang.doan.readbooks.R;

import java.util.ArrayList;
import java.util.List;

public class ReadBook extends AppCompatActivity {
    final String TAG = "HANG_DEBUG";
    static int txtSize = 16;
    final int MIN_TEXTSIZE = 12;
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


    Button btnTextSize;
    Button btnFont;
    Button btnLight;

    List<Integer> chapterIDbuyed = new ArrayList<>();

    String storyName;
    int chapterPrice;

    boolean success = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_item);

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

//khai bao font size
        btnFont = findViewById(R.id.btn_font);
        btnLight= findViewById(R.id.btn_light);
        btnTextSize = findViewById(R.id.btn_txt_size);

        //getPermission();

        //set ligth
 //       boolean success = false;

        btnTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ReadBook.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

                TextView txt_size, txt_font;
                txt_size = bottomSheetDialog.findViewById(R.id.txt_size);

// set txt size
                SeekBar seekBar = bottomSheetDialog.findViewById(R.id.seekbar);
                txt_size.setText(txtSize + "/" + (seekBar.getMax() + MIN_TEXTSIZE)); //16/20
                seekBar.setProgress(txtSize - MIN_TEXTSIZE);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    int progressNew = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        txtSize = i + MIN_TEXTSIZE;
                        txt_size.setText(seekBar.getProgress() + MIN_TEXTSIZE + "/" + (seekBar.getMax() + MIN_TEXTSIZE)); //0/20
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        txt_size.setText(seekBar.getProgress() + MIN_TEXTSIZE + "/" + (seekBar.getMax() + MIN_TEXTSIZE)); //0/20
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            read_book_data.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
                            read_book_data.setTextSize(txtSize);
                        } else {
                            read_book_data.setText(Html.fromHtml(data));
                            read_book_data.setTextSize(txtSize);
                        }
                        read_book_chapter_name.setText(chapterName);
                        read_book_chapter_name.setTextSize(txtSize + 3);
                    }
                });

                bottomSheetDialog.show();
            }

        });
// set light
        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ReadBook.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_light);

                SeekBar seekBar_light = bottomSheetDialog.findViewById(R.id.seekbar_light);
                seekBar_light.setMax(255);
                seekBar_light.setProgress(getBrightness());

                Toast.makeText(ReadBook.this, "btnLight.setOnClickListener", Toast.LENGTH_LONG).show();


                seekBar_light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if (b && success) {
                            setBrightness(i);
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if(!success) {
                            Toast.makeText(ReadBook.this, "Permision not granted", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        getChapterBuy();
        reloadData();

    }

    //set brightness device
    private void setBrightness(int brightness){
        if (brightness < 0){
            brightness = 0;
        } else if (brightness > 255) {
            brightness = 255;
        }

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);

    }
    private int getBrightness(){
        int brightness = 100;
        try {
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightness;
    }
    private void getPermission(){
        boolean value;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            value = Settings.System.canWrite(getApplicationContext());
            if (value){
                boolean success = true;
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent, 1000);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean value = Settings.System.canWrite(getApplicationContext());
            if (value) {
                boolean success = true;
            } else {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void getChapterBuy() {
        String path = "authorDetail/" + AccountFragment.userID + "/lstBuy/" + id_tac_pham;
        DatabaseReference authorRef = FirebaseDatabase.getInstance().getReference(path);
        if (authorRef != null) {
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
                    read_book_chapter_name.setTextSize(txtSize + 3);

//                    read_book_data.setText(data);

                    if (chapterIDbuyed.contains(Integer.parseInt(id)) == true || chapterPrice < 1000) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            read_book_data.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
                            read_book_data.setTextSize(txtSize);
                        } else {
                            read_book_data.setText(Html.fromHtml(data));
                            read_book_data.setTextSize(txtSize);
                        }
                    } else {
//                        Toast.makeText(getApplicationContext(), "Mua truyen di may", Toast.LENGTH_SHORT).show();
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
}
