package com.hang.doan.readbooks.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.ChapterAdapter;
import com.hang.doan.readbooks.models.Author;
import com.hang.doan.readbooks.models.AuthorListStoryPost;
import com.hang.doan.readbooks.models.Chapter;
import com.hang.doan.readbooks.models.GeneralInformation;
import com.hang.doan.readbooks.models.Story_Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteNewActivity extends AppCompatActivity {
    public static final int RC_ADD_CHAPTER = 2;
    public static final int RC_CHOOSE_ORIGINAL_LANGUAGE = 3;
    public static final int RC_CHOOSE_TRANSLATED_LANGUAGE = 4;
    public static final int RC_CHOOSE_CATEGORY = 5;
    public static final int RC_CHOOSE_STATUS = 6;
    final String TAG = "HANG_DEBUG";

    final int SELECT_IMAGE = 1;


    @BindView(R.id.imgStory)
    ImageView imgStory;
    @BindView(R.id.tvOriginalLanguage)
    TextView tvOriginalLanguage;
    @BindView(R.id.tvTranslatedLanguage)
    TextView tvTranslatedLanguage;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    EditText activity_write_new_tentruyen;
    Button activity_write_new_btn_continute;
    EditText activity_write_new_item_info;

    TextView activity_write_new_btn_add_new_chappter;

    String user_id;
    String book_id;
    Story_Post story_Post;

    private DatabaseReference mDatabase;
    Author author = new Author();

    private ChapterAdapter adapter;
    private List<Chapter> chapters = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new);
        ButterKnife.bind(this);

        adapter = new ChapterAdapter(this, chapters);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        user_id = getIntent().getExtras().getString("user_id");
        book_id = getIntent().getExtras().getString("book_id");

        if (book_id != null) {
            //Lấy thông tin truyện về từ đây
            activity_write_new_btn_continute.setText("Cập nhật");
        }

        activity_write_new_item_info = findViewById(R.id.activity_write_new_item_info);
        activity_write_new_tentruyen = findViewById(R.id.activity_write_new_tentruyen);

        activity_write_new_btn_continute = findViewById(R.id.activity_write_new_btn_continute);
        activity_write_new_btn_continute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                story_Post = new Story_Post();
                String key = mDatabase.child("storyDetail").push().getKey();
                Log.d(TAG, "write new key: " + key);

                GeneralInformation generalInformation = new GeneralInformation();

                generalInformation.setStoryID(key);

                generalInformation.setIntroduction(activity_write_new_item_info.getText().toString());
                generalInformation.setAuthorID(user_id);
                generalInformation.setName(activity_write_new_tentruyen.getText().toString());
                generalInformation.setStatus("Tạo mới");
                generalInformation.setIntroduction(activity_write_new_item_info.getText().toString());
                story_Post.setGeneralInformation(generalInformation);
                story_Post.setChapters(chapters);

                mDatabase.child("storyDetail").child(key).setValue(story_Post);


                AuthorListStoryPost post = new AuthorListStoryPost();
                post.setId(key);
                post.setName(activity_write_new_tentruyen.getText().toString());

                author.getLstStory().add(post);
                mDatabase.child("authorDetail").child(user_id).setValue(author);

            }
        });

        activity_write_new_btn_add_new_chappter = findViewById(R.id.activity_write_new_btn_add_new_chappter);
        activity_write_new_btn_add_new_chappter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickNewChapter(view);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference authorRef = database.getReference("authorDetail/" + user_id);
        authorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Author m_auth = dataSnapshot.getValue(Author.class);
                author = m_auth;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        imgStory.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == RC_ADD_CHAPTER) {
            handleNewChapter(data);
        } else if (requestCode == RC_CHOOSE_ORIGINAL_LANGUAGE) {
            handleOriginalLanguage(data);
        } else if (requestCode == RC_CHOOSE_TRANSLATED_LANGUAGE) {
            handleTranslatedLanguage(data);
        } else if (requestCode == RC_CHOOSE_CATEGORY) {
            handleCategory(data);
        } else if (requestCode == RC_CHOOSE_STATUS) {
            handleStatus(data);
        }
    }

    private void handleOriginalLanguage(Intent data) {
        if (data == null) {
            return;
        }
        tvOriginalLanguage.setText(data.getStringExtra("selectedItem"));
    }

    private void handleTranslatedLanguage(Intent data) {
        if (data == null) {
            return;
        }
        tvTranslatedLanguage.setText(data.getStringExtra("selectedItem"));
    }

    private void handleCategory(Intent data) {
        if (data == null) {
            return;
        }
        tvCategory.setText(data.getStringExtra("selectedItem"));
    }

    private void handleStatus(Intent data) {
        if (data == null) {
            return;
        }
        tvStatus.setText(data.getStringExtra("selectedItem"));
    }

    private void handleNewChapter(Intent data) {
        if (data == null) return;
        Bundle bundle = data.getBundleExtra("data");
        String chapterName = bundle.getString("chapter_name");
        String content = bundle.getString("content");
        if (TextUtils.isEmpty(chapterName) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Chưa nhập tiêu đề hoặc nội dung", Toast.LENGTH_SHORT).show();
            return;
        }
        Chapter chapter = new Chapter();
        chapter.setChapterName(chapterName);
        chapter.setData(content);
        chapter.setPrice("0");
        chapters.add(chapter);
        adapter.notifyDataSetChanged();
    }

    public void clickNewChapter(View view) {
        Intent intent = new Intent(this, AddChapterActivity.class);
        startActivityForResult(intent, RC_ADD_CHAPTER);
    }

    @OnClick(R.id.imgBack)
    void back() {
        super.onBackPressed();
    }

    @OnClick({R.id.tvPickImage, R.id.imgStory})
    void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    @OnClick(R.id.layoutPickOriginalLanguage)
    void pickOriginalLanguage() {
        Intent intent = new Intent(this, PickActivity.class);
        intent.putExtra("type", "lang");
        startActivityForResult(intent, RC_CHOOSE_ORIGINAL_LANGUAGE);
    }

    @OnClick(R.id.layoutPickTranslatedLanguage)
    void pickTranslatedLanguage() {
        Intent intent = new Intent(this, PickActivity.class);
        intent.putExtra("type", "lang");
        startActivityForResult(intent, RC_CHOOSE_TRANSLATED_LANGUAGE);
    }

    @OnClick(R.id.layoutPickCategory)
    void pickCategory() {
        Intent intent = new Intent(this, PickActivity.class);
        intent.putExtra("type", "category");
        startActivityForResult(intent, RC_CHOOSE_CATEGORY);
    }

    @OnClick(R.id.layoutStatus)
    void pickStatus() {
        Intent intent = new Intent(this, PickActivity.class);
        intent.putExtra("type", "status");
        startActivityForResult(intent, RC_CHOOSE_STATUS);
    }


}
