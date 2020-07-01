package com.hang.doan.readbooks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.ui.WriteNewActivity;

public class WriteFragment extends Fragment {

    Context ct;
    private FirebaseAuth mAuth;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_write, container, false);

        ct = getContext();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Intent intent = new Intent(ct, WriteNewActivity.class);
        // send movie information to deatilActivity
        intent.putExtra("user_id", mAuth.getUid());
        startActivity(intent);

        return view;
    }



}
