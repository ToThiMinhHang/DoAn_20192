package com.hang.doan.readbooks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Author;
import com.hang.doan.readbooks.models.AuthorListStoryPost;
import com.hang.doan.readbooks.ui.BookDetailActivity;
import com.hang.doan.readbooks.ui.LoginActivity;
import com.hang.doan.readbooks.ui.MyStoryActivity;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;


    TextView txtImgLink;
    TextView userName;
    TextView email;
    Button bnt_sign_out;
    ImageView acc_userImg;

    Context ct;


    Author author = new Author();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ct = getContext();


        userName = (TextView) getView().findViewById(R.id.activity_account_detail_acc_userName);
        email = (TextView) getView().findViewById(R.id.acc_email);
        acc_userImg = getView().findViewById(R.id.acc_userImg);

        bnt_sign_out = getView().findViewById(R.id.btn_sign_out);
        bnt_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(ct)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(ct, LoginActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), ""+ e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //userName.setText(currentUser.getDisplayName());
        email.setText(currentUser.getUid());

        Picasso.with(ct)
                .load(currentUser.getPhotoUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(acc_userImg);



        TextView acc_myStory = getView().findViewById(R.id.acc_myStory);
        acc_myStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct, MyStoryActivity.class);
                // send movie information to deatilActivity
                intent.putExtra("user_id",  mAuth.getUid());
                startActivity(intent);
            }
        });




        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference authorRef = database.getReference("authorDetail/" + currentUser.getUid());
        authorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("authorName").getValue(String.class));
                Author m_auth = dataSnapshot.getValue(Author.class);
                if(m_auth != null)
                    author = m_auth;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Button activity_account_detail_update;
        activity_account_detail_update = getView().findViewById(R.id.activity_account_detail_update);
        activity_account_detail_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                author.setAuthorID(email.getText().toString());
                author.setAuthorName(userName.getText().toString());
                author.setLink(mAuth.getUid());

                authorRef.setValue(author);
            }
        });

    }


}
