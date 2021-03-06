package com.hang.doan.readbooks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.BookItemClickListener;
import com.hang.doan.readbooks.adapters.LibraryAdapter;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.ui.BookDetailActivity;
import com.hang.doan.readbooks.ui.WriteNewActivity;
import com.hang.doan.readbooks.utils.Keyboard;

import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LibraryFragment extends Fragment implements BookItemClickListener {

    @BindView(R.id.rvStories)
    RecyclerView rvStories;
    private LibraryAdapter adapter;

    @BindView(R.id.edtSearch)
    EditText edtSearch;

    private List<Book> books = new ArrayList<>();

    private Context ct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ButterKnife.bind(this, view);
        ct = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        adapter = new LibraryAdapter(context, books);
        rvStories.setAdapter(adapter);
        rvStories.setLayoutManager(new GridLayoutManager(context, 2));
        adapter.setOnItemClickListener(LibraryFragment.this);

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(edtSearch.getText().toString().trim());
                return true;
            }
            return false;
        });

        fetchData();
    }

    private void fetchData() {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("authorDetail").child(userId);
        books.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String authorName = (String) dataSnapshot.child("authorName").getValue();
                if (!dataSnapshot.hasChildren()) {
                    return;
                }
                for (DataSnapshot child : dataSnapshot.child("lstStory").getChildren()) {
                    String storyID = (String) child.child("id").getValue();
                    addStory(authorName, storyID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addStory(String authorName, String storyId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("storyDetail").child(storyId).child("generalInformation");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String authorID = (String) dataSnapshot.child("authorID").getValue();
                String name = (String) dataSnapshot.child("name").getValue();
                String imgLink = (String) dataSnapshot.child("imgLink").getValue(String.class);
                Book book = new Book();
                book.setId_tac_gia(authorID);
                book.setId_tac_pham(storyId);
                book.setName(name);
                book.setImageURL(imgLink);
                //Log.d("HANG_DEBUG", "onDataChange: " +imgLink);
                book.setAuthorName(authorName);
                books.add(book);
                adapter.updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void performSearch(String keyword) {
        Keyboard.hideKeyboard(requireContext(), edtSearch);
        adapter.filter(keyword);
    }

    @OnClick(R.id.tvCancel)
    void cancelKeyboard() {
        Keyboard.hideKeyboard(requireContext(), edtSearch);
    }


    @Override
    public void onBookClick(int position, int type) {
        Intent intent = new Intent(ct, WriteNewActivity.class);
        intent.putExtra("user_id", books.get(position).getId_tac_gia());
        intent.putExtra("book_id", books.get(position).getId_tac_pham());

        startActivity(intent);
    }
}
