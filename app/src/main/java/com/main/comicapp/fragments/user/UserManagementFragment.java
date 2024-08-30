package com.main.comicapp.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.main.comicapp.activities.admin.AdminManagementUserActivity;

public class UserManagementFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        startActivity(new Intent(getActivity(), AdminManagementUserActivity.class));
        return new View(getContext());
    }
}
