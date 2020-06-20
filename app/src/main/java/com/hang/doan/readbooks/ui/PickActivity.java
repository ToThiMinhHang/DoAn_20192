package com.hang.doan.readbooks.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.PickItemAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickActivity extends AppCompatActivity {

    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    private PickItemAdapter adapter;

    private String type;
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        ButterKnife.bind(this);
        adapter = new PickItemAdapter(this, data);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);
        adapter.setOnItemClickListener(this::select);
        fetchData();
    }

    @OnClick(R.id.imgBack)
    public void back() {
        super.onBackPressed();
    }

    private void fetchData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        type = intent.getStringExtra("type");
        switch (type) {
            case "lang": fetchFromFile("language");
            break;
            case "status": fetchFromFile("status");
            break;
            case "category": fetchFromFile("category");
        }
    }

    private void fetchFromFile(String filename) {
        data.clear();
        try {
            InputStream is = getAssets().open(filename);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void select(int i) {
        Intent intent = new Intent();
        intent.putExtra("selectedItem", data.get(i));
        setResult(RESULT_OK, intent);
        finish();
    }
}
