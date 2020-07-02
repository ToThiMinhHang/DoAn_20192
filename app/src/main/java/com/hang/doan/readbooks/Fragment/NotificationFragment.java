package com.hang.doan.readbooks.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.NotificationAdapter;
import com.hang.doan.readbooks.models.Notification;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment {

    @BindView(R.id.rvNotifications)
    RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    @BindView(R.id.layoutLoading)
    View layoutLoading;
    @BindView(R.id.layoutEmpty)
    View layoutEmpty;

    private List<Notification> notifications = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new NotificationAdapter(view.getContext(), notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvNotifications.setAdapter(adapter);

        fetchData();

    }

    private void fetchData() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("notifications").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    onEmptyNotification();
                    return;
                }
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    addNotification(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onEmptyNotification() {
        rvNotifications.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
    }

    private void onExistsNotification() {
        rvNotifications.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
    }

    private void addNotification(DataSnapshot snapshot) {
        Notification notification = snapshot.getValue(Notification.class);
        if (notification == null) {
            return;
        }
        getAvatarUrl(notification.getFromUserID(), url -> {
            onExistsNotification();
            notification.setFromUserImageURL(url);
            notifications.add(notification);
            adapter.notifyDataSetChanged();
        });
    }

    private void getAvatarUrl(String userId, Callback<String> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("authorDetail").child(userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    callback.callback(null);
                    return;
                }
                String imageURL = (String) dataSnapshot.child("imageUser").getValue(String.class);
                callback.callback(imageURL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.callback(null);
            }
        });
    }

    
}
