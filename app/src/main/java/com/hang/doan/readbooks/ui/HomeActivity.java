package com.hang.doan.readbooks.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hang.doan.readbooks.Fragment.AccountFragment;
import com.hang.doan.readbooks.Fragment.HomeFragment;
import com.hang.doan.readbooks.Fragment.LibraryFragment;
import com.hang.doan.readbooks.Fragment.NotificationFragment;
import com.hang.doan.readbooks.Fragment.WriteFragment;
import com.hang.doan.readbooks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HomeActivity extends AppCompatActivity {
    final String TAG = "HANG_DEBUG";

    int backFlag = 0;

    static boolean isFirstLoad = true;

    private Handler warningHandler = new Handler();

    String storyName;
    String chapterName;
    String chapterID;
    String storyID;

    JSONObject jsonObject;
    public static JSONArray read_log;

    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //bottomNav.setBackgroundColor(Color.parseColor("#f1b814"));

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AccountFragment()).commit();
        }


        read_log = readLogData();

        if(read_log != null && isFirstLoad == true) {
            isFirstLoad = false;
            try {
                jsonObject = read_log.getJSONObject(read_log.length() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject != null && bottomNav.getSelectedItemId() == R.id.nav_home) {
                try {
                    storyName = jsonObject.get("storyName").toString();
                    chapterName = jsonObject.get("chapterName").toString();
                    chapterID = jsonObject.get("chapterID").toString();
                    storyID = jsonObject.get("storyID").toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                warningHandler.postDelayed(this::warning, 4000L);
            }
        }

        bottomNav.setSelectedItemId(R.id.nav_home);


    }

    private void warning() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("Bạn đã đọc truyện: " + storyName + "\n" + "Chương: " + chapterName + "\n" + "Bạn có muốn tiếp tục không?");
        builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), ReadBook.class);
                intent.putExtra("INDEX", chapterID);
                intent.putExtra("storyID", storyID);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_account:
                            selectedFragment = new AccountFragment();
                            break;
                        case R.id.nav_writeNew:
                            selectedFragment = new WriteFragment();
                            break;
                        case R.id.nav_library:
                            selectedFragment = new LibraryFragment();
                            break;
                        case R.id.nav_notify:
                            selectedFragment = new NotificationFragment();
                            break;
                        default:
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
            };

    @Override
    public void onBackPressed() {
        backFlag++;

        if (backFlag == 1) {
            super.onBackPressed();
            bottomNav.setSelectedItemId(R.id.nav_home);
            // go to previous activity
        } else if (backFlag == 2) {
            // ask user to exit
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        } else if (backFlag == 3) {
            // stop app
            this.finish();
            System.exit(0);
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                backFlag = 0;
            }
        }, 4000);
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
                //Log.d(TAG, "readLogData() Error" + e.toString());
            }
        }

        return null;
    }


}
