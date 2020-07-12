package com.hang.doan.readbooks.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
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
import com.hang.doan.readbooks.dialog.ReportDialog;
import com.hang.doan.readbooks.models.Font;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    String storyID;
    String authorID;
    String crawlLink;
    String imgLink;

    String data;
    String chapterName;

    TextView read_book_chapter_name;
    TextView read_book_data;
    TextView read_book_btn_back;
    TextView read_book_btn_next;

    JSONArray read_log;

    List<Integer> chapterIDbuyed = new ArrayList<>();

    String storyName;
    int chapterPrice;

    private Handler warningHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_item);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        id = (String) bundle.get("INDEX");
        Log.d(TAG, "onCreate: INDEX" + id);
        storyID = (String) bundle.get("storyID");

        read_book_chapter_name = findViewById(R.id.read_book_chapter_name);
        read_book_chapter_name.setTypeface(null, Typeface.BOLD);

        read_book_data = findViewById(R.id.read_book_data);

        read_book_btn_back = findViewById(R.id.read_book_btn_back);
        read_book_btn_next = findViewById(R.id.read_book_btn_next);

        applyFont(new Prefs(this).getFont());
        applyBrightness(new Prefs(this).getBrightness());
        applyFontSize(new Prefs(this).getFontSize());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getChapterBuy();
        //reloadData();

        read_log = readLogData();
        if(read_log == null) {
            read_log = new JSONArray();
        }

        warningHandler.postDelayed(this::warning, 30*1*1000L);
    }

    void getChapterBuy() {
        String path = "authorDetail/" + FirebaseAuth.getInstance().getUid() + "/lstBuy/" + storyID;
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(path);
        Log.d(TAG, "path: " + path);

        databaseRef.addValueEventListener(new ValueEventListener() {
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
                data = dataSnapshot.child(storyID).child("chapters").child(id).child("data").getValue(String.class);
                chapterName = dataSnapshot.child(storyID).child("chapters").child(id).child("chapterName").getValue(String.class);
                chapterPrice = Integer.parseInt(dataSnapshot.child(storyID).child("chapters").child(id).child("price").getValue(String.class));
                storyName = dataSnapshot.child(storyID).child("generalInformation").child("name").getValue(String.class);
                authorID = dataSnapshot.child(storyID).child("generalInformation").child("authorID").getValue(String.class);
                crawlLink = dataSnapshot.child(storyID).child("generalInformation").child("link").getValue(String.class);
                imgLink = dataSnapshot.child(storyID).child("generalInformation").child("imgLink").getValue(String.class);
                if (data != null && chapterName != null) {

                    if (chapterIDbuyed.contains(Integer.parseInt(id)) || chapterPrice < 1000) {
                        if(crawlLink != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                read_book_data.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                read_book_data.setText(Html.fromHtml(data));
                            }
                        }
                        else {
                            read_book_data.setText(data);
                        }

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("chapterName", chapterName);
                            jsonObject.put("storyName", storyName);
                            jsonObject.put("chapterID", id);
                            jsonObject.put("storyID", storyID);
                            jsonObject.put("imgLink", imgLink);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        saveReadLog(jsonObject);


                    } else {
                        Bundle data = new Bundle();
                        Intent intent = new Intent(ReadBook.this, PaymentActivity.class);
                        data.putString("storyID", storyID);
                        data.putString("authorID", authorID);
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("storyID", storyID);
        startActivity(intent);
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

    @OnClick(R.id.btnReport)
    void report() {
        ReportDialog dialog = new ReportDialog(this);
        dialog.setCallback(report -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:minhhangbn1997@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Báo cáo vi phạm");
            intent.putExtra(Intent.EXTRA_TEXT, report);
            startActivity(Intent.createChooser(intent, "Báo cáo vi phạm"));
        });
        dialog.show();
    }

    private void warning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warining);
        builder.setMessage(R.string.waring_content);
        builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    void saveReadLog(JSONObject object) {
        //write log
        read_log.put(object);

        try {
            File dir = getFilesDir();
            File file = new File(dir, "read_log.txt");


            file.delete();

            FileOutputStream fileout = openFileOutput("read_log.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(read_log.toString());
            outputWriter.close();
            fileout.close();

//            //display file saved message
//            Toast.makeText(getBaseContext(), "File saved successfully!",
//                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            //Log.d(TAG, "saveReadLog Error: " + e.toString());
        }
    }


    JSONArray readLogData() {
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = openFileInput("read_log.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                datax.append(readString);
                readString = buffreader.readLine();
            }

            isr.close();
            fIn.close();

            File f = new File("read_log.txt");
            f.delete();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (datax.toString().length() > 0) {

            try {
                JSONArray jsonArray = new JSONArray(datax.toString());
                return jsonArray;
            } catch (JSONException e) {
                Log.d(TAG, "readLogData() Error" + e.toString());
            }
        }

        return null;
    }

}
