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
        // Trực tiếp chuyển đến AdminManagementUserActivity khi Fragment này được tạo.
        startActivity(new Intent(getActivity(), AdminManagementUserActivity.class));
        // Không gọi finish() để cho phép người dùng quay lại.
        return new View(getContext()); // Trả về một View rỗng vì không cần hiển thị gì
    }
}
