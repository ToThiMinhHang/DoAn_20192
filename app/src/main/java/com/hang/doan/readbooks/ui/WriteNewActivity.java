package com.hang.doan.readbooks.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.ChapterAdapter;
import com.hang.doan.readbooks.data.AuthorRepository;
import com.hang.doan.readbooks.data.StoryRepository;
import com.hang.doan.readbooks.dialog.LoadingDialog;
import com.hang.doan.readbooks.models.Author;
import com.hang.doan.readbooks.models.AuthorListStoryPost;
import com.hang.doan.readbooks.models.Chapter;
import com.hang.doan.readbooks.models.GeneralInformation;
import com.hang.doan.readbooks.models.Story;
import com.hang.doan.readbooks.models.Story_Post;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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
    @BindView(R.id.layoutInfo)
    View layoutInfo;
    @BindView(R.id.layoutChapters)
    View layoutChapters;
    @BindView(R.id.rvChapters)
    RecyclerView rvChapters;
    @BindView(R.id.edtTitle)
    EditText edtTitle;
    @BindView(R.id.edtAbstract)
    EditText edtAbstract;

    EditText activity_write_new_tentruyen;
    Button activity_write_new_btn_continute;
    EditText activity_write_new_item_info;

    private LoadingDialog loadingDialog;

    String user_id;
    String book_id;
    Story_Post story_Post;

    private boolean isInfoExpanded = true;
    private boolean isChaptersExpanded = false;

    private DatabaseReference mDatabase;
    Author author = new Author();

    private Bitmap bitmap;
    private ChapterAdapter adapter;
    private List<Chapter> chapters = new ArrayList<>();
    private String originalLanguage;
    private String translatedLanguage;
    private String category;
    private String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new);
        ButterKnife.bind(this);

        //setup views
        adapter = new ChapterAdapter(this, chapters);
        rvChapters.setAdapter(adapter);
        rvChapters.setLayoutManager(new LinearLayoutManager(this));
        loadingDialog = new LoadingDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        user_id = getIntent().getExtras().getString("user_id");
        book_id = getIntent().getExtras().getString("book_id");

        if (book_id != null) {
            activity_write_new_btn_continute = findViewById(R.id.btnSave);
            //Lấy thông tin truyện về từ đây
            activity_write_new_btn_continute.setText("Cập nhật");

            getStoryInformation(book_id, user_id);
        }
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

    void getStoryInformation(String book_id, String user_id) {
        DatabaseReference bookRef =  FirebaseDatabase.getInstance().getReference("storyDetail/" + book_id);

        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtTitle.setText(dataSnapshot.child("information").child("name").getValue(String.class));

                for (DataSnapshot snapshot : dataSnapshot.child("chapters").getChildren()) {
                    Chapter chapter = snapshot.getValue(Chapter.class);
                    chapters.add(chapter);
                    adapter.notifyDataSetChanged();
                }

//                for (DataSnapshot snapshot : dataSnapshot.child("generalInformation").child("mucsach").getChildren()) {
//                    String mucsach = snapshot.getValue(String.class);
//                    book_detail_mucsach.setText(book_detail_mucsach.getText().toString()  + mucsach+ "\n");
//                }

                Picasso.with(getApplicationContext())
                        .load(dataSnapshot.child("information").child("imgLink").getValue(String.class))
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(imgStory);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
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
        originalLanguage = data.getStringExtra("selectedItem");
        tvOriginalLanguage.setText(originalLanguage);
    }

    private void handleTranslatedLanguage(Intent data) {
        if (data == null) {
            return;
        }
        translatedLanguage = data.getStringExtra("selectedItem");
        tvTranslatedLanguage.setText(translatedLanguage);
    }

    private void handleCategory(Intent data) {
        if (data == null) {
            return;
        }
        category = data.getStringExtra("selectedItem");
        tvCategory.setText(category);
    }

    private void handleStatus(Intent data) {
        if (data == null) {
            return;
        }
        status = data.getStringExtra("selectedItem");
        tvStatus.setText(status);
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
        chapters.add(chapter);
        adapter.notifyDataSetChanged();
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

    @OnClick(R.id.layoutExpandInfo)
    public void expandInfo() {
        isInfoExpanded = !isInfoExpanded;
        if (isInfoExpanded) {
            layoutInfo.setVisibility(View.VISIBLE);
        } else {
            layoutInfo.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.layoutExpandChapters)
    public void expandChapter() {
        isChaptersExpanded = !isChaptersExpanded;
        if (isChaptersExpanded) {
            layoutChapters.setVisibility(View.VISIBLE);
        } else {
            layoutChapters.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tvAddChapter)
    public void addChapter() {
        Intent intent = new Intent(this, AddChapterActivity.class);
        startActivityForResult(intent, RC_ADD_CHAPTER);
    }

    @OnClick(R.id.btnSave)
    public void save() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        if (!validate()) {
            return;
        }
        StoryRepository storyRepository = new StoryRepository();
        AuthorRepository authorRepository = new AuthorRepository();
        String storyID = obtainStoryID(storyRepository);

        loadingDialog.show();
        uploadImage(storyID, url -> {
            GeneralInformation information = buildInformation(url);
            Story story = new Story(information, chapters);
            storyRepository.insertOrUpdate(storyID, story);
            authorRepository.addStory(buildAuthor(), buildAuthorListStoryPost(storyID, information.getName()), isSuccessful -> {
                if (!isSuccessful) onError("add story failed");
                else onSuccess();
            });
        });
    }

    private String obtainStoryID(StoryRepository repository) {
        Intent intent = getIntent();
        if (intent == null) {
            return repository.newKey();
        }
        if (!intent.hasExtra("story_id")) {
            return repository.newKey();
        }
        return intent.getStringExtra("story_id");

    }

    private void uploadImage(String storyID, Callback<String> callback) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("stories/" + storyID + ".jpg");
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                onError("Upload filed");
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri uri = task.getResult();
                if (uri == null) onError("Upload failed");
                else callback.callback(uri.toString());
            }
        });
    }

    private GeneralInformation buildInformation(String imageUrl) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            return new GeneralInformation();
        }
        GeneralInformation information = new GeneralInformation();
        information.setAuthorID(uid);
        information.setImgLink(imageUrl);
        information.setName(edtTitle.getText().toString().trim());
        information.setIntroduction(edtAbstract.getText().toString().trim());
        information.setOriginalLanguage(originalLanguage);
        information.setTranslatedLanguage(translatedLanguage);
        information.setStatus(status);
        information.setMucsach(Collections.singletonList(category));
        return information;
    }

    private Author buildAuthor() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return new Author();
        }
        Author author = new Author();
        author.setAuthorID(user.getUid());
        author.setAuthorName(user.getDisplayName());
        author.setImageUser(user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null);
        return author;
    }

    private AuthorListStoryPost buildAuthorListStoryPost(String storyID, String name) {
        AuthorListStoryPost post = new AuthorListStoryPost();
        post.setId(storyID);
        post.setName(name);
        return post;
    }

    private void onError(String message) {
        Log.d(TAG, "onError: " + message);
        loadingDialog.cancel();
        Toast.makeText(WriteNewActivity.this, "Oops! Có lỗi xảy ra rồi, hãy thử lại sau nhé...", Toast.LENGTH_SHORT).show();
    }

    private void onSuccess() {
        if (isExisted()) {
            Toast.makeText(this, "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Chúc mừng bạn đã có thêm đầu truyện mới!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean isExisted() {
        return getIntent() != null && getIntent().hasExtra("story_id");
    }

    private void alert(String message) {
        new AlertDialog.Builder(this).setTitle("Oops...").setMessage(message).show();
    }

    private boolean validate() {
        String title = edtTitle.getText().toString().trim();
        String introduction = edtAbstract.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            alert("Tiêu đề là tên tác phẩm và không được bỏ trống!");
            return false;
        }
        return true;
    }
}
