package com.hang.doan.readbooks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.ui.LoginActivity;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    TextView txtImgLink;
    TextView userName;
    Button bnt_sign_out;

    Context ct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ct = getContext();

       // txtImgLink = (TextView) getView().findViewById(R.id.userImgLink);

        userName = (TextView) getView().findViewById(R.id.userName);

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
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        userName.setText(currentUser.getEmail());
    }


}
