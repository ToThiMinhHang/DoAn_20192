package com.hang.doan.readbooks.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hang.doan.readbooks.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddChapterActivity extends AppCompatActivity {

    private static final int RC_PICK_FILE = 42;
    @BindView(R.id.edtChapterName)
    EditText edtChapterName;

    @BindView(R.id.edtContent)
    EditText edtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chapter);
        ButterKnife.bind(this);
    }

    public void back(View view) {
        super.onBackPressed();
    }

    public void selectPicture(View view) {

    }

    public void fillContent(View view) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        pickFile();
    }

    public void save(View view) {
        String chapterName = edtChapterName.getText().toString().trim();
        String content = edtContent.getText().toString().trim();
        Intent intent = new Intent();
        Bundle data = new Bundle();
        data.putString("chapter_name", chapterName);
        data.putString("content", content);
        intent.putExtra("data", data);
        setResult(WriteNewActivity.RC_ADD_CHAPTER, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickFile();
        }
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, RC_PICK_FILE);
    }

    private void handleFile(Intent data) {
        if (data == null) {
            return;
        }
        Uri uri = data.getData();
        if (uri == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sbTitle = new StringBuilder();
            StringBuilder sbContent = new StringBuilder();
            String line;
            boolean readContent = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#Title#")) {
                    readContent = false;
                    continue;
                }
                if (line.startsWith("#Content#")) {
                    readContent = true;
                    continue;
                }
                if (readContent) {
                    sbContent.append(line.trim());
                } else {
                    sbTitle.append(line);
                }
            }
            String title = sbTitle.toString();
            String content = sbContent.toString();
            edtChapterName.setText(title);
            edtContent.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PICK_FILE) {
            handleFile(data);
        }
    }
}
